package net.kukido.blog.action;

import org.apache.struts.action.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

public class ViewLogEntry extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            int entryId = Integer.parseInt(req.getParameter("entryId"));
            LogDao logDao = new LogDao();
	    LogEntry entry = logDao.findByEntryId(entryId);            
	    req.setAttribute("entry", entry);
            req.setAttribute("nextEntryId", new Integer(logDao.findEntryAfter(entry.getEntryId())));
            req.setAttribute("prevEntryId", new Integer(logDao.findEntryBefore(entry.getEntryId())));
            
            return mapping.findForward("success");
        }
        catch (DataAccessException e)
        {
            e.printStackTrace();
            res.sendError(res.SC_NOT_FOUND);
            return null; // Do nothing else.
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
}
