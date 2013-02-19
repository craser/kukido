function Elevation(div, gpxTrack) {
	var self = this;
	this.div = div;
	this.gpxTrack = gpxTrack;
	this.chart = null; // Set in renderElevation

	function render() {
		// Create and populate the data table.
		var data = new google.visualization.DataTable();
		data.addColumn('datetime', 'Time');
		data.addColumn('number', 'Elevation');
		for (i in self.gpxTrack.points) {
			var p = self.gpxTrack.points[i];
			var r = [new Date(p.time), p.elv];
			try { data.addRow(r); }
			catch (e) {
				console.log(e);
			}
		}

		// Create and draw the visualization.
		var chart = new google.visualization.AreaChart(self.div);
		chart.draw(data, {curveType: "function",
				   title: 'Elevation Profile',
				   backgroundColor: 'transparent',
				   legend: {
					   position: 'none'
				   },
				   colors: ['#9c9']
			});
		
		return chart;
	};
	
	this.addListener = function(eventName, f) {
		google.visualization.events.addListener(this.chart, eventName, f);
	};
	
	this.chart = render();
}