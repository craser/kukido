<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="net.kukido.maps.*" %>

<jsp:useBean id="entry" type="net.kukido.blog.datamodel.LogEntry" scope="request" />
<jsp:useBean id="map" type="net.kukido.blog.datamodel.Attachment" scope="request" />
<%-- jsp:useBean id="track" type="net.kukido.maps.GpsTrack" scope="request" / --%>
<jsp:useBean id="tracks" type="java.util.List" scope="request" />
<%--
GpsLocation
    private float latitude;
    private float longitude;
    private float elevation;
    private Date timestamp;
--%>
[
<nested:size name="tracks" id="numTracks" />
<nested:iterate id="track" name="tracks" type="GpsTrack" indexId="trackIndex">
{   "title": "<nested:write name="entry" property="title" />",
    "fileName": "<nested:write name="map" property="fileName" />",
    "trackName": "<nested:write name="track" property="name" />",
    "duration": "<nested:write name="track" property="formattedDuration" />",
    "date": "<nested:write name="track" property="startTime" format="MM/dd/yyyy" />",
    "climbing": <nested:write name="track" property="climbingVertical" format="0.00" />,
    "climbingFeet": <nested:write name="track" property="climbingVerticalFeet" format="0.00" />,
    "miles": <nested:write name="track" property="miles" format="0.00" />,
    "kilometers": <nested:write name="track" property="kilometers" format="0.00" />,
    "center": {
        "lat": <nested:write name="track" property="center.latitude" />,
        "lon": <nested:write name="track" property="center.longitude" />
        },
    "bounds": { <%-- Note that this is specific to North America! --%>
        "n": <nested:write name="track" property="bounds.maxLatitude" />,
        "s": <nested:write name="track" property="bounds.minLatitude" />,
        "e": <nested:write name="track" property="bounds.maxLongitude" />,
        "w": <nested:write name="track" property="bounds.minLongitude" />
        },
    "points": [
    <nested:size name="track" property="thinnedTrack" id="numPoints" />
    <nested:iterate name="track" property="thinnedTrack"  id="point" type="GpsLocation" indexId="pointIndex">
        {   "lat": <nested:write name="point" property="latitude" />,
            "lon": <nested:write name="point" property="longitude" />,
            "elv": <nested:write name="point" property="elevation" />,
            "dst": <nested:write name="point" property="distance" />,
            "bpm": <nested:write name="point" property="heartRate" />,
            "time": <nested:write name="point" property="routeTime" />
        }<nested:notEqual name="pointIndex" value="<%= String.valueOf(numPoints.intValue() - 1) %>">,</nested:notEqual>
    </nested:iterate>
    ]
}<nested:notEqual name="trackIndex" value="<%= String.valueOf(numTracks.intValue() - 1) %>">,</nested:notEqual>
</nested:iterate>
]
