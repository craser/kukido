new OneBanana({
    name: "Map",
    setup: function() {
        google = {};
        google.maps = {};
        google.maps.LatLng = MockLatLng;
        google.maps.Polyline = MockPolyline;
        google.maps.Map = MockMap;
        google.maps.LatLngBounds = MockLatLngBounds;
        google.maps.Marker = MockMarker;
        
        google.maps.MapTypeId = {};
        google.maps.MapTypeId.TERRAIN = "TERRAIN";
        google.maps.MapTypeId.HYBRID = "HYBRID";
        google.maps.MapTypeId.ROADMAP = "ROADMAP";
        google.maps.MapTypeId.SATELLITE = "SATELLITE";

        google.maps.ControlPosition = {};
	    google.maps.ControlPosition.TOP_LEFT = "TOP_LEFT";

        google.maps.MapTypeControlStyle = {};
	    google.maps.MapTypeControlStyle.DROPDOWN_MENU = "DROPDOWN_MENU";

        Colors = {
            getDefaultColor: function() { return "#f00"; }
        };
    }
}).test(

    function test_newMap(test) {
        var mapDiv = null;
        var mapOptions = null;
        google.maps.Map = function(div, options) {
            mapDiv = div;
            mapOptions = options;
        };

        var div = "MAMBO";
        var map = new Map([div]);
        test.ok((mapDiv === div), "Pass div to google Map.");
        test.ok(mapOptions != null, "Map options must not be null.");
    },
    function test_renderTrack(test) {
        var div = "MAPDIV";
        var track = buildTrack(300);
        var t = 0;                             // Index into track.points.

        google.maps.Polyline = function(options) {
            test.ok(options.path.length <= 101, "Path must be shorter than 101 points.  Found: " + options.path.length);
            for (var i = 0; i < options.path.length; i++, t++) {
                var mp = options.path[i];
                var ap = track.points[t];
                if (mp.lat != ap.lat || mp.lng != ap.lon) {
                    test.ok(false, "Points in track and map must agree.");
                }
            }
            t--; // Last and first points overlap, so t must backtrack to last processed point.
        };

        var map = new Map(div);

        map.renderTrack(track, "#0f0"); // See checks above in mock Polyline.
        test.ok(t == (track.points.length - 1)); // Make sure we checked every point.
    },
    function test_removeTrack(test) {
        var created = 0;
        google.maps.Polyline = function(conf) {
            created++;
            this.conf = conf;
            this.setMap = function(map) { test.ok(map === null, "Set map to null."); };
        };

        var track = buildTrack(300);
        var map = new Map("DIV");
        map.renderTrack(track, "#0f0");

        test.expect(created);
        map.removeTrack(track);
    },
    function test_removeMark(test) {
        var map = new Map("DIV");
        test.expect(1);
        map.removeMark({ setMap: function(map) { test.ok(map === null, "Must set map to null."); } });
    },
    function test_markLocation(test) {
        var gmap = null;
        google.maps.Map = function() {
            gmap = this;
            return MockMap.apply(this, arguments);
        };

        google.maps.Marker = function(conf) {
            test.ok(conf.position.lat == point.lat);
            test.ok(conf.position.lng == point.lon);
            this.setMap = function(m) {
                test.ok(m === gmap);
            };
        };

        var map = new Map("DIV");
        test.ok(gmap != null, "Set gmap.");
        var point = { lat: randomLatLon(), lon: randomLatLon() };
        test.expect(4);
        map.markLocation(point);
    },
    function test_zoomToBounds(test) {
        var bounds = { n: 1, e: 2, s: 3, w: 4 };
        google.maps.Map = function() {
            MockMap.apply(this, arguments);
            this.fitBounds = function(b) {
                test.ok(b.ne.lat == bounds.n);
                test.ok(b.ne.lng == bounds.e);
                test.ok(b.sw.lat == bounds.s);
                test.ok(b.sw.lng == bounds.w);
            };
        };

        var map = new Map("DIV");
        test.expect(4);
        map.zoomToBounds(bounds);
    }
);


// 	this.showImageOnMap = function(fileName)
// 	{
// 	    var marker = markers[fileName];
// 	    var desc = descriptions[fileName];
// 	    marker.openInfoWindowHtml(desc);
// 	};
	



////////////////////////////////////////////////////////////////////////////////
// Mock Objects
function MockLatLng(lat, lng) {
    this.lat = lat;
    this.lng = lng;
}

function MockPolyline(conf) {
    this.conf = conf;
    this.setMap = function(map) {};
}

function MockMap(div, options) {
    this.div = div;
    this.options = options;
    this.fitBounds = function(bounds) {};
}

function MockLatLngBounds(sw, ne) {
    this.sw = sw;
    this.ne = ne;
}

function MockMarker(options) {
    this.options = options;
    this.setMap = function(map) {};
}

////////////////////////////////////////////////////////////////////////////////
// Helpers
function randomLatLon() {
    return (Math.random() * 1000) % 180;
}
function buildTrack(length) {
    var bounds = {
        n: randomLatLon(),
        e: randomLatLon(),
        s: randomLatLon(),
        w: randomLatLon()
    };
    var points = [];
    for (var i = 0; i < 300; i++) {
        var p = { lat: randomLatLon(), lon: randomLatLon() };
        points.push(p);
    }

    return {
        bounds: bounds,
        points: points,
        fileName: "FILE_NAME"
    };
}

