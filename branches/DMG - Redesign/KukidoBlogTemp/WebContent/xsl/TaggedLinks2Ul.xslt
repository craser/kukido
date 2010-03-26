<?xml version='1.0' encoding='UTF-8'?>
<!-- Style RSS so that it is human-readable. -->

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    >

<xsl:output omit-xml-declaration="yes" />


<xsl:template match="/posts">
  <div class="blogrollfolder"><a href="http://del.icio.us/DeathBeforeDecaf/{@tag}"><xsl:value-of select="@tag" /></a></div>
  <ul class="navmenu">
    <xsl:if test="count(//post) > 0">
      <xsl:apply-templates select="//post" />
    </xsl:if>
    <xsl:if test="count(//post) = 0">
      <li> <a href="http://del.icio.us/DeathBeforeDecaf/{@tag}">no "<xsl:value-of select="@tag" />" links</a> </li>
    </xsl:if>
  </ul>
</xsl:template>

<xsl:template match="post">
  <li><a href="{@href}"><xsl:value-of select="@description" /></a>
  </li>
</xsl:template>

</xsl:stylesheet>

