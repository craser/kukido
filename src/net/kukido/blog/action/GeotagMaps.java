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
public class GeotagMaps extends Action
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
	try
	{
            AttachmentDao attachmentDao = new AttachmentDao();
            GeotagDao geotagDao = new GeotagDao();
            
            Collection attachments = attachmentDao.findByFileType(Attachment.TYPE_MAP);
            for (Iterator i = attachments.iterator(); i.hasNext(); ) 
            {
                Attachment a = (Attachment)i.next();
                geotagDao.deleteByAttachmentId(a.getAttachmentId());
                
                if (a.getIsMap())
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
    
    private GpsTrack parseMap(Attachment map)
        throws DataAccessException, SAXException, IOException
    {
        new AttachmentDao().populateBytes(map);
        GpxParser gpxParser = new GpxParser();
        byte[] bytes = map.getBytes();
        InputStream in = new ByteArrayInputStream(bytes);
        GpsTrack track = gpxParser.parse(in);
        
        return track;
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
