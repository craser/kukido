/*
 * LogEdit.java
 *
 * Created on February 25, 2004, 9:49 AM
 */

package net.kukido.blog.action;

import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import net.kukido.blog.util.ImageTools;

/**
 *
 * @author  craser
 */
public class LogEdit extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            LogEditForm updateForm = (LogEditForm)form;
            LogDao logDao = new LogDao();
	    LogEntry entry = updateForm.getEntry();
            logDao.update(entry);


	    AttachmentDao attachmentDao = new AttachmentDao();
            /*
	    for (Iterator as = entry.getAttachments().iterator(); as.hasNext(); )
	    {
		Attachment a = (Attachment)as.next();
                if (a.getIsGalleryImage() && (a.getDateTaken() == null)) {
                    ImageTools tools = new ImageTools();
                    Date dateTaken = tools.getDateTaken(a.getBytes());
                    a.setDateTaken(dateTaken);
                }
		attachmentDao.update(a);
	    }
            */

	    req.setAttribute("entry", entry);
	    Collection attachments = new AttachmentDao().findByEntryId(entry.getEntryId());
	    req.setAttribute("attachments", attachments);

            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }    
}
