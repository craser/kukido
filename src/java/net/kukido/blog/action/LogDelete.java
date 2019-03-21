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
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author  craser
 */
public class LogDelete extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
        	int entryId = Integer.parseInt(req.getParameter("entryId"));
            
            LogDao logDao = new LogDao();
            AttachmentDao attachmentDao = new AttachmentDao();
            
            // Clean up all the attachments first.
            LogEntry entry = logDao.findByEntryId(entryId);            
            attachmentDao.deleteByEntryId(entryId);
            
            // Once all the attachments have been deleted, then delete the Entry.
            logDao.delete(entryId);
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }    
}
