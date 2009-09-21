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
import javax.imageio.ImageIO;

/**
 * @author  craser
 * @version
 */
public class Error extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	throw new ServletException("Testing");
    }
}
