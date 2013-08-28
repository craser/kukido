/*
 * LogDelete.java
 *
 * Created on July 31, 2004, 12:00 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 * 
 * @author craser
 */
public class DeleteAttachment extends Action 
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try {
			AttachmentDao attachmentDao = new AttachmentDao();
			LogDao logDao = new LogDao();
			
			int attachmentId = Integer.parseInt(req.getParameter("attachmentId"));
			Attachment attachment = attachmentDao.findByAttachmentId(attachmentId);
			LogEntry entry = logDao.findByEntryId(attachment.getEntryId());
			if (attachment.getFileName().equals(entry.getImageFileName())) {
				entry.setImageFileName(null);
				logDao.update(entry);
			}
			attachmentDao.delete(attachmentId);
			return mapping.findForward("success");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
