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
import net.kukido.maps.*;

/**
 *
 * @author  craser
 */
public class GpxGmap extends Action
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
	try
	{
            AttachmentDao attachmentDao = new AttachmentDao();
            LogDao logDao = new LogDao();
            
            Attachment map = attachmentDao.findByFileName(req.getParameter("fileName"));
            attachmentDao.populateBytes(map);
            GpxParser gpxParser = new GpxParser();
            GpsTrack track = gpxParser.parse(map.getBytes());
            LogEntry entry = logDao.findByEntryId(map.getEntryId());

            req.setAttribute("entry", entry);
            req.setAttribute("map", map);
            req.setAttribute("track", track);

            return mapping.findForward("success");
	}
	catch (Exception e)
	{
            e.printStackTrace(System.err);
	    throw new ServletException(e);
	}
    }    
}
