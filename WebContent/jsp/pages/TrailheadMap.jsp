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
    <script type="text/JavaScript" src="javascript/jquery-1.11.3.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.28&sensor=false&key=AIzaSyBOUra7aNY509z2Z8mitJjK4FUpU_oOy1A"> </script>
    <script type="text/JavaScript" src="javascript/map.js"> </script>
    <script type="text/JavaScript" src="javascript/colors.js"> </script>
    <script type="text/JavaScript" src="javascript/sidebar.js"> </script>
    <script type="text/JavaScript" src="javascript/trailheadmapui.js"> </script>
    <script type="text/JavaScript">
      // Assign the actual Google Map obj. to the global var.
      $(window).load(function() {
          new TrailheadMapUI($("#map"), $("#sidebar"));
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
