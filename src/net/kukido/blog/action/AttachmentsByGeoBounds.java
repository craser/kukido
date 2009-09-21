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
public class AttachmentsByGeoBounds extends Action
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
	try
	{
            float minLat = Float.parseFloat(req.getParameter("minLat"));
            float maxLat = Float.parseFloat(req.getParameter("maxLat"));
            float minLon = Float.parseFloat(req.getParameter("minLon"));
            float maxLon = Float.parseFloat(req.getParameter("maxLon"));
            String fileType = req.getParameter("fileType");
            
            AttachmentDao attachmentDao = new AttachmentDao();
            GeotagDao geotagDao = new GeotagDao();
            
            Collection geotags = geotagDao.findByBounds(minLat, maxLat, minLon, maxLon);
            Collection attachments = new ArrayList();
            for (Iterator i = geotags.iterator(); i.hasNext(); ) {
                Geotag geotag = (Geotag)i.next();
                Attachment attachment = attachmentDao.findByAttachmentId(geotag.getAttachmentId());
                if (fileType == null || fileType.equals(attachment.getFileType())) {
                    attachments.add(attachment);
                }
            }
            
            req.setAttribute("attachments", attachments);
            req.setAttribute("geotags", geotags);

            return mapping.findForward("success");
	}
	catch (Exception e)
	{
            e.printStackTrace(System.err);
	    throw new ServletException(e);
	}
    }
}
