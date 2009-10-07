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

/**
 * @author  craser
 * @version
 */
public class MophoUpload extends Action
{
    static private final String SAVE_DIRECTORY  = "/mopho";
    static private final String SMALL_DIRECTORY = "small";
    static private final int    SMALL_WIDTH     = 140; 

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	try
	{
            MophoUploadForm uploadForm = (MophoUploadForm)form;
	    User user = (User)req.getSession().getAttribute("user");
            MophoEntry entry = new MophoEntry();
            entry.setDatePosted(new Date());
            entry.setTitle(uploadForm.getTitle());
            entry.setUserId(user.getUserId());
            entry.setUserName(user.getUserName());
            entry.setFileName(uploadForm.getImage().getFileName());
            entry.setBytes(uploadForm.getImage().getFileData());
            
            MophoDao mophoDao = new MophoDao();
            mophoDao.create(entry);
            
            return mapping.findForward("success");
	}
	catch (Exception e)
	{
	    throw new ServletException(e);
	}
    }
}
