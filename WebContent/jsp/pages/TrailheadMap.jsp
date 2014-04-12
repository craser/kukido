<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="net.kukido.blog.datamodel.*" %>
<%@ page import="net.kukido.maps.*" %>
<%@ page import="java.util.*" %>

<tiles:insert definition="mapLayout">
  <tiles:put name="title" type="string">Map of Geotagged Trails</tiles:put>
  <tiles:put name="head" type="string">
    <link rel="stylesheet" type="text/css" href="css/map.css" />
    <link rel="stylesheet" type="text/css" href="css/trailheadmap.css" />
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&key=AIzaSyBOUra7aNY509z2Z8mitJjK4FUpU_oOy1A"> </script>
    <script type="text/JavaScript" src="javascript/mapfactory.js"> </script>
    <script type="text/JavaScript" src="javascript/effects.js"> </script>
    <script type="text/JavaScript" src="javascript/jsonparse.js"> </script>
    <script type="text/JavaScript" src="javascript/ajax.js"> </script>
    <script type="text/JavaScript" src="javascript/map.js"> </script>
    <script type="text/JavaScript" src="javascript/colors.js"> </script>
    <script type="text/JavaScript" src="javascript/sidebar.js"> </script>
    <script type="text/JavaScript" src="javascript/trailheadmapui.js"> </script>
    <script>
      // Assign the actual Google Map obj. to the global var.
      window.addEventListener("load", function() {
    	  var mapDiv = document.getElementById("map");
    	  var sidebarDiv = document.getElementById("sidebar");
    	  var ui = new TrailheadMapUI(mapDiv, sidebarDiv);
      });
    </script>
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- TrailheadMap.jsp -->
    <div id="map"></div>
    <div id="sidebar"></div>
    <!-- End of TrailheadMap.jsp -->
  </tiles:put>  
</tiles:insert>
