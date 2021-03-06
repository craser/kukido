<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="java.util.Collection" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
    <title>DreadedMonkeyGod.net: <tiles:getAsString name="title" /></title>
    <dmg:BaseHrefTag />
    <link rel="search" type="application/opensearchdescription+xml" title="DMG" href="dmg-opensearch-descriptor.xml"/>
    <link rel="stylesheet" type="text/css" href="css/main.css" media="screen"/>
    <link rel="alternate" type="application/rss+xml" href="monkeyfeed.xml" title="RSS feed for dreadedmonkeygod.net"/>
    <tiles:get name="analytics" />
    <script src="javascript/jquery-1.11.3.js"></script>
    <!-- page-specific header information -->
    <tiles:get name="head" />
    <!-- end of page-specific header information -->
    <link rel="stylesheet" type="text/css" href="css/lightbox.css"/>
  </head>
  <body>
    <h1 id="titlebanner"><html:link page="/">dreadedmonkeygod . net</html:link></h1>
    <div class="header">
      <ul class="sitenav">
        <li><html:link href="trailmaps">trail maps</html:link></li>
        <li><html:link href="mailto:chris@dreadedmonkeygod.net">contact</html:link></li>
        <li><html:link href="about">about the author</html:link></li>
        <li><html:link href="https://twitter.com/DeathB4Decaf">@DeathB4Decaf</html:link></li>
        <li><html:link href="monkeyfeed.xml"><img src="img/feed-icon.gif" /> DMG</html:link></li>
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
    <div class="footer">
      <!-- footer -->
      <tiles:get name="footer" />
      <!-- footer -->
    </div>
    <script src="javascript/lightbox.js"></script>
  </body>
</html>
<% out.flush(); // Dust off and nuke 'em from orbit.  It's the only way to be sure. %>