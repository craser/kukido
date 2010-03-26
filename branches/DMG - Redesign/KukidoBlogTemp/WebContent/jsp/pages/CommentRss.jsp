<%@ page contentType="text/xml" %><?xml version="1.0" encoding="UTF-8"?>

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean id="comments" type="java.util.Collection" scope="request"/>
<rss version="2.0">
  <channel>
    <logic:present name="entry" scope="request">
      <title>DMG: Comments on "<bean:write name="entry" property="title" />"</title>
      <link>http://www.dreadedmonkeygod.net/home/archives/<bean:write name="entry" property="entryId" />#comments</link>
      <description>Reader-Submitted Comments to DMG</description>
      <image>
        <url>http://www.dreadedmonkeygod.net/home/img/rss_logo_badhairday.jpg</url>
        <title>Chris Raser's Bad Hair Day</title>
        <link>http://www.dreadedmonkeygod.net/home/archives/<bean:write name="entry" property="entryId" />#comments</link>
      </image>   
    </logic:present>
    <logic:notPresent name="entry" scope="request">
      <title>DreadedMonkeyGod: Comments</title>
      <link>http://dreadedmonkeygod.net</link>
      <description>Reader-Submitted Comments to DMG</description>
      <image>
        <url>http://www.dreadedmonkeygod.net/home/img/rss_logo_badhairday.jpg</url>
        <title>Chris Raser's Bad Hair Day</title>
        <link>http://www.dreadedmonkeygod.net/</link>
      </image>
    </logic:notPresent>
    <language>en-us</language>
    <generator>Not-Quite-Braindead JSP</generator>
    <ttl>40</ttl>
    <nested:iterate name="comments" id="comment" type="net.kukido.blog.datamodel.Comment">
      <item>
        <title><bean:write name="comment" property="userName"/></title>
        <link>http://dreadedmonkeygod.net/home/archive/<bean:write name="comment" property="entryId"/>#<bean:write name="comment" property="commentId" /></link>
        <description><![CDATA[
          <bean:write name="comment" property="commentHtml" />
        ]]></description>
        <pubDate><bean:write name="comment" property="datePosted" format="EEE, d MMM yyyy HH:mm:ss zzz"/></pubDate>
      </item>
    </nested:iterate>
  </channel>
</rss>
