package net.kukido.blog.action;

import org.apache.struts.action.*;
import net.kukido.blog.dataaccess.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

/**
 * Provides an RSS feed of blog entries where I've checked syndicated=true. That feed
 * gets picked up by IFTTT and cross-posted to Facebook, Twitter, etc.
 */
public class ViewSyndicatedLogs extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            int numEntries = Integer.parseInt(req.getParameter("numEntries"));
            LogDao logDao = new LogDao();
            Collection entries = logDao.findSyndicated(numEntries);
            req.setAttribute("entries", entries);
            
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
