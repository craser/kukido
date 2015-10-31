function MapUI(gpxFileName, mapDiv, elevationDiv) {
	var self = this; // private reference to avoid magical "this" bugs.

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
		self.map = new Map(mapDiv[0]);
    	self.map.renderTrack(gpxTrack);
		self.elevation = new Elevation(elevationDiv, gpxTrack);
		addElevationListeners(gpxTrack);
		self.map.zoomToBounds(gpxTrack.bounds);
	}

	(function() {
	    var url = "json/maps/" + gpxFileName;
	    $.getJSON(url, init);
	})();
}