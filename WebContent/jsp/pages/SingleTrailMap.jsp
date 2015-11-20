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
    <script type="text/JavaScript" src="javascript/jquery-1.11.3.js"></script>
    <script type="text/JavaScript" src="javascript/jquery.canvasjs.min.js"> </script>
    <script type="text/JavaScript" src="javascript/canvasjs.min.js"> </script>
    <script type="text/JavaScript" src="javascript/mapui.js"> </script>
    <script type="text/JavaScript" src="javascript/elevation.js"> </script>
    <script type="text/JavaScript" src="javascript/map.js"> </script>
    <script type="text/JavaScript" src="javascript/colors.js"> </script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&key=AIzaSyBOUra7aNY509z2Z8mitJjK4FUpU_oOy1A"></script>
    <script type="text/JavaScript">
      $(window).load(function() {
    	  window.mapui = new MapUI('<nested:write name="map" property="fileName" />', $("#map"), $("#routeinfo"), $("#elevationgraph"));
      });
    </script>
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- GpxGmap.jsp -->
    <div id="map"></div>
    <div id="routeinfo">
      <div id="routeinfohandle">
        Details
      </div>
      <div id="routeinfodetail">
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
            <td>Climbing (Vert. ft):</td>
            <%-- Since GPX (and therefore my mapping code) uses meter for elevation, convert to feet. --%>
            <td class="numeric"><nested:write name="track" property="climbingVerticalFeet" format="0" /> </td>
          </tr>
          <tr>
            <td colspan="2">
              <dmg:entrylink entryId="<%= entry.getEntryId() %>">Trip Report</dmg:entrylink>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <dmg:downloadLink format="gpx" fileName="${map.fileName}"><dmg:attachmentIcon attachmentType="map" /> GPX</dmg:downloadLink>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <dmg:downloadLink format="tcx" fileName="${map.fileName}"><dmg:attachmentIcon attachmentType="map" /> TCX</dmg:downloadLink>
            </td>
          </tr>

        </table>
        <div style="clear: both"></div>
      </div>
    </div>
    <!-- End of GpxGmap.jsp -->
  </tiles:put>  

  <tiles:put type="string" name="sidebar">
  </tiles:put>
  
</tiles:insert>
