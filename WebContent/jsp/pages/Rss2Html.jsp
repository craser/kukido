<?xml version='1.0' encoding='UTF-8'?>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page contentType="text/xml" %>

<!-- Style RSS so that it is human-readable. -->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    >

<xsl:template match="/">
  <html>
    <head>
      <dmg:BaseHrefTag />
      <title><xsl:value-of select="//channel/title" /></title>
      <style>
        .title {
          font-size: 1.2em;
          font-weight: bold;
        }

        .title a {
          text-decoration: none;
        }

        .entrymetainfo {
          font-size: 0.8em;
          border-top: 1px solid #555;
          border-bottom: 1px solid #555;
          background-color: #eee;
        }

        .entrycontent {
          margin-bottom: 1em;
        }

        #rssexplanation {
	  border: 3px solid #555;
          background-color: #fffacd;
          padding: 1em;
          margin: 1em 0 1em 0;
        }

        #rssexplanation p {
          margin: 0 0 1em 0;
          
        }

        #titlebanner {
          font-size: 2.5em;
          font-weight: bold;
          font-family: sans-serif;
          letter-spacing: -7px;
          padding-bottom: -3px;
          margin: 0;
          border: 0;
          background-image: url(img/coffee_stain.jpg);
          background-position: top left;
          background-repeat: no-repeat;
          background-color: white;
          border: 1px solid black;
        }

        #titlebanner a {
          border: 0;
        }

        #titlebanner a {
          display: block;
          width: 600px;
          color: #234;
          padding-top: 2em;
          padding-left: 0.2em;
          text-decoration: none;
        }

        #titlebanner a:hover {
          color: orange;
        }

      </style>
    </head>
    <body>
      <div id="titlebanner"><html:link page="/">dreadedmonkeygod . net</html:link></div>      
      <div class="content">
        <div id="rssexplanation">
	   <p class="title">What's RSS?</p>

	   <p>This page it looks a little odd because it's really
           meant for other computers, not people.  It's a particular
           kind of web page called an "RSS Feed".  Sites like <a
           href="http://www.bloglines.com">Bloglines.com</a>, and a
           number of programs for your home machine are made just for
           reading RSS feeds.  They gather up information from a bunch
           of RSS feeds, and use the information to build sort of a
           custom website with just the articles you want to read.
           It's kind of like having a news ticker made just for you:
           when your favorite sites add something new, your "RSS
           Reader" will let you know.</p>

	   <p>Ned Batchelder has posted an excellent explanation of
	   what RSS is <a
	   href="http://www.nedbatchelder.com/site/whatisrss.html">here</a>,
	   if you're interested in learning more.</p>

           <p>If you want to read my site, the best place to start is <a
           href="http://dreadedmonkeygod.net">http://dreadedmonkeygod.net</a>,
           where all the regular for-people stuff is.</p>

        </div>
        <xsl:apply-templates select="//item" />
      </div>
    </body>
  </html>
</xsl:template>

<xsl:template match="item">
  <div class="entry">
    <div class="title"><a href="{link}"><xsl:value-of select="title" /></a></div>
    <div class="entrymetainfo">
    Posted <xsl:value-of select="pubDate" />
    </div>
    <div class="entrycontent">
      <xsl:value-of select="description" disable-output-escaping="yes" />
    </div>
  </div>
</xsl:template>

</xsl:stylesheet>

