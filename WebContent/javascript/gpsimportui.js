function LoadManager(filters, container, entryId, loader) {
    var self = this;
    var statusDiv = null;
    var ids = [];         // IDs of activities currently listed on device.
    var previousIds = []; // IDs of previously uploaded activities.
    var showOld = false;
    var showExisting = false;

    this.displayActivities = function() {
        container.html("");
        ids.map(function(id) {
            var row = buildActivityRow(id);
            function success() {
                row.off("click");
                row.removeClass("uploading");
                row.addClass("uploaded");
                //setTimeout(function() { row.fadeOut(); }, 500);
            }
            function fail(message) {
                row.removeClass("uploading");
                row.addClass("uploadfailed");
                var error = $("<div>");
                error.html(message);
                row.append(error);
            }
            row.click(function() {
                row.addClass("uploading");
                self.upload(id, success, fail);
            });

            container.append(row);
        });
    };

    this.upload = function(id, k, ek) {
        var url = "/home/GpsImport.do";
        var doc = loader.getActivityDetail(id, function(doc) {
            var postXml = formatDoc(doc);
            var data = {
                "activityId": id,
                "fileName": defaultFilename(id),
                "entryId": entryId,
                "xml": postXml
            };
            $.post(url, data).success(function(data, textStatus, jqXHR) {
                data = $.parseJSON(data);
                if (data.status == "success") {
                    k();
                }
                else {
                    ek(data.message);
                }
                self.setStatus(data.status);
            }, "json");
        });
    };

    this.noDevicesFound = function() {
        var status = self.setStatus("No devices found. Click to retry.");
        status.css("cursor", "pointer");
        status.click(init);
    };

    this.setStatus = function(msg) {
        debug("Status: " + msg);
        statusDiv.html("");
        var status = $("<div>");
        status.addClass("status");
        statusDiv.append(status);
        status.html(msg);
        return status;
    };

    this.clearStatus = function() {
        statusDiv.html("");
    };

    function initFilters() {
        $("#showold").click(toggleOld);
        $("#showexisting").click(toggleExisting);
    }

    function toggleOld() {
        showOld ? self.hideOld() : self.showOld();
    }

    function toggleExisting() {
        showExisting ? self.hideExisting() : self.showExisting();
    }

    this.showOld = function() {
        showOld = true;
        $("#showold").addClass("selected");
        var selector = showOld ? ".old:not(.existing)" : ".old";
        $(selector).fadeIn();
    };

    this.hideOld = function() {
        showOld = false;
        $("#showold").removeClass("selected");
        $(".old").fadeOut();
    };

    this.showExisting = function() {
        showExisting = true;
        $("#showexisting").addClass("selected");
        var selector = showExisting ? ".existing:not(.old)" : ".existing";
        $(selector).fadeIn();
    };

    this.hideExisting = function() {
        showExisting = false;
        $("#showexisting").removeClass("selected");
        $(".existing").fadeOut();
    };

    function debug() {
        console.log.apply(console, arguments);
    }

    function buildActivityRow(id) {
        var row = $("<div>");
        var name = $("<div>");
        var status = $("<span>");
        status.addClass("loadstatus");
        row.append(status);
        row.append(name);
        row.addClass("activity");

        // FIXME: STRICTLY FOR TESTING
        if (new Date(id).getDay() == 2) {
            row.addClass("existing");
        }

        if (previousIds.indexOf(id) > -1) {
            row.addClass("existing");
        }
        if (isOld(id)) { // FIXME: Set back to 15 for production.
            row.addClass("old");
        }
        name.html(new Date(id).toString());
        return row;
    }

    function isOld(id) {
        var limit = Math.max.apply(null, previousIds.map(function(id) {
            return new Date(id);
        }));

        return new Date(id).getTime() < limit;
    }

    function formatDoc(doc) {
        var s = new XMLSerializer().serializeToString(doc);
        return s;
    }

    function select(id) {
        selectedIds.push(id);
        self.setStatus("Selected: " + id);
    }

    function unselect(id) {
        selectedIds = selectedIds.filter(function(x) {
            return x != id;
        });
        self.setStatus("Unselected: " + id);
    }

    function createStatusDiv() {
        var div = $("<div>");
        container.append(div);
        return div;
    }

    function defaultFilename(id) {
        function pad(n) { return (n < 10) ? ("0" + n) : ("" + n); }
        var d = new Date(id); // Garmin uses timestamps for IDs.
        return d.getFullYear()
            + pad(d.getMonth() + 1)
            + pad(d.getDate())
            + ".gpx";
    }

    // Just a basic fucntion for now. Not hooked up to anything.
    function loadPreviousIds(k) {
        var url = "/home/ActivityIds.do";
	    $.getJSON(url, function(ids) {
	        previousIds = ids;
	        if (k) k();
	    });
    }

    function semaphore(n, k) {
        return function() {
            if (--n == 0) k();
        };
    }

    function init() {
        self.setStatus("Loading...");
        initFilters();
        // When we've both retrieved the existing uploaded activity IDs, and
        // read the activity listing from the device, then we're ready to
        // continue. So we use a semaphore function to control that flow.
        var gate = semaphore(2, self.displayActivities);
        loader.findDevices(function(devices) {
            if (devices.length > 0) {
                var deviceName = devices[0].displayName;
                self.setStatus("Reading from " + deviceName);
                loader.getActivityListing(function(doc) {
                    var nodes = doc.getElementsByTagName("Id");
                    ids = [];
                    for (var i = 0; i < nodes.length; i++) {
                        ids.push(nodes[i].textContent);
                    }
                    gate();
                });
            }
            else {
                self.noDevicesFound();
            }
        });
        loadPreviousIds(gate);
    }

    (function() {
        statusDiv = createStatusDiv();
        loader.status(function(status) {
            self.setStatus(status.text.join(" | "));
        });
        init();
    }());
}

/**
 * Abstraction layer between my code and Garmin's plug-in.
 * @param Garmin the "Garmin" object that contains the plugin. (Passed for scoping & testing purposes.)
 * @param console Errors/messages are logged to this console.
 */
function GarminLoader(Garmin, console) {
    var control = null;
    var listeners = new ListenerRegistry();

    /**
     * Asynchronously load the list of detected GPS devices.
     * @param k function(object[])
     */
    this.findDevices = function(k) {
        var e = "onFinishFindDevices";
        var f = function() {
            listeners.unbind(e, f);
            k(control.getDevices());
        };
        listeners.bind(e, f);
        control.findDevices(); // Start searching for devices.
    };
    
    /**
     * Sets the device from which to read.
     * @param id: Device id from findDevices.
     */
    this.setDevice = function(id) {
        control.setDevice(id);
    };

    /**
     * Asynchronously load the list of activities (as an XML doc)
     * @param k: function(doc)
     */
    this.getActivityListing = function(k) {
        var e = "onFinishReadFromDevice";
        var done = function() {
            listeners.unbind(e, done);
            k(control.gpsData);
        };
        listeners.bind(e, done);
        control.readDataFromDevice(Garmin.DeviceControl.FILE_TYPES.tcxDir);
    };

    /**
     * Asynchronously retrieve the activity detail as a TCX XML doc.
     * @param id: ID of the activity from readActivityListing.
     * @param k: function(doc)
     */
    this.getActivityDetail = function(id, k) {
        console.log("Detail ID: " + id);
        var e = "onFinishReadFromDevice";
        var done = function() {
            listeners.unbind(e, done);
            k(control.gpsData);
        };
        listeners.bind(e, done);
        control.readDetailFromDevice(Garmin.DeviceControl.FILE_TYPES.tcxDetail, id);
    };

    this.status = function(f) {
        listeners.bind("onProgressReadFromDevice", function() {
            var status = control.getDeviceStatus();
            f(status);
        });
    };

    function dumpActivityIds(doc) {
        var ids = doc.getElementsByTagName("Id");
        var id = ids[0].textContent;
        dumpDetail(id);
    }

    function dumpDevices(devices) {
        devices = devices || control.getDevices();
        console.log("Found " + control.numDevices + " devices:");
        devices.forEach(function(device) {
            console.log("    " + device);
        });
    }

    function logStatus() {
        var status = control.getDeviceStatus();
        console.log(status.text.join(" | "));
    }

    (function init() {
        control = new Garmin.DeviceControl();
        control.register(listeners);
        listeners.bind("onProgressReadFromDevice", logStatus);
        if (!control.isUnlocked() && !control.unlock(null) ) {
            throw "LOADING ERROR";
        }
    }());

    /**
     * The Garmin API doesn't seem to give us a way to UN-bind
     * listeners, so we hand it an instance of a universal listener
     * (ListenerRegistry), and then bind/unbind listeners to that.
     */
    function ListenerRegistry(control) {
        var self = this;
        var listeners = {}; // event --> handlers[]

        this.bind = function(event, f) {
            listeners[event].push(f);
        };

        this.unbind = function(event, f) {
            var ls = listeners[event];
            var i = ls.indexOf(f);
            if (i >= 0) {
                listeners[event].splice(i, 1);
            }
        };

        (function init() {
            var events = ["onStartFindDevices", "onFinishFindDevices", "onCancelFindDevices", "onProgressReadFromDevice", "onCancelReadFromDevice", "onFinishReadFromDevice"];
            events.forEach(function(event) {
                listeners[event] = [];
                self[event] = function() {
                    console.log(event + "()");
                    if (listeners[event]) {
                        listeners[event].forEach(function(f) { f.apply(null, arguments); });
                    }
                };
            });
        }());        
    }
}



