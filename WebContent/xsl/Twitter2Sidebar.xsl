<?xml version='1.0' encoding='UTF-8'?>

<!-- Style RSS so that it is human-readable. -->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    >
    
  <xsl:template match="/">
    <xsl:apply-templates select="//channel" />
  </xsl:template>

  <xsl:template match="channel">
    <!-- This relies on blogsidebar.css being properly linked from the including page. -->
    <div class="blogdigest">
      <h3 class="channeltitle"><a href="{link}"><img src="img/twitter-headshot.jpg" /> Chris on Twitter</a></h3>
      <xsl:apply-templates select="//item" />
    </div>
  </xsl:template>

  <xsl:template match="item">
    <xsl:variable name="charlim">500</xsl:variable>
    <div class="blogentry">
      <xsl:variable name="body">
        <xsl:call-template name="strip-tags">
          <xsl:with-param name="s"><xsl:value-of select="description" /></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="string-length($body) &lt;= $charlim">
          <xsl:value-of select="description" disable-output-escaping="yes" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="substring($body,1,$charlim)" disable-output-escaping="yes" />...
          <a href="{link}" class="morelink">...more</a>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:variable name="ts"><xsl:value-of select="pubDate" /></xsl:variable>
      <xsl:variable name="d"><xsl:value-of select="concat('20', substring($ts, 7,2), '-', substring($ts, 1,2), '-', substring($ts,4,2))" /></xsl:variable>
      <div class="blogentrymetadata"><a href="{link}"><xsl:value-of select="$ts" /></a></div>
      <div class="blogentrymetadata"><a href="{link}"><xsl:value-of select="$d" /></a></div>
    </div>
  </xsl:template>
  
  
  <xsl:template name="strip-tags">
    <xsl:param name="s" />
    <xsl:variable name="op">&lt;</xsl:variable>
    <xsl:variable name="cl">&gt;</xsl:variable>
    <xsl:choose>
      <xsl:when test="contains($s, $op)">
        <xsl:value-of select="substring-before($s, $op)" /><xsl:call-template name="strip-tags"><xsl:with-param name="s" select="substring-after($s, $cl)" /></xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$s" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>

