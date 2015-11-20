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
					var tip = "<b>distance:</b> " + x + "m<br/><b>time:</b> " + t + "<br/><b>elevation:</b> " + y + "ft";
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
				valueFormatString: "0.##m",
				titleFontFamily: "HelveticaNeue-condensed,sans-serif",
				gridColor: "#f0f0f0",
				interval: 100,
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
				color: "#006600",
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

	function getMilesTo(a, b) {
		// Converts from degrees to radians.
		function radians(degrees) {
			return degrees * Math.PI / 180;
		};

		var EARTH_RADIUS_KM = 6371;
		var lat1 = a.lat;
		var lon1 = b.lon;
		var lat2 = b.lat;
		var lon2 = b.lon;

		var R = EARTH_RADIUS_KM * 1000;
		var dLat = radians(lat1 - lat2);
		var dLon = radians(lon1 - lon2);
		var n = Math.sin(dLat/2) * Math.sin(dLat/2)
			+ Math.cos(radians(lat1)) * Math.cos(radians(lat2))
			* Math.sin(dLon/2) * Math.sin(dLon/2);
		var c = 2 * Math.atan2(Math.sqrt(n), Math.sqrt(1-n));
		var d = R * c;

		return d * 3.28084 / 5280;
	}

	function mapDataPoints(track) {
		var start = null; // ms
		var prev = null;  // point
		var dist = 0; // total
		return track.points.map(function(p, i) {
			start = start || new Date(p.time).getTime();
			dist += prev ? getMilesTo(prev, p) : 0;
			prev = p;
			return {
				x: dist,
				y: p.elv * 3.28084,
				t: formatMs(new Date(p.time).getTime() - start),
				row: i
			};
		});
	}

	function formatMs(ms) {
		var t = ms;
		var f = "";
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

		console.log("formatMs(" + ms + "): " + s);

		return s;
	}


	init(gpxTrack);
}