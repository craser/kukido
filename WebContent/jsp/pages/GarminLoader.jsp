<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" 
%><%--

    This page is used to pull in all the support files for the Garmin Communicator plugin. 

    Garmin uses Prototype, which fights with JQuery if they're loaded in the same scope. So
    we load this page in an iframe, and pull in the JavaScript objects from the enclosing
    page.

--%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Garmin Communicator Plugin</title>
    	<dmg:BaseHrefTag />
        <script type="text/javascript" src="javascript/garmin/prototype.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/util/Util-Broadcaster.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/util/Util-BrowserDetect.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/util/Util-DateTimeFormat.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/util/Util-PluginDetect.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/util/Util-XmlConverter.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/activity/GarminMeasurement.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/activity/GarminSample.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/activity/GarminSeries.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/activity/GarminActivity.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/activity/GpxActivityFactory.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/activity/TcxActivityFactory.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/device/GarminObjectGenerator.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/device/GarminGpsDataStructures.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/device/GarminPluginUtils.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/device/GarminDevice.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/device/GoogleMapController.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/device/GarminDevicePlugin.js">&#160;</script>
        <script type="text/javascript" src="javascript/garmin/device/GarminDeviceControl.js">&#160;</script>
	</head>
	<body>
		<object id="GarminActiveXControl"
				style="WIDTH: 0px; HEIGHT: 0px; visible: hidden" height="0" width="0"
				classid="CLSID:099B5A62-DE20-48C6-BF9E-290A9D1D8CB5">
			<object id="GarminNetscapePlugin" type="application/vnd-garmin.mygarmin" width="0" height="0">
			</object>
		</object>
	</body>
</html>
<% out.flush(); // Dust off and nuke 'em from orbit.  It's the only way to be sure. %>
