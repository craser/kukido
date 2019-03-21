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
    <dmg:mapScript />
    <script type="text/JavaScript" src="javascript/trailmaps.js"> </script>
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- TrailheadMap.jsp -->
    <div id="map"></div>
    <div id="sidebar"></div>
    <!-- End of TrailheadMap.jsp -->
  </tiles:put>  
</tiles:insert>
