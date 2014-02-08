

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
	return function () { return Colors.getColor(n); }
}

function displayTrackInfo(marker, gpxTrack, getColor, hideTrackInfo) {
    openSlide();
    
    // Adding to the innerHTML property strips off
    // all the click handlers in the existing sub-elements.  So
    // I've got to build the actual DOM tree rather than just
    // catting together some HTML.

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
    td.appendChild(document.createTextNode(tdData));
    tr.appendChild(th);
    tr.appendChild(td);
    return tr;
}


