function Sidebar(div) {
	
	var trackInfos = {};
	var height;
	
	this.setHeight = function(h) {
		height = h; // Used later in updateScrolling.
	};
	
	this.setTop = function(t) {
		$(div).css("top", t + "px");
	};
	
	this.hideTrackInfo = function(track) {
		console.log("hideTrackInfo(" + track.fileName + ")");
		var infoDiv = trackInfos[track.fileName];
		trackInfos[track.fileName] = null;

		$(infoDiv).fadeOut(250, function() {
			$(infoDiv).remove();
		});
	};

	this.showTrackInfo = function(track, conf) {
		var trackInfo = buildTrackInfo(track, conf);
	    trackInfos[track.fileName] = trackInfo;
	    $(div).append(trackInfo);
	    updateScrolling();
	};
	
	function updateScrolling() {
		var tall = $(div).height() >= height;
		console.log("updateScrolling() tall: " + tall);
		$(div).css("maxHeight", (tall ? (height + "px") : ""));
		$(div).css("overflowY", (tall ? "scroll" : "hidden"));
	}
	
	function buildTrackInfo(track, conf) {
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
	    trackTable.appendChild(buildTr("Distance (mi):", track.miles));
	    trackTable.appendChild(buildTr("Climbing (ft):", track.climbingFeet));
	    
	    var zoomTd = document.createElement("td");
	    var zoomLink = document.createElement("a");
	    zoomLink.setAttribute("href", "javascript:");
	    zoomLink.innerHTML = "&laquo; zoom";
	    zoomLink.addEventListener("click", conf.zoom);
	    zoomTd.appendChild(zoomLink);
	    
	    var entryTd = document.createElement("td");
	    entryTd.setAttribute("colspan", "2");
	    entryTd.setAttribute("align", "right");
	    entryTd.innerHTML = "<a href=\"maps/" + track.fileName + "\">more &raquo;</a>";

	    var lastRow = document.createElement("tr");
	    lastRow.appendChild(zoomTd);
	    lastRow.appendChild(entryTd);
	    
	    trackTable.appendChild(lastRow);
	    
	    trackInfo.appendChild(trackTable);
	    return trackInfo;
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