function MapUI(gpxFileName, mapDiv, elevationDiv) {
	var self = this; // private reference to avoid magical "this" bugs.
	
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
	
	function addElevationListeners(gpxTrack) {
		var mark = null;
		var add = function(p) { 
			mark = self.map.markLocation(gpxTrack.points[p.row]);
		};
		var remove = function(p) {
			self.map.removeMark(mark);
		};
		self.elevation.addListener('onmouseout', remove);
		self.elevation.addListener('onmouseover', add);
	}
	
	function init(gpxTracks) {
		var gpxTrack = gpxTracks[0]; // brain dead hack.
		self.resizeMapDiv();
		self.map = new Map(mapDiv);
    	self.map.renderTrack(gpxTrack);
		self.elevation = new Elevation(elevationDiv, gpxTrack);
		addElevationListeners(gpxTrack);
	    window.addEventListener("resize", self.fitToScreen);
		self.fitToScreen();
		self.map.zoomToBounds(gpxTrack.bounds);
	}

	(function() {
		self.elevationDiv = elevationDiv;
		self.mapDiv = mapDiv;
		
	    var url = "json/maps/" + gpxFileName;
	    var k = function (mapJson) {
	        var gpxTracks = eval(mapJson);
	        init(gpxTracks);
	    };
	    Ajax().get(url, k);
	})();
}