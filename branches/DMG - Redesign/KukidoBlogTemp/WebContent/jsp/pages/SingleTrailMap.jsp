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
    <script type="text/JavaScript" src="javascript/gpxmap.js"> </script>
    <script type="text/JavaScript" src="javascript/colors.js"> </script>
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- GpxGmap.jsp -->
    <div id="slider" class="open" onclick="toggleSlide()">&nbsp;</div>
    <div id="map"></div>
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
      
      
      function addMarkers(descriptions, markers) {
          <nested:iterate name="entry" property="attachments" id="attachment" type="Attachment">
            <nested:iterate name="attachment" property="geotags" id="geotag" type="Geotag">
              <logic:equal name="attachment" property="fileType" value="image">
              descriptions['<%= attachment.getFileName() %>'] = '<div><b><bean:write name="attachment" property="title" /></b></div><a class="postcard" href="attachments/<%= attachment.getFileName() %>"><img src="attachments/postcards/<%= attachment.getFileName() %>" /></a><div style="text-align: center"><a class="postcard" href="attachments/<%= attachment.getFileName() %>">(click to zoom)</div><div>Taken <nested:write name="attachment" property="dateTaken" format="MM/dd/yy hh:mm:ssa zz" /></div>';
              markers['<%= attachment.getFileName() %>'] = buildWayPoint(new GLatLng(<nested:write name="geotag" property="latitude" />,<nested:write name="geotag" property="longitude" />)
                                                                         ,'<nested:write name="attachment" property="title" />'
                                                                         ,buildClickHandler('<%= attachment.getFileName() %>'));
              map.addOverlay(markers['<%= attachment.getFileName() %>']);
              </logic:equal>
            </nested:iterate>
          </nested:iterate>
      }
      
      function parseAnchor() {
          var imgName = window.location.hash.substring(1);
          if (imgName) {
              showImageOnMap(imgName);
          }
      }
      
      var map = [];      
      var descriptions = new Object();
      var markers = new Object();
      
      // Assign the actual Google Map obj. to the global var.
      window.onload = function() { fitToScreen(); map = bindMap('map'); map.setMapType(G_PHYSICAL_MAP); addMarkers(descriptions, markers); parseAnchor(); renderMapByName('<nested:write name="map" property="fileName" />', getDefaultColor); };
      window.onresize = function() { fitToScreen(); };
    </script>
    <!-- End of GpxGmap.jsp -->
  </tiles:put>  

  <tiles:put type="string" name="sidebar">
    <div id="sidebarcontainer">
        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Route Info</tiles:put>
          <tiles:put type="string" name="content">
            <table border="0" width="100%">
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
                  <a href="attachments/<%= map.getFileName() %>"><dmg:attachmentIcon attachmentType="map" /> Download GPX file</a>
                </td>
              </tr>
            </table>
          </tiles:put>
        </tiles:insert>

        <nested:notEmpty name="entry" property="attachments">
          <tiles:insert definition="sidebarElement" flush="false">
            <tiles:put type="string" name="title">Gallery</tiles:put>
            <tiles:put type="string" name="content">
              <ul class="gallerymenu">
                <nested:iterate name="entry" property="attachments" id="attachment" type="net.kukido.blog.datamodel.Attachment">
                  <logic:equal name="attachment" property="fileType" value="image">
                      <logic:notEmpty name="attachment" property="geotags">
                        <li><a href="javascript:showImageOnMap('<%= attachment.getFileName() %>')" title="<%= attachment.getTitle() %>">
                        <img alt="<%= attachment.getTitle() %>" class="thumbnail" src="attachments/thumbs/<%= attachment.getFileName() %>" />
                        </a></li>
                      </logic:notEmpty>
                  </logic:equal>
                </nested:iterate>
              </ul>
            </tiles:put>
          </tiles:insert>
        </nested:notEmpty>
    </div>
  </tiles:put>
  
</tiles:insert>
