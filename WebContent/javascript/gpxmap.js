var MapUI = require('./maps/mapui');

$(window).load(function() {
  window.mapui = new MapUI($('#map').data('map'),
	$("#map"),
	$("#routeinfo"),
	$("#unitselection"),
	$("#elevationgraph"),
	$("#routesummary"));
});
