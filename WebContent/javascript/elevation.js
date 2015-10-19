function Elevation(div, gpxTrack) {
	var self = this;
	var chart = null; // Set in renderElevation

	function render() {
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
		var c = new google.visualization.AreaChart(div[0]); // jquery --> dom
		c.draw(data, {curveType: "function",
				   title: 'Elevation Profile',
				   backgroundColor: 'transparent',
				   legend: {
					   position: 'none'
				   },
				   colors: ['#9c9']
			});
		
		return c;
	};
	
	this.addListener = function(eventName, f) {
		google.visualization.events.addListener(chart, eventName, f);
	};
	
	chart = render();
}