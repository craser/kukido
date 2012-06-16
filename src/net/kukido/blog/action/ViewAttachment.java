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
 * @author craser
 * @version
 */
public class ViewAttachment extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException
    {
        OutputStream resOut = null;
        try {
            if (!validReferrer(req)) {
                return mapping.findForward("forbidden");
            }
                    
            String fileName = req.getParameter("fileName");
            AttachmentDao attachmentDao = new AttachmentDao();
            Attachment attachment = attachmentDao.findByFileName(fileName);
            attachmentDao.populateBytes(attachment);
            String mimeType = getServlet().getServletContext()
                    .getMimeType(attachment.getFileName());
            if (mimeType == null)
                mimeType = attachment.getMimeType(); // Last-ditch effort...
            res.setContentType(mimeType);
            resOut = res.getOutputStream();
            resOut.write(attachment.getBytes());

            return null; // Do nothing else.
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            res.sendError(res.SC_NOT_FOUND);
            return null; // Do nothing else.
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
        finally {
            try {
                resOut.flush();
            }
            catch (Exception ignored) {
            }
            try {
                resOut.close();
            }
            catch (Exception ignored) {
            }
        }
    }
    
    private boolean validReferrer(HttpServletRequest req) {

        String referer = req.getHeader("referer");
        String referrer = req.getHeader("referrer");
        referer = (referer == null) ? "" : referer;
        referrer = (referrer == null) ? "" : referrer;
        
        String[] validReferrers = new String[] { "dreadedmonkeygod.net", "google.com", "localhost" };
        for (String ref : validReferrers) {
            if (referer.contains(ref) || referrer.contains(ref)) {
                return true;
            }
        }
        return false;
    }

}
