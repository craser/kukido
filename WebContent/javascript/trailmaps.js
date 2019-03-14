var TrailheadMapUI = require('./trailheadmapui');

// Assign the actual Google Map obj. to the global var.
$(window).load(function() {
  new TrailheadMapUI($("#map"), $("#sidebar"));
});