function MapUI(gpxFileName, mapDiv, routeInfoDiv, unitDiv, elevationDiv, summaryDiv) {
	var self = this; // private reference to avoid magical "this" bugs.

	this.units = {
		imperial: {
			distance: {
				label: "mi",
				interval: 0.5,
				convert: function(meters) {
					return (meters * 3.28084 / 5280);
				}
			},
			elevation: {
				label: "ft",
				interval: 200,
				convert: function(meters) {
					return meters * 3.28084;
				}
			}
		},
		metric: {
			distance: {
				label: "km",
				interval: 0.5,
				convert: function(meters) {
					return meters / 1000;
				}
			},
			elevation: {
				label: "m",
				interval: 50,
				convert: function(meters) {
				 	return meters;
				}
			}

		},
	};

	this.markLocation = function(p) {
		return self.map.markLocation(p);
	};

	this.removeMark = function(mark) {
		self.map.removeMark(mark);
	};

	this.setUnits = function(unitSpec) {
		var units = (typeof unitSpec) == "string" ? self.units[unitSpec] : unitSpec;
		self.routeInfo.setUnits(units);
	};
	
	function init(gpxTracks) {
		$("body").css("height", $(window).innerHeight());
		var gpxTrack = gpxTracks[0]; // brain dead hack.
		self.map = new Map(mapDiv[0]);
    	self.map.renderTrack(gpxTrack);
    	self.routeInfo = new RouteInfo(self, routeInfoDiv, elevationDiv, summaryDiv, gpxTrack);
		self.map.zoomToBounds(gpxTrack.bounds);
		self.setUnits(self.units.imperial); // FIXME: Will cause repeated rendering. Bad.
	}

	(function() {
	    var url = "json/maps/" + gpxFileName;
	    $.getJSON(url, init);
	})();
}

function UnitSelection(mapUi, unitDiv) {
}

function RouteInfo(mapUi, routeInfoDiv, elevationDiv, summaryDiv, gpxTrack) {
	var self = this;
	var drawer = $("#routeinfo");
	var contents = $("#routeinfodetail");
	var handle = $("#routeinfohandle");

	this.elevation = new Elevation(mapUi, elevationDiv, gpxTrack);
	this.summary = new Summary(mapUi, summaryDiv, gpxTrack);

	this.show = function() {
		drawer.animate({ bottom: 0 });
	};

	this.hide = function() {
		drawer.animate({ bottom: -contents.outerHeight() });
	};

	this.setUnits = function(units) {
		self.elevation.setUnits(units);
		self.summary.setUnits(units);
	};

	function init() {
		handle.click((function() {
			var open = (parseInt(drawer.css("bottom")) == 0);
			return function() {
				open ? self.hide() : self.show();
				open = !open;
			};
		}()));
	}

	init();
}

function Summary(mapUi, summaryDiv, gpxTrack) {
	this.setUnits = function(units) {
		render(units);
	};

	function render(units) {
		$(summaryDiv).html(""); // Clear out the container.
		var table = $("<table></table>");
		table.append(makeRow("Total Time", gpxTrack.duration));
		table.append(makeRow("Distance (" + units.distance.label + ")", round(units.distance.convert(gpxTrack.kilometers * 1000), 2)));
		table.append(makeRow("Climbing (" + units.elevation.label + ")", round(units.elevation.convert(gpxTrack.climbing))));
		$(summaryDiv).append(table);
	};

	function round(n, d) {
		d = d || 0;
		var m = Math.pow(10, d);
		return Math.round(n * m) / m;
	}

	function makeRow(label, data, dataClass) {
		var row = $("<tr></tr>")
		var labelCell = $("<td></td>");
		var dataCell = $("<td></td>");
		labelCell.html(label + ":");
		dataCell.html(data);
		if (dataClass) dataCell.addClass(dataClass);
		row.append(labelCell);
		row.append(dataCell);
		return row;
	}

	(function init() {
		render(mapUi.units.imperial);
	}());
}