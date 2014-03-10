function TrailheadMapUI(mapDiv, sidebar) {
	var self = this; // private reference to avoid magical "this" bugs.

	function resizeMap(elementId) {
	    var div = document.getElementById(elementId);
	    var hw = window.innerHeight;
	    var hd = document.getElementsByTagName("body")[0].offsetHeight;
	    var hc = div.offsetHeight;
	    var dhc = (hd > hw) ? (hd - hw) : 0;
	    div.style.height = (hc - dhc) + "px";
	}	
	
	this.resizeMapDiv = function() {
	    var hw = window.innerHeight;
	    var hd = document.getElementsByTagName("body")[0].offsetHeight;
	    var hc = this.mapDiv.offsetHeight;
	    var dhc = (hd > hw) ? (hd - hw) : 0;
	    this.mapDiv.style.height = (hc - dhc) + "px";
	};
	
	this.fitToScreen = function() {
	    var body = document.getElementsByTagName("body")[0];
	    var bodyHeight = body.offsetHeight;
	    var windowHeight = window.innerHeight || document.documentElement.clientHeight;
	    var windowWidth = window.innerWidth || document.documentElement.clientWidth;
	    var dh = windowHeight - (bodyHeight + 0); // Set this to the margin of the body.
	    var dw = windowWidth - self.map.getWidth();
	    self.map.resizeBy(dw, dh);
	};
	
	function init(mapLocations) {
		// TODO: Initailize based on trailhead locations from mapLocations.
		// mapLocations.bounds
		// mapLocations.maps
	    window.addEventListener("resize", self.fitToScreen);
		self.fitToScreen();
		markMaps(mapLocations.maps);
		with (mapLocations.bounds) { self.map.zoomToBounds(n, s, e, w); }
	}
	
	function markMaps(maps) {
	    for (var i = 0; i < maps.length; i++) {
	        var m = maps[i];
	        self.map.markLocation(m.location);
	        //var onClick = buildOnClick(gmap, marker, m, i);
	        //var marker = new GMarker(new GLatLng(m.location.lat, m.location.lon));
	        //gmap.addOverlay(marker);
	        //GEvent.addListener(marker, "click", onClick);
	        //displayFuncs[m.fileName] = onClick;
	    }
	}

	(function() {
		self.map = new Map(mapDiv);
		self.sidebar = sidebar;
	    var k = function (mapJson) {
	        var mapLocations = json_parse(mapJson);
	        init(mapLocations);
	    };
	    Ajax.get("json/maplocations", k);
	})();
}