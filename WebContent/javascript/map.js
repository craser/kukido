function Map(div) {
    var overlays = [];
    var points = [];
    var descriptions = new Object();
    var markers = new Object();
    
	this.div = div;
	this.map = bind(this.div);
	this.bounds = null; // Set in zoomToBounds
	
	this.renderTrack = function(gpxTrack, getColor) {
	    var color = null;
	    with (gpxTrack.bounds) { this.zoomToBounds(minLat, maxLat, minLon, maxLon); }
	    for (var i = 0; i < gpxTrack.points.length; i++) {
	        var p = gpxTrack.points[i]; // GPS point
	        var g = new GLatLng(p.lat, p.lon);
	        points.push(g);
	        color = getColor(p); // Get the color for this point.
	        if ((i % 100) == 0) {
	            overlays.push(new GPolyline(points, color));
	            points = [];
	            points.push(g);
	        }
	    }
	    if (points.length > 0) overlays.push(new GPolyline(points, color));

	    for (var i = 0; i < overlays.length; i++) {
	        this.map.addOverlay(overlays[i]);
	    }
	};
	
	this.resize = function(w, h) {
		if (!!w) this.div.style.width = w + "px";
		if (!!h) this.div.style.height = h + "px";
	};
	
	this.zoomToBounds = function(minLat, maxLat, minLon, maxLon) {
		if (!!minLat) {
		    this.bounds = buildBounds(minLat, maxLat, minLon, maxLon);
		}
	    var zoom = this.map.getBoundsZoomLevel(this.bounds);
	    this.map.setCenter(this.bounds.getCenter(), zoom);
	};

	this.showImageOnMap = function(fileName)
	{
	    var marker = markers[fileName];
	    var desc = descriptions[fileName];
	    marker.openInfoWindowHtml(desc);
	};
	
	this.markLocation = function(p) {
		var loc = new GLatLng(p.lat, p.lon);
		var mark = new GMarker(loc, {
			clickable: false,
			dragable: false
		});
		this.map.addOverlay(mark);
		return mark;
	};
	
	this.getHeight = function() {
		return this.div.offsetHeight;
	};
	
	this.getWidth = function() {
		return this.div.offsetWidth;
	};
	
	this.resizeBy = function(dw, dh) {
		this.resize(this.getWidth() + dw, this.getHeight() + dh);
	};
	
	function bind(div) {
	    if (GBrowserIsCompatible()) {
	        var gmap = new GMap2(div);
	        gmap.addControl(new GLargeMapControl());
	        gmap.addControl(new GMapTypeControl());
	        gmap.addControl(new GScaleControl());
	        gmap.removeMapType(G_HYBRID_MAP);
	        gmap.addMapType(G_PHYSICAL_MAP);
	    	gmap.setMapType(G_PHYSICAL_MAP); 
	        gmap.enableScrollWheelZoom();

	        GEvent.addListener(gmap, "click", function(marker, point) {
	            if (!marker) { gmap.closeInfoWindow(); }
	        });

	        return gmap; 
	    }
	}

	function buildClickHandler(fileName)
	{
	    return function() { showImageOnMap(fileName); };
	}

	function buildBounds(minLat, maxLat, minLon, maxLon)
	{
	    var ne = new GLatLng(maxLat, maxLon);
	    var sw = new GLatLng(minLat, minLon);
	    return new GLatLngBounds(sw, ne);
	}
	
	function buildWayPoint(point, name, handler)
	{
	    var marker = new GMarker(point);
	    GEvent.addListener(marker, "click", handler);
	    return marker;
	}
}




