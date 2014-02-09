function Map(div) {
    var descriptions = new Object();
    var markers = new Object();
    var bounds = null;
    var map = null;
	
	this.renderTrack = function(gpxTrack, getColor) {
	    var lines = [];
	    var points = [];
	    var color = null;
	    getColor = getColor || function(p) { return "#FF0000"; };
	    with (gpxTrack.bounds) { setBounds(minLat, maxLat, minLon, maxLon); }
	    for (var i = 0; i < gpxTrack.points.length; i++) {
	        var p = gpxTrack.points[i]; // GPS point
	        var g = new google.maps.LatLng(p.lat, p.lon);
	        points.push(g);
	        color = getColor(p); // Get the color for this point.
	        if ((i % 100) == 0) {
	        	var line = new google.maps.Polyline({path: points, strokeColor: color, map: map});
	        	lines.push(line);
	            points = [];
	            points.push(g);
	        }
	    }
	    if (points.length > 0) {
	    	lines.push(new google.maps.Polyline({path: points, strokeColor: color, map: map}));
	    }
	};
	
	this.resize = function(w, h) {
		if (!!w) this.div.style.width = w + "px";
		if (!!h) this.div.style.height = h + "px";
	};
	
	this.resizeBy = function(dw, dh) {
		this.resize(this.getWidth() + dw, this.getHeight() + dh);
	};
	
	this.zoomToBounds = function() {
	    map.fitBounds(bounds);
	};

	this.showImageOnMap = function(fileName)
	{
	    var marker = markers[fileName];
	    var desc = descriptions[fileName];
	    marker.openInfoWindowHtml(desc);
	};
	
	this.markLocation = function(p) {
		var loc = new google.maps.LatLng(p.lat, p.lon);
		var mark = new google.maps.Marker(loc, {
			clickable: false,
			dragable: false
		});
		mark.setMap(map);
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
	
	function bind(div) {
	    var options = {
	    	mapTypeId: google.maps.MapTypeId.TERRAIN,
	    	mapTypeControlOptions: {
	    		mapTypeIds: [google.maps.MapTypeId.TERRAIN, google.maps.MapTypeId.HYBRID, google.maps.MapTypeId.ROADMAP, google.maps.MapTypeId.SATELLITE],
	    		position: google.maps.ControlPosition.TOP_RIGHT,
	    		style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
	    	},
	    	scroll: true,
	    	panControl: false,
	    	scaleControl: true,
	    	streetViewControl: false, // FIXME: Configure this.
	    	center: new google.maps.LatLng(0, 0, false) // FIXME: Pass in the actual center of the route.	    		
	    };
	    var gmap = new google.maps.Map(div, options);

	    return gmap;
	}
	
	function setBounds(minLat, maxLat, minLon, maxLon) {
		var ne = new google.maps.LatLng(maxLat, maxLon);
	    var sw = new google.maps.LatLng(minLat,  minLon);
	    bounds = new google.maps.LatLngBounds(sw, ne);
	};
    
	this.div = div;
	this.resizeBy(0, 0); // This sets up style properties needed by Google Maps API.
	map = bind(this.div);
}




