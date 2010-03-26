/*
 * CreateComment.java
 *
 * Created on February 21, 2006, 10:02 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.config.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import net.sf.akismet.Akismet;

public class UnmarkCommentSpam extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            int commentId = Integer.parseInt(req.getParameter("commentId"));
            CommentDao commentDao = new CommentDao();
            Comment comment = commentDao.findByCommentId(commentId);
            comment.setIsSpam(false);
            commentDao.update(comment);
            
            submitHam(req, comment);
            
            ActionForward fw = mapping.findForward("success");
            String newPath = fw.getPath() + comment.getEntryId();
            ActionForward newFw = new ActionForward(fw);
            newFw.setPath(newPath);
            return newFw;
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    /**
     * Sumbmits this comment to the spamtrab service (at the moment, Akismet)
     * and let's them know it was WRONGLY tagged as spam.
     */
    private void submitHam(HttpServletRequest req, Comment comment)
        throws MalformedURLException
    {
        DmgConfig config = new DmgConfig();
        String apiKey = config.getProperty("spam.akismet.apikey");
        Akismet ak = new Akismet(apiKey, "http://www.dreadedmonkeygod.net");
        String permalink = getPermalink(req, comment);        
        ak.submitHam(comment.getIpAddress()
                ,comment.getUserAgent()
                ,comment.getReferrer()
                ,permalink
                ,"comment"
                ,comment.getUserName()
                ,comment.getUserEmail()
                ,comment.getUserUrl()
                ,comment.getComment()
                ,null);      
    }    
    
    /**
     * @return the base url of this page, including the trailing slash.
     **/
    private String getPermalink(HttpServletRequest req, Comment comment)
	throws MalformedURLException
    {
	URL reqUrl = new URL(req.getRequestURL().toString());
	String protocol = reqUrl.getProtocol();
	int port = reqUrl.getPort();
	String host = reqUrl.getHost();
	String webContext = getServlet().getServletContext().getServletContextName();
            

	StringBuffer baseUrl = new StringBuffer();
	baseUrl.append(protocol);
	baseUrl.append("://");
	baseUrl.append(host);
	if (port >= 0) {
	    baseUrl.append(":");
	    baseUrl.append(port);
	}
	baseUrl.append(webContext.startsWith("/") ? "" : "/");
	baseUrl.append(webContext);
	baseUrl.append("/");
        baseUrl.append("archive/");
        baseUrl.append(comment.getEntryId());

	return baseUrl.toString();
    }
    
    private boolean isValid(CommentForm commentForm, ActionMapping mapping, HttpServletRequest req)
    {
        ActionErrors errors = commentForm.validate(mapping, req);
        return (null != errors && !errors.isEmpty());
    }
}
