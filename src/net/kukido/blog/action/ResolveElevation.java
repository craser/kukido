package net.kukido.blog.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.kukido.blog.dataaccess.AttachmentDao;
import net.kukido.blog.datamodel.Attachment;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxFormatter;
import net.kukido.maps.GpxParser;
import net.kukido.maps.google.ElevationResolver;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
 

public class ResolveElevation extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
    		throws ServletException, IOException
	{
    	try {
	    	String fileName = req.getParameter("file");
	    	AttachmentDao dao = new AttachmentDao();
	    	Attachment gpx = dao.findByFileName(fileName);
	    	dao.populateBytes(gpx);
	    	GpsTrack track = new GpxParser().parse(gpx.getBytes()).get(0); // Hackety hack.
	    	track = new ElevationResolver().resolve(track);
	    	
	    	gpx.setBytes(formatGpx(track));

	    	// Save the resolved version of the file as a new attachment.
	    	gpx.setFileName("RESOLVED-" + gpx.getFileName());
	    	dao.create(gpx);
	    	
	    	return mapping.findForward("success");
    	}
    	catch (Exception e) {
    		throw new ServletException(e);
    	}
	}
    
    private byte[] formatGpx(GpsTrack track) throws IOException
    {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	List<GpsTrack> tracks = new ArrayList<GpsTrack>(1);
    	tracks.add(track);
    	new GpxFormatter().format(tracks, out);
    	
    	return out.toByteArray();
    }

}
