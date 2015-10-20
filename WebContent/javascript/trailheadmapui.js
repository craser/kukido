function TrailheadMapUI(mapDiv, sidebarDiv) {
	var self = this; // private reference to avoid magical "this" bugs.
	var map = new Map(mapDiv[0]);
	var sidebar = new Sidebar(sidebarDiv);
	
	this.fitToScreen = function() {
	    var body = document.getElementsByTagName("body")[0];
	    var bodyHeight = body.offsetHeight;
	    var windowHeight = window.innerHeight || document.documentElement.clientHeight;
	    var windowWidth = window.innerWidth || document.documentElement.clientWidth;
	    var dh = windowHeight - (bodyHeight + 2); // Set this to the margin of the body.
	    var dw = windowWidth - map.getWidth();
	    map.resizeBy(dw, dh);
	    sidebar.setHeight(map.getHeight());
	    sidebar.setTop(map.getTop());
	};
	
	function init(mapLocations) {
	    window.addEventListener("resize", self.fitToScreen);
		self.fitToScreen();
		markMaps(mapLocations.locations);
		map.zoomToBounds(mapLocations.bounds);
	}
	
	function showRide(loc, mark, color) {
        mark.setMap(null); // Hide the marker.
		var url = 'json/maps/' + loc.fileName;
		var show = function(tracks) {
            for (var t = 0; t < tracks.length; t++) {
                var track = tracks[t];
                var conf = {
                		color: color,
                		onclick: function hide() { 
                        	map.removeTrack(track);
                        	sidebar.hideTrackInfo(track);
                        	markMap(loc, color);
                        },
                        zoom: function() {
                        	map.zoomToBounds(track.bounds);
                        }
                };
                map.renderTrack(track, color);
                sidebar.showTrackInfo(track, conf);
            }
            
		};
		$.getJSON(url, show)
			.fail(function() { console.log.apply(console, arguments); })
			.always(function() { console.log("Ajax call to " + url + " complete.")});
	}
	
	function markMaps(locations) {
	    for (var i = 0; i < locations.length; i++) {
	        var color = Colors.getNextColor();
	    	markMap(locations[i], color);
	    }
	}
	
	function markMap(loc, color) {
        var mark = map.markLocation(loc.location);
        var show = function() { showRide(loc, mark, color); };
        mark.setClickable(true);
        mark.addListener("click", show);
	}

	(function() {
	    $.getJSON("json/maplocations", init)
			.fail(function() { console.log.apply(console, arguments); })
			.always(function() { console.log("Ajax call to json/maplocations complete.")});
	})();
}