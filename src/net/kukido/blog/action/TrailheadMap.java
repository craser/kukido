/*
 * MophoUpload.java
 *
 * Created on October 16, 2003, 7:00 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.util.*;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import net.kukido.maps.*;

/**
 * @author  craser
 * @version
 */
public class TrailheadMap extends ViewAttachment
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	try
	{
	    GeotagDao geotagDao = new GeotagDao();
            AttachmentDao attachmentDao = new AttachmentDao();
            Collection geotags = geotagDao.findByFileType(Attachment.TYPE_MAP);
            Collection maps = new LinkedList();
            for (Iterator i = geotags.iterator(); i.hasNext(); ) {
                Geotag geotag = (Geotag)i.next();
                Attachment map = attachmentDao.findByAttachmentId(geotag.getAttachmentId());
                maps.add(map);
            }
            
            GpsBounds bounds = findBounds(geotags);
            req.setAttribute("maps", maps);
            req.setAttribute("bounds", bounds);
            return mapping.findForward("success");
	}
        catch (DataAccessException e)
        {
            e.printStackTrace();
            res.sendError(res.SC_NOT_FOUND);
            return null; // Do nothing else.
        }
	catch (Exception e)
	{
	    throw new ServletException(e);
	}
    }
    
    /**
     * @param geotags a Collection of Geotags
     */
    public GpsBounds findBounds(Collection geotags)
    {
        Iterator i = geotags.iterator();
        Geotag l = (Geotag)i.next();
        float maxLat = l.getLatitude();
        float minLat = maxLat;
        float maxLon = l.getLongitude();
        float minLon = maxLon;
        
        while (i.hasNext()) {
            l = (Geotag)i.next();
            maxLat = Math.max(maxLat, l.getLatitude());
            minLat = Math.min(minLat, l.getLatitude());
            maxLon = Math.max(maxLon, l.getLongitude());
            minLon = Math.min(minLon, l.getLongitude());
        }
        
        GpsBounds bounds = new GpsBounds(minLat, maxLat, minLon, maxLon);
        return bounds;        
    }

}
