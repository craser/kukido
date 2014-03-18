function Sidebar(sidebarDiv) {
	
	var trackInfos = {};
	
	this.setHeight = function(h) {
		sidebarDiv.style.maxHeight = h + "px";
	};
	
	this.hideTrackInfo = function(track) {
		console.log("hideTrackInfo(" + track.fileName + ")");
		var infoDiv = trackInfos[track.fileName];
		trackInfos[track.fileName] = null;
		sidebarDiv.removeChild(infoDiv);
	};

	this.showTrackInfo = function(track, conf) {
		color = conf.color || Colors.getDefaultColor();
		console.log("showTrackInfo(" + track.fileName + ", \"" + color + "\")");
	    var trackInfo = document.createElement("div");
	    trackInfo.setAttribute("id", track.fileName);
	    trackInfo.setAttribute("class", "trackInfo");
	    trackInfo.style.borderColor = color;
	    
	    var trackTitle = document.createElement("h4");
	    trackTitle.setAttribute("style", "background-color: " + color);
	    trackTitle.setAttribute("class", "trackInfoTitle");
	    
	    trackTitle.innerHTML = track.title; // Title is already HTML-escaped.
	    if (conf.onclick) { trackTitle.addEventListener("click", conf.onclick); }
	    trackInfo.appendChild(trackTitle);
	    
	    var trackTable = document.createElement("table");
	    trackTable.appendChild(buildTr("Date:", track.date));
	    trackTable.appendChild(buildTr("Duration:", track.duration));
	    trackTable.appendChild(buildTr("Distance (km):", track.kilometers));
	    trackTable.appendChild(buildTr("Climbing (m):", track.climbing));
	    
	    var finalTd = document.createElement("td");
	    finalTd.setAttribute("colspan", "2");
	    finalTd.setAttribute("align", "right");
	    finalTd.innerHTML = "<a href=\"maps/" + track.fileName + "\">more &raquo;</a>";
	    trackTable.appendChild(finalTd);
	    
	    trackInfo.appendChild(trackTable);
	    trackInfos[track.fileName] = trackInfo;
	    
	    sidebarDiv.appendChild(trackInfo);
	};

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
}