// Some functions in this file require the prior import of ajax.js

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
    return function() { showImageOnMap(fileName); }
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
    var sidebar = document.getElementById("sidebarcontainer");
    var slider = document.getElementById("slider");


    var divHeight = div.offsetHeight - 2; // offsetHeight includes the border, but the style.height doesn't.
    var newHeight = (divHeight + dh) + "px";
    div.style.height = newHeight;
    sidebar.style.height = newHeight;
    slider.style.height = newHeight;

}

function toggleSlide() {
    var sidebar = document.getElementById("sidebarcontainer").parentNode;
    var content = document.getElementById("map").parentNode;
    var slider = document.getElementById("slider");
    
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
    var slider = document.getElementById("slider");
    
    content.style.marginRight = "220px";
    sidebar.style.display = "block";
    slider.className = "open";
}
    
function closeSlide()
{
    var sidebar = document.getElementById("sidebarcontainer").parentNode;
    var content = document.getElementById("map").parentNode;
    var slider = document.getElementById("slider");
    
    content.style.marginRight = "0px";
    sidebar.style.display = "none";
    slider.className = "closed";
}

function renderTrack(map, gpxTrack, getColor)
{
    var overlays = [];
    var points = [];
    for (var i = 0; i < gpxTrack.points.length; i++) {
        var p = gpxTrack.points[i]; // GPS point
        var g = new GLatLng(p.lat, p.lon);
        points.push(g);
        var color = getColor(p); // Get the color for this point.
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
      
// Requires ajax.js
function renderMapByName(gpxFileName, getColor) {
    // Default to a "rich blue" color for map rendering.
    getColor = (getColor == null) ? function(p) { return "#FF0000"; } : getColor;
    var url = "json/maps/" + gpxFileName;
    var k = function (mapJson) {
        var gpxTracks = eval(mapJson);
        for (var i = 0; i < gpxTracks.length; i++) {
        	var gpxTrack = gpxTracks[i];
        	renderTrack(map, gpxTrack, getColor);
        }
    };
    GDownloadUrl(url, k);
}