package net.kukido.blog.action;

import org.apache.struts.action.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class Gallery extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
	    int attachmentId = Integer.parseInt(req.getParameter("attachmentId"));
	    Attachment galleryImage = new AttachmentDao().findByAttachmentId(attachmentId); // Find the info for this attachment. 
            LogEntry entry = new LogDao().findByEntryId(galleryImage.getEntryId()); // Find the relavent log entry.
            Collection galleryImages = new AttachmentDao().findByEntryId(entry.getEntryId()); // Find the other attachments to that log entry.
            for (Iterator i = galleryImages.iterator(); i.hasNext(); )
            {
                Attachment a = (Attachment)i.next();
                if (!a.getIsGalleryImage()) { i.remove(); }
            }
	    req.setAttribute("galleryImage", galleryImage);
            req.setAttribute("entry", entry);
            req.setAttribute("galleryImages", galleryImages);
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
}
