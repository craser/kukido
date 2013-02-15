<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="net.kukido.blog.datamodel.*" %>
<%@ page import="net.kukido.maps.*" %>
<jsp:useBean id="entry" type="LogEntry" scope="request" />
<jsp:useBean id="map" type="Attachment" scope="request" />
<jsp:useBean id="track" type="GpsTrack" scope="request" />
<tiles:insert definition="mapLayout">
  <tiles:put name="title" type="string"><bean:write name="map" property="title" /></tiles:put>
  <tiles:put name="head" type="string">
    <link rel="stylesheet" type="text/css" href="css/map.css" />
    <script type="text/JavaScript" src="https://www.google.com/jsapi"> </script>
    <script type="text/JavaScript">
      google.load('visualization', '1.0', {'packages': ['corechart']});
    </script>
    <script type="text/JavaScript" src="javascript/gpxmap.js"> </script>
    <script type="text/JavaScript" src="javascript/colors.js"> </script>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAOggD5Fz3iK4oyqrD-5a3rxTtbl1hwI1wrVZ-gcFeSdvKcjZNDhTfeymXLgG1x94ojMlumMHhPx5OnA" type="text/javascript"></script>
    <!-- script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAOggD5Fz3iK4oyqrD-5a3rxTFRfqDGOwfXAlOK-54sJyR4NNS5RRcymeccR_BOTGOd_RmVO8QutZgJg" type="text/javascript"script -->
    <script>
      
      function bindMap(elementId)
      {
          if (GBrowserIsCompatible()) {
              var div = document.getElementById(elementId);
              var gmap = new GMap2(div);
              gmap.addControl(new GLargeMapControl());
              gmap.addControl(new GMapTypeControl());
              gmap.addControl(new GScaleControl());
              gmap.removeMapType(G_HYBRID_MAP);
              gmap.addMapType(G_PHYSICAL_MAP);
              gmap.enableScrollWheelZoom();
              
              // applying trkpt center:
              zoomToBounds(gmap, 
                           <nested:write name="track" property="bounds.minLatitude" />, 
                           <nested:write name="track" property="bounds.maxLatitude" />, 
                           <nested:write name="track" property="bounds.minLongitude" />, 
                           <nested:write name="track" property="bounds.maxLongitude" />);

              GEvent.addListener(gmap, "click", function(marker, point) {
                  if (!marker) { gmap.closeInfoWindow(); }
              });

              return gmap; 
          }
      }
      
      var map = [];      
      var descriptions = new Object();
      var markers = new Object();
      
      // Assign the actual Google Map obj. to the global var.
      window.addEventListener("load", function() { 
    	  fitToScreen(); 
    	  map = bindMap('map'); map.setMapType(G_PHYSICAL_MAP); 
    	  renderPageByName('<nested:write name="map" property="fileName" />', getDefaultColor); 
      });
      window.addEventListener("resize", function() { fitToScreen(); });
    </script>
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- GpxGmap.jsp -->
    <!--  div id="slider" class="open" onclick="toggleSlide()">&nbsp;</div -->
    <div id="map"></div>
    <div id="routeinfo">
    	<div id="elevationgraph"></div>
            <table border="0">
              <tr>
                <td>Total Time:</td>
                <td class="numeric"><nested:write name="track" property="formattedDuration" /></td>
              </tr>
              <tr>
                <td>Distance (miles):</td>
                <td class="numeric"><nested:write name="track" property="miles" format="0.00" /></td>
              </tr>
              <tr>
                <td>Distance (km):</td>
                <td class="numeric"><nested:write name="track" property="kilometers" format="0.00" /></td>
              </tr>
              <tr>
                <td>Climbing (Vert. ft):</td>
                <%-- Since GPX (and therefore my mapping code) uses meter for elevation, convert to feet. --%>
                <td class="numeric"><%= new java.text.DecimalFormat("0").format(track.getClimbingVertical() * 3.2808399f) %> </td>
              </tr>
              <tr>
                <td>Climbing (Vert. m):</td>
                <td class="numeric"><nested:write name="track" property="climbingVertical" format="0" /></td>
              </tr>
              <tr>
                <td colspan="2">
                  <dmg:entrylink entryId="<%= entry.getEntryId() %>">Trip Report</dmg:entrylink>
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <a href="attachments/${map.fileName}"><dmg:attachmentIcon attachmentType="<%= "map" %>" />Download GPX file</a>
                </td>
              </tr>
            </table>
            <div style="clear: both"></div>
    </div>
    <!-- End of GpxGmap.jsp -->
  </tiles:put>  

  <tiles:put type="string" name="sidebar">
  </tiles:put>
  
</tiles:insert>
