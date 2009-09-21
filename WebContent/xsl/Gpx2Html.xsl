<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:gpx="http://www.topografix.com/GPX/1/0"
    >
  <xsl:output
      indent="yes"
      method="html"
      omit-xml-declaration="yes"
      media-type="application/xhtml+xml"
      doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
      />
        
<xsl:template match="/">
  <xsl:apply-templates select="gpx:gpx" />
</xsl:template>

<xsl:template match="gpx:gpx">
  <html>
    <head>
      <title><xsl:value-of select="name()" /></title>
      <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAOggD5Fz3iK4oyqrD-5a3rxTFRfqDGOwfXAlOK-54sJyR4NNS5RRcymeccR_BOTGOd_RmVO8QutZgJg" type="text/javascript"></script>
     <script type="text/javascript">

      function buildWayPoint(point, name, desc, elevation)
      {
          var marker = new GMarker(point);
          GEvent.addListener(marker, "click", function() {
              marker.openInfoWindowHtml("&lt;b&gt;" + name + "&lt;/b&gt;&lt;br&gt;" + desc + "&lt;br&gt;");
          });
          return marker;
      }

      function showMap()
      {
          if (GBrowserIsCompatible()) {
              var div = document.getElementById("map");
  	      var map = new GMap2(div);
              map.addControl(new GSmallMapControl());
              map.addControl(new GMapTypeControl());
              // applying trkpt center:
              <xsl:apply-templates select="//gpx:trkpt[1]" mode="center"/>
              // applying bounds center:
              <xsl:apply-templates select="//gpx:bounds" />
              // done applying centers.
              
              <xsl:for-each select="//gpx:trkseg">
                var points = [];
                <xsl:for-each select="gpx:trkpt">
                  points.push(new GLatLng(<xsl:value-of select="@lat" />,<xsl:value-of select="@lon" />));
                </xsl:for-each>
                map.addOverlay(new GPolyline(points));
              </xsl:for-each>

              var marker = 0;
              <xsl:for-each select="//gpx:wpt">
              map.addOverlay(buildWayPoint(new GLatLng(<xsl:value-of select="@lat" />,<xsl:value-of select="@lon" />)
                                           ,'<xsl:value-of select="gpx:name" />'
                                           ,'<xsl:value-of select="gpx:desc" />'
                                           ,'<xsl:value-of select="gpx:ele" />'));
              </xsl:for-each>

              GEvent.addListener(map, "click", function(marker, point) {
                  if (!marker) { map.closeInfoWindow(); }
              });
          }
      }
      </script>
    </head>
    <body onload="showMap();" onunload="GUnload()">
      The Map:
      <div id="map" style="border: 1px solid black; height: 600px;"></div>
    </body>
  </html>
</xsl:template>


<xsl:template match="gpx:trkpt" mode="center">
    map.setCenter(new GLatLng(<xsl:value-of select="@lat" />,<xsl:value-of select="@lon" />), 17);
</xsl:template>

<xsl:template match="gpx:trkpt">
  <tr>
    <td><xsl:value-of select="@lat" /></td>
    <td><xsl:value-of select="@lon" /></td>
  </tr>
</xsl:template>

<xsl:template match="gpx:bounds">
    map.setCenter(new GLatLng(<xsl:value-of select="string(@minlat + ((@maxlat - @minlat) div 2))" />
                              ,<xsl:value-of select="string(@minlon + ((@maxlon - @minlon) div 2))" />), 14);
</xsl:template>


<xsl:template match="gpx:trkpt">
  
</xsl:template>

</xsl:transform>