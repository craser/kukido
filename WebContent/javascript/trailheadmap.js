 
// Takes the GMap and an array of DMG Maps, and renders the
// markers and sets up event handling.
function markMaps(gmap, maps, displayFuncs) {
    for (var i = 0; i < maps.length; i++) {
        var m = maps[i];
        var marker = new GMarker(new GLatLng(m.location.lat, m.location.lon));
        var onClick = buildOnClick(gmap, marker, m, i);
        gmap.addOverlay(marker);
        GEvent.addListener(marker, "click", onClick);
        displayFuncs[m.fileName] = onClick;
    }
}

function showMap(fileName) {
    displayFuncs[fileName]();
}

// Necessary evil, to get scoping to work correctly.
function buildOnClick(gmap, marker, m, i) {
    return function() {
            var origImg = marker.getIcon().image;
            marker.setImage("img/progress_small.gif");
            var k = function(mapJson) {
                var gpxTracks = eval(mapJson); // Should be an array of GPS Tracks.
                
                for (var t = 0; t < gpxTracks.length; t++) {
	                var gpxTrack = gpxTracks[t];
	                
	                var getColor = buildColorGetter(t + i);
	                var trackOverlays = renderTrack(gmap, gpxTrack, getColor);
	                var hideTrackInfo = function() {
	                            // Clear the overlay and the info window
	                            for (var i = 0; i < trackOverlays.length; i++) {
	                                gmap.removeOverlay(trackOverlays[i]);
	                            }
	                            var infoTable = document.getElementById(gpxTrack.fileName);
	                            var sidebar = infoTable.parentNode;
	                            sidebar.removeChild(infoTable);
	                            gmap.addOverlay(marker);
	                            if (sidebar.childNodes.length <= 0) { closeSlide(); }
	                        };
	                displayTrackInfo(marker, gpxTrack, getColor, hideTrackInfo); // Opens the info window
	                marker.setImage(origImg);
	                gmap.removeOverlay(marker);
                }
                
            }; // k
            var url = 'json/maps/' + m.fileName;
            GDownloadUrl(url, k);
        }; // onClick
}

function buildColorGetter(n) {
	return function () { return getColor(n); }
}

function displayTrackInfo(marker, gpxTrack, getColor, hideTrackInfo) {
    openSlide();
    
    // BUG! FIXME: Adding to the innerHTML property strips off
    // all the click handlers in the existing sub-elements.  So
    // I've got to build the actual DOM tree rather than just 
    // catting together some HTML.
    //var routeDescHtml = buildDesc(gpxTrack, color, gpxTrack.fileName);
    //sidebar.innerHTML += routeDescHtml;

    var div = document.createElement("div");
    div.setAttribute("id", gpxTrack.fileName);
    div.setAttribute("class", "trackInfo");
    
    var trackTitle = document.createElement("h4");
    trackTitle.setAttribute("style", "background-color: " + getColor());
    trackTitle.setAttribute("class", "trackInfoTitle");
    
    trackTitle.onclick = hideTrackInfo;
    trackTitle.innerHTML = gpxTrack.title; // Title is already HTML escaped.
    div.appendChild(trackTitle);
    
    var trackTable = document.createElement("table");
    trackTable.appendChild(buildTr("Date:", gpxTrack.date));
    trackTable.appendChild(buildTr("Duration:", gpxTrack.duration));
    trackTable.appendChild(buildTr("Distance (km):", gpxTrack.kilometers));
    trackTable.appendChild(buildTr("Climbing (m):", gpxTrack.climbing));
    
    var finalTd = document.createElement("td");
    finalTd.setAttribute("colspan", "2");
    finalTd.setAttribute("align", "right");
    finalTd.innerHTML = "<a href=\"maps/" + gpxTrack.fileName + "\">more &raquo;</a>";
    trackTable.appendChild(finalTd);
    
    div.appendChild(trackTable);
    
    var sidebar = document.getElementById("sidebarcontainer");
    sidebar.appendChild(div);
}

function buildTr(thData, tdData)
{
    var tr = document.createElement("tr");
    var th = document.createElement("th");
    th.appendChild(document.createTextNode(thData));
    var td = document.createElement("td");
    td.appendChild(document.createTextNode(tdData))
    tr.appendChild(th);
    tr.appendChild(td);
    return tr;
}

function buildDesc(gpxTrack, color, divId) {
    return "<div id=\"" + divId + "\"  class=\"trackInfo\">"
        + "<h4 id=\"" + divId + "-title\" style=\"background-color: " + color + "\">" + gpxTrack.title + "</h4>"
        + "<table>"
        + "<tr>"
        + "<th>Date:</th>"
        + "<td>" + gpxTrack.date + "</td>"
        + "</tr>"
        + "<tr>"
        + "<th>Duration:</th>"
        + "<td>" + gpxTrack.duration + "</td>"
        + "</tr>"
        + "<tr>"
        + "<th>Distance (km):</th>"
        + "<td>" + gpxTrack.kilometers + "</td>"
        + "</tr>"
        + "<tr>"
        + "<th>Climbing (m):</th>"
        + "<td>" + gpxTrack.climbing + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td colspan=\"2\" align=\"right\"></td>"
        + "</tr>"
        + "</table>"
        + "</div>";
}
