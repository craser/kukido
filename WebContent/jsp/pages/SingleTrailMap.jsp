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
    <dmg:mapScript />
    <script type="text/JavaScript" src="js/gpxmap.js"> </script>
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- GpxGmap.jsp -->
    <div id="map" data-map="<nested:write name="map" property="fileName" />"></div>
    <div id="routeinfo">
      <div id="routeinfohandle">
        Details
      </div>
      <div id="routeinfodetail">
        <div id="unitselection"></div>
        <div id="elevationgraph"></div>
        <div id="routesummary"></div>
        <table border="0">
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
