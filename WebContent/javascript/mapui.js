function MapUI(gpxFileName, mapDiv, elevationDiv) {
	var self = this; // private reference to avoid magical "this" bugs.
	
	this.resizeMapDiv = function() {
	    var hw = $(window).innerHeight();
	    var hd = $("body").height();
	    var hc = $(mapDiv).height();
	    var dhc = (hd > hw) ? (hd - hw) : 0;
	    $(mapDiv).height((hc - dhc) + "px");
	};
	
	this.fitToScreen = function() {
		var bodyHeight = $("body").height();
		var windowHeight = $(window).innerHeight();
		var windowWidth = $(window).innerWidth();
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
		self.map = new Map(mapDiv[0]);
    	self.map.renderTrack(gpxTrack);
		self.elevation = new Elevation(elevationDiv, gpxTrack);
		addElevationListeners(gpxTrack);
		$(window).resize(self.fitToScreen);
		self.fitToScreen();
		self.map.zoomToBounds(gpxTrack.bounds);
	}

	(function() {
	    var url = "json/maps/" + gpxFileName;
	    $.getJSON(url, init);
	})();
}