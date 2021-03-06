<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="java.util.Collection" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>DreadedMonkeyGod.net</title>
    <dmg:BaseHrefTag />
    <link rel="search" type="application/opensearchdescription+xml" title="DMG" href="dmg-opensearch-descriptor.xml"/>
    <link rel="stylesheet" type="text/css" href="css/main.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="css/frontpage.css" />
    <link rel="stylesheet" type="text/css" href="css/lightbox.css"/>
    <link rel="alternate" type="application/rss+xml" href="monkeyfeed.xml" title="RSS feed for dreadedmonkeygod.net"/>
    <tiles:get name="analytics" />
    <script src="javascript/jquery-1.11.3.js"></script>
    <!-- page-specific header information -->
    <tiles:get name="head" />
    <!-- end of page-specific header information -->
  </head>
  <body>
    <h1 id="titlebanner"><html:link page="/">dreadedmonkeygod . net</html:link></h1>
    <div id="headerdiv" class="header">
      <ul class="sitenav">
        <li class="trailmaps-link"><html:link href="trailmaps">trail maps</html:link></li>
        <li class="twitter-link"><html:link href="https://twitter.com/DeathB4Decaf">@DeathB4Decaf</html:link></li>
        <li class="rss-link"><html:link href="monkeyfeed.xml"><img src="img/feed-icon.gif" /> DMG</html:link></li>
        <jsp:include page="/jsp/nav/UserNav.jsp" /><%-- includes admin elements --%>
    	<li class="search-form">
    		<html:form action="SearchLogEntries" method="GET">
            	<html:text property="searchTerm" size="10" value="Search" onfocus="this.value=''" />
            	<input type="hidden" name="pageSize" value="30" />
            	<html:submit value="GO" />
    		</html:form>
    	</li>
      </ul>
    </div>
    <div class="sidebar">
      <!-- sidebar -->
      <tiles:get name="sidebar" />
      <!-- end of sidebar -->
    </div>
    <div class="content">
      <!-- main content area -->
      <tiles:get name="content" ignore="false" />
      <!-- end main content area -->
    </div>
    <script src="javascript/lightbox.js"></script>
  </body>
</html>
<% out.flush(); // Dust off and nuke 'em from orbit.  It's the only way to be sure. %>