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
 * @author craser
 */
public class LogEdit extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException
    {
        try {
            LogEditForm updateForm = (LogEditForm) form;
            LogEntry entry = updateForm.getEntry();
            
            // This is kind of ugly, but it'll fix the bug for now.
            if (entry.getImageFileName() != null && !"".equals(entry.getImageFileName())) {
                AttachmentDao attachmentDao = new AttachmentDao();
                Attachment thumb = attachmentDao.findByFileName(entry.getImageFileName());
                entry.setImageFileType(thumb.getFileType());
            }

            LogDao logDao = new LogDao();
            logDao.update(entry);
            
            req.setAttribute("entry", entry);
            Collection attachments = new AttachmentDao().findByEntryId(entry.getEntryId());
            req.setAttribute("attachments", attachments);

            return mapping.findForward("success");
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
