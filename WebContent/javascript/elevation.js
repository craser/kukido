function Elevation(mapUi, div, gpxTrack) {
	var self = this;
	var chart = null; // Set in renderElevation
	var mark = null;  // Holds currently-marked location on Map.

	function render() {
		$(div).CanvasJSChart({
			toolTip: {
				borderColor: "#000",
				borderThickness: 1,
				fontFamily: "HelveticaNeue-condensed,sans-serif",
				fontStyle: "normal",
				cornerRadius: 0,
				animationEnabled: false,
				contentFormatter: function(e) {
					var p = e.entries[0].dataPoint;
					var x = Math.round(p.x * 100) / 100;
					var y = Math.round(p.y);
					var t = p.t;
					var tip = "<span style=\"font-size: 1.2em\"> " + t + "</span><br/><b>distance:</b> " + x + "mi<br/><b>elevation:</b> " + y + "ft";
					addMark(p);
					return tip;
				}
			},
			axisX: {
				titleFontFamily: "HelveticaNeue-condensed,sans-serif",
				gridColor: "#fff",
				interval: 0.5,
				labelFontColor: "#000",
				lineColor: "#000",
				tickColor: "#000",
				lineThickness: 1,
				tickThickness: 1,
				valueFormatString: "0.##mi"
			},
			axisY: {
				valueFormatString: "0.##ft",
				titleFontFamily: "HelveticaNeue-condensed,sans-serif",
				gridColor: "#eee",
				interval: 200,
				includeZero: false,
				interlacedColor: "#fafafa",
				labelFontColor: "#000",
				lineColor: "#000",
				tickColor: "#000",
				lineThickness: 1,
				tickThickness: 1,
				gridThickness: 1
			},
			data: [{
				type: "splineArea",
				color: "#92c282",
				markerColor: "#f00",
				dataPoints: mapDataPoints(gpxTrack)
			}]
		});
		$(div).mouseout(removeMark);
	}

	function addMark(p) {
		removeMark();
		mark = mapUi.markLocation(gpxTrack.points[p.row]);
	}

	function removeMark(p) {
		if (mark) mapUi.removeMark(mark);
	}

	function init(gpxTrack) {
		chart = render();
	}

	function toMiles(meters) {
		return meters * 3.28084 / 5280;
	}

	function mapDataPoints(track) {
		var start = null; // ms
		return track.points.map(function(p, i) {
			start = start || new Date(p.time).getTime();
			return {
				x: toMiles(p.dst),
				y: p.elv * 3.28084,
				t: formatMs(new Date(p.time).getTime() - start),
				row: i
			};
		});
	}

	function formatMs(ms) {
		var t = ms;
		var SECOND = 1000;
		var MINUTE = 60 * SECOND;
		var HOUR = 60 * MINUTE;
		var DAY = 24 * HOUR;

		var days = Math.floor(t / DAY);
		t = t % DAY;
		var hours = Math.floor(t / HOUR);
		t = t % HOUR;
		var minutes = Math.floor(t / MINUTE);
		t = t % MINUTE;
		var seconds = Math.floor(t / SECOND);
		t = t % SECOND;

		function pad(n) {
			n = "" + n;
			while (n.length < 2) {
				n = "0" + n;
			}
			return n;
		}

		var s = (days ? days + ":" : "")
			+ (days ? pad(hours) + ":" : (hours ? hours + ":" : ""))
			+ (minutes ? pad(minutes) + ":" : "")
			+ pad(seconds);

		return s;
	}


	init(gpxTrack);
}