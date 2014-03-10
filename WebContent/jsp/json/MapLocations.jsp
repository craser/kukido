<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ page import="net.kukido.blog.datamodel.*" %>
<%@ page import="net.kukido.maps.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="maps" type="Collection" scope="request" />
<jsp:useBean id="bounds" type="GpsBounds" scope="request" />
{
	bounds: { n: <nested:write name="bounds" property="maxLatitude" />, 
			  s: <nested:write name="bounds" property="minLatitude" />, 
			  e: <nested:write name="bounds" property="maxLongitude" />,
			  w: <nested:write name="bounds" property="minLongitude" />
			},
	maps: [
	<nested:iterate name="maps" id="map" type="Attachment">
	  <nested:iterate name="map" property="geotags" id="geotag" type="Geotag">
	    {   location: { lat: <nested:write name="geotag" property="latitude" />, lon: <nested:write name="geotag" property="longitude" /> },
	        fileName: "<nested:write name="map" property="fileName" />"
	    },
	  </nested:iterate>
	</nested:iterate>
	]
}