function MapFactory() {
	
	this.getMap = function(div) {
	    var options = {
		    	mapTypeId: google.maps.MapTypeId.TERRAIN,
		    	mapTypeControlOptions: {
		    		mapTypeIds: [google.maps.MapTypeId.TERRAIN, google.maps.MapTypeId.HYBRID, google.maps.MapTypeId.ROADMAP, google.maps.MapTypeId.SATELLITE],
		    		position: google.maps.ControlPosition.TOP_LEFT,
		    		style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
		    	},
		    	scroll: true,
		    	panControl: false,
		    	scaleControl: true,
		    	streetViewControl: false, // FIXME: Configure this.
		    	center: new google.maps.LatLng(0, 0, false) // FIXME: Pass in the actual center of the route.	    		
		    };
		var map = new google.maps.Map(div, options);
		return map;
	};
	
	this.getPolyLine = function(points, color, map) {
		return new google.maps.Polyline({path: points, strokeColor: color, map: map});
	};
	
	this.getLatLng = function(lat, lon) {
		return new google.maps.LatLng(lat, lon);
	};
	
	this.getBounds = function(n, e, s, w) {
		var ne = new google.maps.LatLng(n, e);
	    var sw = new google.maps.LatLng(s, w);
	    return new google.maps.LatLngBounds(sw, ne);
	};
	
	this.getMarker = function(loc, map) {
		var mark = new google.maps.Marker({
			position: loc,
			clickable: false,
			dragable: false
		});
		mark.setMap(map);
		return mark;
	};
}