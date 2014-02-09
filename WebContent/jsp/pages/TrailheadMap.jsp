<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="net.kukido.blog.datamodel.*" %>
<%@ page import="net.kukido.maps.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="maps" type="Collection" scope="request" />
<jsp:useBean id="bounds" type="GpsBounds" scope="request" />

<tiles:insert definition="mapLayout">
  <tiles:put name="title" type="string">Map of Geotagged Trails</tiles:put>
  <tiles:put name="head" type="string">
    <link rel="stylesheet" type="text/css" href="css/map.css" />
    <link rel="stylesheet" type="text/css" href="css/trailheadmap.css" />
    <script type="text/JavaScript" src="javascript/gpxmap.js"> </script>
    <script type="text/JavaScript" src="javascript/colors.js"> </script>
    <script type="text/JavaScript" src="javascript/trailheadmap.js"> </script>
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- TrailheadMap.jsp -->
    <div id="map"></div>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAOggD5Fz3iK4oyqrD-5a3rxTtbl1hwI1wrVZ-gcFeSdvKcjZNDhTfeymXLgG1x94ojMlumMHhPx5OnA" type="text/javascript"></script>
    <!-- script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAOggD5Fz3iK4oyqrD-5a3rxTFRfqDGOwfXAlOK-54sJyR4NNS5RRcymeccR_BOTGOd_RmVO8QutZgJg" type="text/javascript"script -->
    <script>
      
      var map = [];
      var displayFuncs = {};
      var gpsMaps = [      
      <nested:iterate name="maps" id="map" type="Attachment">
        <nested:iterate name="map" property="geotags" id="geotag" type="Geotag">
        {   location: { lat: <nested:write name="geotag" property="latitude" />, lon: <nested:write name="geotag" property="longitude" /> },
            fileName: "<nested:write name="map" property="fileName" />"
        },
        </nested:iterate>
      </nested:iterate>
      ];
      
      function window_onload() 
      {
          fitToScreen();
          closeSlide();
          map = bindMap('map'); 
          map.setMapType(G_PHYSICAL_MAP); 
          zoomToBounds(map,
                       <nested:write name="bounds" property="minLatitude" />, 
                       <nested:write name="bounds" property="maxLatitude" />, 
                       <nested:write name="bounds" property="minLongitude" />, 
                       <nested:write name="bounds" property="maxLongitude" />);
      }
      
      // Assign the actual Google Map obj. to the global var.
      window.onload = window_onload;
      window.onresize = fitToScreen;
    </script>
    <!-- End of TrailheadMap.jsp -->
  </tiles:put>  

  <tiles:put type="string" name="sidebar">
    <div id="sidebarcontainer"></div>
  </tiles:put>

</tiles:insert>
