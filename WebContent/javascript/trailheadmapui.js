function TrailheadMapUI(mapDiv, sidebarDiv) {
	var self = this; // private reference to avoid magical "this" bugs.
	var map = null;
	var sidebar = null;
	
	this.fitToScreen = function() {
	    var body = document.getElementsByTagName("body")[0];
	    var bodyHeight = body.offsetHeight;
	    var windowHeight = window.innerHeight || document.documentElement.clientHeight;
	    var windowWidth = window.innerWidth || document.documentElement.clientWidth;
	    var dh = windowHeight - (bodyHeight + 0); // Set this to the margin of the body.
	    var dw = windowWidth - map.getWidth();
	    map.resizeBy(dw, dh);
	};
	
	function init(mapLocations) {
	    window.addEventListener("resize", self.fitToScreen);
		self.fitToScreen();
		markMaps(mapLocations.locations);
		with (mapLocations.bounds) { map.zoomToBounds(n, s, e, w); }
	}
	
	function showRide(loc, mark) {
        mark.setMap(null); // Hide the marker.
		var url = 'json/maps/' + loc.fileName;
		var show = function(json) {
            var tracks = json_parse(json); // Should be an array of GPS Tracks.
            for (var t = 0; t < tracks.length; t++) {
                var track = tracks[t];
                var color = Colors.getNextColor();
                var hide = function() { 
                	map.removeTrack(track);
                	sidebar.hideTrackInfo(track);
                	markMap(loc);
                };
                map.renderTrack(track, color);
                sidebar.showTrackInfo(track, color, hide);
                //setTimeout(hide, 5000); // for testing.
            }
            
		};
		Ajax.get(url, show);
	}
	
	function markMaps(locations) {
	    for (var i = 0; i < locations.length; i++) {
	    	markMap(locations[i]);
	    }
	}
	
	function markMap(loc) {
        var mark = map.markLocation(loc.location);
        var show = function() { showRide(loc, mark); };
        mark.setClickable(true);
        mark.addListener("click", show);
	}

	(function() {
		map = new Map(mapDiv);
		sidebar = new Sidebar(sidebarDiv);
	    var k = function (mapJson) {
	        var mapLocations = json_parse(mapJson);
	        init(mapLocations);
	    };
	    Ajax.get("json/maplocations", k);
	})();
}