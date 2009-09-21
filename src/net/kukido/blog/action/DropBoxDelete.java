package net.kukido.blog.action;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import net.kukido.blog.forms.*;

public class DropBoxDelete extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
	String fileName = req.getParameter("fileName");
	String dropBoxPath = getServlet().getServletContext().getRealPath("/dropbox");
	File dropBox = new File(dropBoxPath);
	if (!dropBox.exists()) { throw new FileNotFoundException(dropBoxPath); }

	File file = new File(dropBox, fileName);

	file.delete();
        
        return mapping.findForward("success");
    }
}
