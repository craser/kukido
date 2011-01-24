/*
 * CalculateGeotags.java
 *
 * Created on October 23, 2007, 9:29 PM
 */

package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.xml.sax.SAXException;
import net.kukido.maps.*;

/**
 *
 * @author  craser
 */
public class CalculateGeotags extends Action
{
    static private final String TRACKPOINT_TAG_NAME = "trkpt";
    static private final String LATITUDE_ATTR_NAME = "lat";
    static private final String LONGITUDE_ATTR_NAME = "lon";
    static private final String ELEVATION_TAG_NAME = "ele";
    static private final String TIME_TAG_NAME = "time";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
	try
	{
            BatchGeotagForm geoForm = (BatchGeotagForm)form;

            LogDao logDao = new LogDao();
            AttachmentDao attachmentDao = new AttachmentDao();
            GeotagDao geotagDao = new GeotagDao();
            
            LogEntry entry = logDao.findByEntryId(geoForm.getEntryId());
	    Attachment map = attachmentDao.findByAttachmentId(geoForm.getMapId());
            GpsTrack track = parseMap(map);
            
            for (Iterator attachments = entry.getAttachments().iterator(); attachments.hasNext(); ) 
            {
                Attachment a = (Attachment)attachments.next();
                if (geoForm.getReplaceExistingTags()) {
                    geotagDao.deleteByAttachmentId(a.getAttachmentId());
                }
                
                if (a.getIsGalleryImage())
                {
                    System.out.println("Geotagging image \"" + a.getFileName() + "\"");
                    Date timestamp = getCorrectedTimestamp(a, geoForm.getTimeOffset());
                    GpsLocation location = track.getLocationByTimestamp(timestamp);

                    Geotag geotag = new Geotag(location);
                    geotag.setAttachmentId(a.getAttachmentId());
                    geotag.setMapId(geoForm.getMapId());
                    geotagDao.create(geotag);      
                }
                else if (a.getIsMap())
                {
                    System.out.println("Geotagging map \"" + a.getFileName() + "\"");
                    GpsTrack t = parseMap(a);
                    GpsLocation location = t.getCenter();
                    location.setTimestamp(t.getStartTime());
                    
                    Geotag geotag = new Geotag(location);
                    geotag.setAttachmentId(a.getAttachmentId());
                    geotag.setMapId(a.getAttachmentId());
                    geotagDao.create(geotag);
                }
            }

            return mapping.findForward("success");
	}
	catch (Exception e)
	{
            e.printStackTrace(System.err);
	    throw new ServletException(e);
	}
    }
    
    private GpsLocation getCenter(List<GpsTrack> tracks)
    {
    	GpsBounds bounds = tracks.get(0).getBounds();
    	for (GpsTrack track : tracks) {
    		bounds = bounds.expand(track.getBounds());
    	}
    	
    	return bounds.getCenter();
    }
    
    private GpsTrack parseMap(Attachment map)
        throws DataAccessException, SAXException, IOException
    {
        new AttachmentDao().populateBytes(map);
        GpxParser gpxParser = new GpxParser();
        byte[] bytes = map.getBytes();
        InputStream in = new ByteArrayInputStream(bytes);
        List<GpsTrack> tracks = gpxParser.parse(in);
        
        return tracks.get(0);
    }
    
    
    private Date getCorrectedTimestamp(Attachment attachment, int timeOffset)
    {
        // NO! NO! NO! BAD AND WRONG!  This drops all TIME ZONE information!
        //long timestamp = attachment.getDateTaken().getTime();
        //timestamp += (timeOffset * 1000); // timeOffset is in SECONDS
        //return new Date(timestamp);
        
        // Maybe this won't suck.
        Date timestamp = attachment.getDateTaken();
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(cal.SECOND, timeOffset);
        Date adjustedTimestamp = cal.getTime();
        
        return adjustedTimestamp;
    }
}
