// Some functions in this file require the prior import of ajax.js

var descriptions = new Object();
var markers = new Object();
      


function bindMap(elementId)
{
    if (GBrowserIsCompatible()) {
        var div = document.getElementById(elementId);
        var gmap = new GMap2(div);
        gmap.addControl(new GLargeMapControl());
        gmap.addControl(new GMapTypeControl());
        gmap.addControl(new GScaleControl());
        gmap.removeMapType(G_HYBRID_MAP);
        gmap.addMapType(G_PHYSICAL_MAP);
        gmap.enableScrollWheelZoom();

        GEvent.addListener(gmap, "click", function(marker, point) {
            if (!marker) { gmap.closeInfoWindow(); }
        });

        return gmap; 
    }
}

function resizeMap(elementId) {
    var div = document.getElementById(elementId);
    var hw = window.innerHeight;
    var hd = document.getElementsByTagName("body")[0].offsetHeight;
    var hc = div.offsetHeight;
    var dhc = (hd > hw) ? (hd - hw) : 0;
    div.style.height = (hc - dhc) + "px";
}

function buildWayPoint(point, name, handler)
{
    var marker = new GMarker(point);
    GEvent.addListener(marker, "click", handler);
    return marker;
}

function buildClickHandler(fileName)
{
    return function() { showImageOnMap(fileName); };
}

function showImageOnMap(fileName)
{
    var marker = markers[fileName];
    var desc = descriptions[fileName];
    marker.openInfoWindowHtml(desc);
}

function buildBounds(minLat, maxLat, minLon, maxLon)
{
    var ne = new GLatLng(maxLat, maxLon);
    var sw = new GLatLng(minLat, minLon);
    return new GLatLngBounds(sw, ne);
}

function zoomToBounds(gmap, minLat, maxLat, minLon, maxLon)
{
    var bounds = buildBounds(minLat, maxLat, minLon, maxLon);
    var lat = (minLat + maxLat) / 2;
    var lon = (minLon + maxLon) / 2;
    var zoom = gmap.getBoundsZoomLevel(bounds);
    gmap.setCenter(new GLatLng(lat,lon), zoom);
}


function fitToScreen() {
    var body = document.getElementsByTagName("body")[0];
    var bodyHeight = body.offsetHeight;
    var windowHeight = (window.innerHeight)
        ? window.innerHeight
        : document.documentElement.clientHeight;
    var dh = windowHeight - (bodyHeight + 0); // Set this to the margin of the body.

    var div = document.getElementById("map");


    // 150 = height of elevation graph
    var divHeight = div.offsetHeight - 2; // offsetHeight includes the border, but the style.height doesn't.
    var newHeight = (divHeight + dh) + "px";
    div.style.height = newHeight;
}

function toggleSlide() {
    var sidebar = document.getElementById("sidebarcontainer").parentNode;
    
    if ("none" == sidebar.style.display) {
        openSlide();
    }
    else {
        closeSlide();
    }
}

function openSlide()
{
    var sidebar = document.getElementById("sidebarcontainer").parentNode;
    var content = document.getElementById("map").parentNode;
    
    content.style.marginRight = "220px";
    sidebar.style.display = "block";
}
    
function closeSlide()
{
    var sidebar = document.getElementById("sidebarcontainer").parentNode;
    var content = document.getElementById("map").parentNode;
    
    content.style.marginRight = "0px";
    sidebar.style.display = "none";
}


function renderElevation(div, map, gpxTrack)
{
	// Create and populate the data table.
	var data = new google.visualization.DataTable();
	data.addColumn('datetime', 'Time');
	data.addColumn('number', 'Elevation');
	for (i in gpxTrack.points) {
		var p = gpxTrack.points[i];
		var r = [new Date(p.time), p.elv];
		try { data.addRow(r); }
		catch (e) {
			console.log(e);
		}
	}

	// Create and draw the visualization.
	var chart = new google.visualization.AreaChart(div);
	chart.draw(data, {curveType: "function",
			   title: 'Elevation Profile',
			   backgroundColor: 'transparent',
			   legend: {
				   position: 'none'
			   },
			   colors: ['#9c9']
		});
	
	var add = function(p) { 
		var mark = markLocation(map, gpxTrack.points[p.row]);
		var remove = function(p) {
			map.removeOverlay(mark);
		};
		google.visualization.events.addListener(chart, 'onmouseout', remove);
	};
	google.visualization.events.addListener(chart, 'onmouseover', add);
}


function markLocation(map, p)
{
	var loc = new GLatLng(p.lat, p.lon);
	var mark = new GMarker(loc, {
		clickable: false,
		dragable: false
	});
	map.addOverlay(mark);
	return mark;
}

function renderTrack(map, gpxTrack, getColor)
{
    var overlays = [];
    var points = [];
    var color = null;
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
        map.addOverlay(overlays[i]);
    }

    return overlays;
}

function renderPageByName(gpxFileName, map, getColor) {
    // Default to a "rich blue" color for map rendering.
    getColor = (getColor == null) ? function(p) { return "#FF0000"; } : getColor;
    var url = "json/maps/" + gpxFileName;
    var k = function (mapJson) {
        var gpxTracks = eval(mapJson);
        for (var i = 0; i < gpxTracks.length; i++) {
        	var gpxTrack = gpxTracks[i];
        	renderTrack(map, gpxTrack, getColor);
        	var div = document.getElementById("elevationgraph");
        	renderElevation(div, map, gpxTrack);
        }
    };
    GDownloadUrl(url, k);	
}
