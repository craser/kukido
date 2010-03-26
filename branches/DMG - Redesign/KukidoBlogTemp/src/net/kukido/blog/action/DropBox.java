package net.kukido.blog.action;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import net.kukido.blog.forms.*;

public class DropBox extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
	String dropBoxPath = getServlet().getServletContext().getRealPath("/dropbox");
	File dropBox = new File(dropBoxPath);
	if (!dropBox.exists())
            dropBox.mkdir();
       
        String[] fileNames = dropBox.list();
        req.setAttribute("fileNames", fileNames);

	return mapping.findForward("success");	
    }
}
