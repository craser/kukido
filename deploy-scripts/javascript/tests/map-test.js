module("Map", {
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

});

test("new Map()", function() {
    var mapDiv = null;
    var mapOptions = null;
    google.maps.Map = function(div, options) {
        mapDiv = div;
        mapOptions = options;
    };
        
    var div = "MAMBO";
    var map = new Map(div);
    ok((mapDiv === div), "Pass div to google Map.");
    ok(mapOptions != null, "Map options must not be null.");
});

test("renderTrack", function() {
    var div = "MAPDIV";
    var track = buildTrack(300);
    var t = 0;                             // Index into track.points.

    google.maps.Polyline = function(options) {
        ok(options.path.length <= 101, "Path must be shorter than 101 points.  Found: " + options.path.length);
        for (var i = 0; i < options.path.length; i++, t++) {
            var mp = options.path[i];
            var ap = track.points[t];
            if (mp.lat != ap.lat || mp.lng != ap.lon) {
                ok(false, "Points in track and map must agree.");
            }
        }
        t--; // Last and first points overlap, so t must backtrack to last processed point.
    };

    var map = new Map(div);

    map.renderTrack(track, "#0f0"); // See checks above in mock Polyline.
    ok(t == (track.points.length - 1)); // Make sure we checked every point.
});

test("removeTrack", function() {
    var created = 0;
    google.maps.Polyline = function(conf) {
        created++;
        this.conf = conf;
        this.setMap = function(map) { ok(map === null, "Set map to null."); };
    };

    var track = buildTrack(300);
    var map = new Map("DIV");
    map.renderTrack(track, "#0f0");

    expect(created);
    map.removeTrack(track);
});

	
test("resize", function() {
    var div = {};
    div.style = {};

    var w = 249568;
    var h = 203495;

    var map = new Map(div);
    map.resize(w, h);
    ok(div.style.width = w + "px");
    ok(div.style.height = h + "px");
});

test("getHeight", function() {
    var h = 34523562;
    var div = {};
    div.style = {};
    div.offsetHeight = h;
    var map = new Map(div);
    var mh = map.getHeight();
    ok(mh == h, "Height must be retrieved from div.offsetHeight");
});
    
test("getWidth", function() {
    var w = 34523562;
    var div = {};
    div.style = {};
    div.offsetWidth = w;
    var map = new Map(div);
    var mw = map.getWidth();
    ok(mw == w, "Width must be retrieved from div.offsetWidth");
});


test("resizeBy", function() {
    var w = 249568;
    var h = 203495;

    var dw = -98;
    var dh = 2345;

    var div = {};
    div.style = {};
    div.offsetWidth = w;
    div.offsetHeight = h;

    var map = new Map(div);
    map.resize(w, h);
    ok(div.offsetWidth == w, "Div width is correct.");
    ok(div.offsetHeight == h, "Div height is correct.");

    var nw = w + dw;
    var nh = h + dh;

    map.resizeBy(dw, dh);
    ok(div.style.width == nw + "px");
    ok(div.style.height == nh + "px");
});

test("removeMark", function() {
    var map = new Map("DIV");
    expect(1);
    map.removeMark({ setMap: function(map) { ok(map === null, "Must set map to null."); } });
});

test("markLocation", function() {
    var gmap = null;
    google.maps.Map = function() {
        gmap = this;
        return MockMap.apply(this, arguments);
    };
        
    google.maps.Marker = function(conf) {
        ok(conf.position.lat == point.lat);
        ok(conf.position.lng == point.lon);
        this.setMap = function(m) {
            ok(m === gmap);
        };
    };

    var map = new Map("DIV");
    ok(gmap != null, "Set gmap.");
    var point = { lat: randomLatLon(), lon: randomLatLon() };
    expect(4);
    map.markLocation(point);
});
	
test("getTop", function() {
    var div = {};
    div.offsetTop = 1;

    div.offsetParent = {};
    div.offsetParent.offsetTop = 2;

    div.offsetParent.offsetParent = {};
    div.offsetParent.offsetParent.offsetTop = 3;
    
    div.offsetParent.offsetParent.offsetParent = {};
    div.offsetParent.offsetParent.offsetParent.offsetTop = 4;

    var map = new Map(div);
    var top = map.getTop();
    ok((top === 10), "expected 10, got: " + top);
});

test("zoomToBounds", function() {
    var bounds = { n: 1, e: 2, s: 3, w: 4 };
    google.maps.Map = function() {
        MockMap.apply(this, arguments);
        this.fitBounds = function(b) {
            ok(b.ne.lat == bounds.n);
            ok(b.ne.lng == bounds.e);
            ok(b.sw.lat == bounds.s);
            ok(b.sw.lng == bounds.w);
        };
    };

    var map = new Map("DIV");
    expect(4);
    map.zoomToBounds(bounds);            
});


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

