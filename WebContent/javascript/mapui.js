function MapUI(gpxFileName, mapDiv, routeInfoDiv, elevationDiv) {
	var self = this; // private reference to avoid magical "this" bugs.

	this.markLocation = function(p) {
		self.map.markLocation(p);
	};

	this.removeMark = function(mark) {
		self.map.removeMark(mark);
	};
	
	function init(gpxTracks) {
		$("body").css("height", $(window).innerHeight());
		var gpxTrack = gpxTracks[0]; // brain dead hack.
		self.map = new Map(mapDiv[0]);
    	self.map.renderTrack(gpxTrack);
    	self.routeInfo = new RouteInfo(self, routeInfoDiv, elevationDiv, gpxTrack);
		self.map.zoomToBounds(gpxTrack.bounds);
	}

	(function() {
	    var url = "json/maps/" + gpxFileName;
	    $.getJSON(url, init);
	})();
}

function RouteInfo(mapUi, routeInfoDiv, elevationDiv, gpxTrack) {
	var self = this;
	var drawer = $("#routeinfo");
	var contents = $("#routeinfodetail");
	var handle = $("#routeinfohandle");

	this.elevation = new Elevation(mapUi, elevationDiv, gpxTrack);

	this.show = function() {
		drawer.animate({ bottom: 0 });
	};

	this.hide = function() {
		drawer.animate({ bottom: -contents.outerHeight() });
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