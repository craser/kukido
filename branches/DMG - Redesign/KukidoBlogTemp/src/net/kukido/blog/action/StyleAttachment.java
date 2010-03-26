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
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

/**
 * @author  craser
 * @version
 */
public class StyleAttachment extends ViewAttachment
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	OutputStream resOut = null;
	try
	{
	    String fileName = req.getParameter("fileName");
            String xsltTemplate = req.getParameter("xsl");
            
	    Attachment attachment = new AttachmentDao().findByFileName(fileName);            
            String templatePath = getServlet().getServletContext().getRealPath(xsltTemplate);
            Source xslt = new StreamSource(new FileInputStream(templatePath));
            Source xml = new StreamSource(new ByteArrayInputStream(attachment.getBytes()));
            
            
            
            //Result result = new StreamResult(res.getOutputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Result result = new StreamResult(out);

            Transformer transformer = TransformerFactory.newInstance().newTransformer(xslt);
            for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
                String param = (String)e.nextElement();
                String value = req.getParameter(param);
                transformer.setParameter(param, value);
            }
            transformer.transform(xml, result);
            
            try { res.getOutputStream().write(out.toByteArray()); }
            catch (Exception e) {
                res.getWriter().write(out.toString());
            }

            return null; // Do nothing else.
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
	finally
	{
	    try { resOut.flush(); } catch (Exception ignored) {}
	    try { resOut.close(); } catch (Exception ignored) {}
	}
    }

}
