/*
 * GpxHandler.java
 *
 * Created on October 24, 2007, 9:59 PM
 */

package net.kukido.maps;

import java.util.*;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.text.*;

/**
 * @author  craser
 */
 public class GpxParser extends DefaultHandler
{
    static private final SimpleDateFormat gpxDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sszz");
    
    private GpsTrack track;
    private GpsLocation trackPoint;
    private Stack<String> state;
    private StringBuffer chars;
    private List<GpsTrack> tracks;
    
    /** Creates a new instance of GpxHandler */
    public GpxParser() {
        state = new Stack<String>();
        chars = new StringBuffer();
        tracks = new ArrayList<GpsTrack>();
    }
    
    public List<GpsTrack> parse(byte[] bytes)
        throws SAXException, IOException
    {
        InputStream in = null;
        try 
        {
            in = new ByteArrayInputStream(bytes);
            return parse(in);
        }
        finally
        {
            try { in.close(); } catch (Exception ignored) {}
        }
    }
    
    public List<GpsTrack> parse(InputStream in)
    	throws SAXException, IOException
    {
        //XMLReader reader = XMLReaderFactory.createXMLReader();
        // FIXME: This should be soft-coded using a property value
        // to set the class name of the appropriate factory, etc.
    	XMLReader reader = new org.apache.xerces.parsers.SAXParser();
    	reader.setContentHandler(this);
    	reader.parse(new InputSource(in));
    	
    	return tracks;
    }
    
    public void startPrefixMapping(String prefix, String uri)
    {
        //System.out.println("Mapping prefix: " + prefix + " => " + uri);
    }
    
    public void error(SAXParseException e)
    {
        System.err.println("'Recoverable' error parsing GPX document: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    
    public void fatalError(SAXParseException e)
    {
        System.err.println("Fatal error parsing GPX document: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    
    public void warning(SAXParseException e)
    {
        System.err.println("Warning while parsing GPX document: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    
    public void startDocument() 
        throws SAXException
    {
        //System.out.println("GpxParser: Starting Document");
    }
    
    public void startElement(String namespaceUrl, String locale, String name, Attributes atts)
        throws SAXException
    {
        //System.out.println("startElement(\"" + name + "\")");
        state.push(name);
        if ("trk".equals(name)) {
            track = new GpsTrack();
        }
        else if ("trkpt".equals(name)) {
            trackPoint = new GpsLocation();
            float lat = Float.parseFloat(atts.getValue("lat"));
            float lon = Float.parseFloat(atts.getValue("lon"));
            trackPoint.setLatitude(lat);
            trackPoint.setLongitude(lon);
        }
    }
    
    public void endElement(String namespaceURI, String localName, String name)
        throws SAXException
    {
        String currentState = (String)state.pop();
        
        if (!currentState.equals(name)) {
            throw new IllegalStateException("ERG!  Expected end of \"" + state.peek() + "\","
                + " found \"" + name + "\"");
        }
        
        
        String val = chars.toString().trim();
        chars = new StringBuffer();
        
        if ("trkpt".equals(currentState)) {
            trackPoint.setGrade(calculateGrade(track, trackPoint));
            track.add(trackPoint);
            trackPoint = null; // Fail Fast
        }
        else if ("ele".equals(currentState)) {
            float elevation = Float.parseFloat(val);
            if (trackPoint != null) trackPoint.setElevation(elevation);
        }
        else if ("time".equals(currentState)) {
            try {
                Date timestamp = parseDateString(val);
                if (trackPoint != null) trackPoint.setTimestamp(timestamp);
            }
            catch (ParseException e) {
                throw new SAXException("Unable to parse timestamp: \"" + val + "\"");
            }
        }  
        else if ("name".equals(currentState)) {
        	track.setName(val);
        }
        else if ("trk".equals(currentState)) {
        	tracks.add(track);
        	track = null;
        }
    }
    
    private Date parseDateString(String dateString)
        throws ParseException
    {
        //System.out.println("GPX Time   : " + dateString);
        dateString = dateString.replace('T', ' '); // I feel dirty.
        dateString = dateString.substring(0, dateString.lastIndexOf('Z'));
        dateString += "GMT";
        Date timestamp = gpxDateFormat.parse(dateString);
        //System.out.println("Parsed Date: " + gpxDateFormat.format(timestamp));
        
        return timestamp;        
    }
    
    /**
     * Calculates the average grade between the last point in the track
     * and the given point.
     * @param track
     * @param trackPoint
     * @return
     */
    private float calculateGrade(GpsTrack track, GpsLocation trackPoint)
    {
        if (track.isEmpty()) {
            return 0f;
        }
        else {
            GpsLocation a = track.get(track.size() - 1);
            double rise = a.getElevation() - trackPoint.getElevation();
            double run = a.getMetersTo(trackPoint);
            float grade = new Float((rise / run) * 100d);
            return grade;
        }
    }
    
    public void characters(char[] buff, int start, int len)
        throws SAXException
    {
        chars.append(buff, start, len);      
    }
}
