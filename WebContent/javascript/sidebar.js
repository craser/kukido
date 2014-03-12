function Sidebar(div) {
	
	this.showTrackInfo = function(track, color) {
		color = color || Colors.getDefaultColor();
		console.log("showTrackInfo(" + track.fileName + ", \"" + color + "\")");
	};
	
	this.hideTrackInfo = function(track) {
		console.log("hideTrackInfo(" + track.fileName + ")");
	};
}