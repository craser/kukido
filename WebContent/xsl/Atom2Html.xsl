<?xml version='1.0' encoding='UTF-8'?>
<!-- Style RSS so that it is human-readable. -->

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:atom="http://www.w3.org/2005/Atom"
    version="1.0"
    >

<xsl:output omit-xml-declaration="yes" />


<xsl:template match="//atom:feed">
  <div class="navitem">
    <span class="navtitle">
      <xsl:element name="a">
        <xsl:attribute name="href"><xsl:value-of select="atom:link[@rel='self']/@href" /></xsl:attribute>
        Recommended Reading
      </xsl:element>
	</span>
    <ul class="navmenu">
    <xsl:apply-templates select="//atom:entry" />
    </ul>
  </div>
</xsl:template>
 
<xsl:template match="atom:entry">
  <li>
  	<xsl:element name="a">
  	  <xsl:attribute name="href"><xsl:apply-templates select="atom:link" /></xsl:attribute>
  	  <xsl:value-of select="atom:title" disable-output-escaping="yes" />
  	</xsl:element>
  </li>
</xsl:template>

<xsl:template match="atom:link">
  <xsl:value-of select="@href" />
</xsl:template>

</xsl:stylesheet>

