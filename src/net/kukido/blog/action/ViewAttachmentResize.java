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

/**
 * @author  craser
 * @version
 */
public class ViewAttachmentResize extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	OutputStream resOut = null;
	try
	{
	    String fileName = req.getParameter("fileName");
	    int maxDimension = Integer.parseInt(req.getParameter("maxDimension"));

	    Thumbnail thumbnail = null;
	    try
	    {
                //System.out.println("[ViewAttachmentResize] Seeking attachment: \"" + fileName + "\"");
		thumbnail = new ThumbnailDao().findByFileNameMaxDimension(fileName, maxDimension);
	    }
	    catch (DataAccessException e)
	    {
                //System.out.println("[ViewAttachmentResize] creating resized version of \"" + fileName + "\"");
                AttachmentDao attachmentDao = new AttachmentDao();
		Attachment attachment = attachmentDao.findByFileName(fileName);
                attachmentDao.populateBytes(attachment);
		String format = fileName.substring(fileName.indexOf(".") + 1);
		ImageTools tools = new ImageTools();
		BufferedImage thumb = tools.createImage(attachment.getBytes());           
		thumb = tools.scaleToMaxDim(thumb, maxDimension);
                // This part we do if we can, but don't sweat it if we can't.
                try { 
                    int orientation = tools.getOrientation(attachment.getBytes());
                    //System.out.println("Orientation: " + orientation); // This just prints the orientation.
                    thumb = tools.fixOrientation(thumb, orientation); 
                }
                catch (Exception ignored) {}
                
		ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
		ImageIO.write(thumb, format, imageOut);

		thumbnail = new Thumbnail();
		thumbnail.setAttachmentId(attachment.getAttachmentId());
		thumbnail.setFileName(attachment.getFileName());
		thumbnail.setMaxDimension(maxDimension);
		thumbnail.setBytes(imageOut.toByteArray());

		new ThumbnailDao().create(thumbnail);
	    }


	    String mimeType = getServlet().getServletContext().getMimeType(thumbnail.getFileName());
	    res.setContentType(mimeType);
	    resOut = res.getOutputStream();
	    resOut.write(thumbnail.getBytes());

            return null; // Do nothing else.
	}
	catch (Exception e)
	{
	    System.out.println("PROBLEM CREATING NEW THUMBNAIL!");
	    e.printStackTrace(System.out);
	    throw new ServletException(e);
	}
	finally
	{
	    try { resOut.flush(); } catch (Exception ignored) {}
	    try { resOut.close(); } catch (Exception ignored) {}
	}
    }

}
