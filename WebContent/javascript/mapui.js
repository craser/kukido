function MapUI(mapDiv, elevationDiv) {
	var self = this; // private reference to avoid magical "this" bugs.
	this.map = new Map(mapDiv);
	
	this.fitToScreen = function() {
	    var body = document.getElementsByTagName("body")[0];
	    var bodyHeight = body.offsetHeight;
	    var windowHeight = (window.innerHeight)
	        ? window.innerHeight
	        : document.documentElement.clientHeight;
	    var dh = windowHeight - (bodyHeight + 0); // Set this to the margin of the body.
	    
	    this.map.resizeBy(0, dh);
	};
	
	this.renderPageByName = function(gpxFileName, getColor) {
	    // Default to a "rich blue" color for map rendering.
	    getColor = (getColor == null) ? function(p) { return "#FF0000"; } : getColor;
	    var url = "json/maps/" + gpxFileName;
	    var k = function (mapJson) {
	        var gpxTracks = eval(mapJson);
	        for (var i = 0; i < gpxTracks.length; i++) {
	        	var gpxTrack = gpxTracks[i];
	        	self.map.renderTrack(gpxTrack, getColor);
	        	self.renderElevation(elevationDiv, self.map.map, gpxTrack);
	        }
	    };
	    GDownloadUrl(url, k);
	};
	
	this.renderElevation = function(div, map, gpxTrack) {
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
			var mark = self.map.markLocation(gpxTrack.points[p.row]);
			var remove = function(p) {
				self.map.map.removeOverlay(mark);
			};
			google.visualization.events.addListener(chart, 'onmouseout', remove);
		};
		google.visualization.events.addListener(chart, 'onmouseover', add);
	};
	
    window.addEventListener("resize", function() { self.fitToScreen(); });
}