package net.kukido.blog.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.kukido.blog.dataaccess.AttachmentDao;
import net.kukido.blog.dataaccess.LogDao;
import net.kukido.blog.datamodel.Attachment;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxParser;

import org.apache.struts.action.*;

public class CyclingStats extends Action
{

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
		try
		{
            AttachmentDao attachmentDao = new AttachmentDao();
            LogDao logDao = new LogDao();
            
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH);
            
            // Get the info necessary for year-to-date miles, month-to-date miles, average speed, # of rides, 
            float ytdMiles = 0;
            float mtdMiles = 0;
            
            GpxParser gpxParser = new GpxParser();
            Collection maps = attachmentDao.findMapsByYear(year);
            for (Iterator i = maps.iterator(); i.hasNext();) {
            	Attachment map = (Attachment)i.next();
            	attachmentDao.populateBytes(map);
            	List<GpsTrack> tracks = gpxParser.parse(map.getBytes());
            	GpsTrack track = tracks.get(0);
            	
            	ytdMiles += track.getMiles();
            	now.setTime(track.getStartTime()); // Abusing left-over Calendar object
            	if (now.get(Calendar.MONTH) == month) {
            		mtdMiles += track.getMiles();
            	}
            }
            
            req.setAttribute("ytdMiles", new Float(ytdMiles));
            req.setAttribute("mtdMiles", new Float(mtdMiles));
            
            return mapping.findForward("success");
		}
		catch (Exception e)
		{
	            e.printStackTrace(System.err);
		    throw new ServletException(e);
		}
    }    
}

