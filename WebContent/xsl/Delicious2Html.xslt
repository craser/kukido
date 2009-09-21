<?xml version='1.0' encoding='UTF-8'?>
<!-- Style RSS so that it is human-readable. -->

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    >

<xsl:output omit-xml-declaration="yes" />


<xsl:template match="/posts">
  <div class="navitem">
    <span class="navtitle"><a href="http://del.icio.us/DeathBeforeDecaf">Recent Bookmarks</a></span>
    <ul class="navmenu">
    <xsl:apply-templates select="//post" />
    </ul>
  </div>
</xsl:template>

<xsl:template match="post">
  <li><a href="{@href}"><xsl:value-of select="@description" /></a>
  </li>
</xsl:template>

</xsl:stylesheet>

