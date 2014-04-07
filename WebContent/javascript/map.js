function Map(div, mapFactory) {
	mapFactory = mapFactory || new MapFactory(); // Testability.
    var descriptions = new Object();
    var markers = new Object();
    var bounds = null;
    var map = null;
    var tracks = {}; // filename --> Polyline[]
	
	this.renderTrack = function(gpxTrack, color) {
		color = color || Colors.getDefaultColor();
	    var lines = [];
	    var points = [];
	    setBounds(gpxTrack.bounds);
	    for (var i = 0; i < gpxTrack.points.length; i++) {
	        var p = gpxTrack.points[i]; // GPS point
	        var g = mapFactory.getLatLng(p.lat, p.lon);
	        points.push(g);
	        if ((i % 100) == 0) {
	        	var line = mapFactory.getPolyLine(points, color, map);
	        	lines.push(line);
	            points = [];
	            points.push(g);
	        }
	    }
	    if (points.length > 0) {
	    	lines.push(mapFactory.getPolyLine(points, color, map));
	    }
	    tracks[gpxTrack.fileName] = lines;
	};
	
	this.removeTrack = function(gpxTrack) {
		var lines = tracks[gpxTrack.fileName];
		tracks[gpxTrack.fileName] = null;
		for (var i = 0; i < lines.length; i++) {
			var line = lines[i];
			line.setMap(null);
		}
	};
	
	this.resize = function(w, h) {
		if (!!w) this.div.style.width = w + "px";
		if (!!h) this.div.style.height = h + "px";
	};
	
	this.resizeBy = function(dw, dh) {
		this.resize(this.getWidth() + dw, this.getHeight() + dh);
	};
	
	this.zoomToBounds = function(b) {
		if (b) {
			var bounds = mapFactory.getBounds(b.n, b.e, b.s, b.w);
		    map.fitBounds(bounds);
		}
		else {
			map.fitBounds(bounds);
		}
	};

	this.showImageOnMap = function(fileName)
	{
	    var marker = markers[fileName];
	    var desc = descriptions[fileName];
	    marker.openInfoWindowHtml(desc);
	};
	
	this.markLocation = function(p) {
		var loc = mapFactory.getLatLng(p.lat, p.lon);
		var mark = mapFactory.getMarker(loc, map);
		return mark;
	};
	
	this.removeMark = function(mark) {
		mark.setMap(null);
	};
	
	this.getHeight = function() {
		return this.div.offsetHeight;
	};
	
	this.getWidth = function() {
		return this.div.offsetWidth;
	};
	
	this.getTop = function() {
		var top = 0;
		for (var n = div; n != null; n = n.offsetParent) {
			top += n.offsetTop;
		}
		return top;
	};
	
	function bind(div) {
	    var gmap = mapFactory.getMap(div);
	    return gmap;
	}
	
	function setBounds(b) {
		bounds = mapFactory.getBounds(b.n, b.e, b.s, b.w);
	};
    
	this.div = div;
	this.resizeBy(0, 0); // This sets up style properties needed by Google Maps API.
	map = bind(this.div);
}




