<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean id="entryId" class="java.lang.String" scope="request" />

<tiles:insert definition="pageLayout">

  <tiles:put type="string" name="head">
    <link rel="stylesheet" type="text/css" href="css/gpsimport.css">
    <script type="text/JavaScript" src="javascript/gpsimportui.js"> </script>
    <script type="text/JavaScript">

      $(window).load(function() {
          var container = $("#activitylist");
          var filters = $("activityfilters");
          var iframe = $("<iframe>");
          iframe.height(0);                               // Hide our shame.
          iframe.width(0);
          iframe.css("border", "none");
          iframe.attr("id", "testframe");
          iframe.attr("src", "garmin/loader");
          iframe.load(function() {
	      var Garmin = iframe[0].contentWindow.Garmin;
	      var loader = new GarminLoader(Garmin, console);
              var loadManager = new LoadManager(filters, container, '<bean:write name="entryId"/>', loader, iframe);
          });
          $("body").append(iframe);
      });

    </script>
  </tiles:put>

  <tiles:put type="string" name="content">
    <div id="donelink">
      <html:link action="LogEditForm" paramId="entryId" paramName="entryId">done</html:link>
    </div>
    <div id="activityfilters">
        <button id="showexisting"></button>
        <button id="showold"></button>
    </div>
    <div id="activitylist"></div>
  </tiles:put>

</tiles:insert>