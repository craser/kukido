<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet
   type="text/xsl"
   href="/home/feeds/rss2html.xslt"
   media="screen" ?>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page contentType="text/xml" %>
<jsp:useBean id="entries" type="java.util.Collection" scope="request"/>
<rss version="2.0">
  <channel>
    <title>DreadedMonkeyGod.net</title>
    <link>http://dreadedmonkeygod.net</link>
    <description>Chris Raser's Personal Blog</description>
    <image>
      <url>http://www.dreadedmonkeygod.net/home/img/rss-logo.jpg</url>
      <title>Chris Raser</title>
      <link>http://www.dreadedmonkeygod.net/</link>
    </image>
    <language>en-us</language>
    <generator>Not-Quite-Braindead JSP</generator>
    <ttl>40</ttl>
    <logic:iterate name="entries" id="entry" type="net.kukido.blog.datamodel.LogEntry">
    <item>
      <title><bean:write name="entry" property="title"/></title>
      <link>http://dreadedmonkeygod.net/home/archive/<bean:write name="entry" property="entryId"/></link>
      <description><![CDATA[
        <logic:present name="entry" property="imageFileName">
      	  <p style="text-align: center">
      	    <dmg:image size="postcards" fileName="<%= entry.getImageFileName() %>" absolute="true" />
          </p>
        </logic:present>
        <bean:write name="entry" property="intro" filter="false"/>
        <logic:present name="entry" property="body">
          <logic:notEqual name="entry" property="body" value="">
            <a href="http://dreadedmonkeygod.net/home/archive/<%= entry.getEntryId() %>">read entire post &raquo;</a>
          </logic:notEqual>
        </logic:present>
      ]]></description>
      <pubDate><bean:write name="entry" property="datePosted" format="EEE, d MMM yyyy HH:mm:ss zzz"/></pubDate>
    </item>
    </logic:iterate>     
  </channel>
</rss>
