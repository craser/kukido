/*
 * CreateComment.java
 *
 * Created on February 21, 2006, 10:02 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.log.Logging;
import net.kukido.blog.servlet.*;
import java.io.*;
import java.net.*;
import java.util.Collection;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import net.kukido.blog.config.*;
import net.sf.akismet.Akismet;


public class DeleteCommentSpam extends ViewCommentSpam
{
    private Logger log = Logging.getLogger(DeleteCommentSpam.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            CommentDao dao = new CommentDao();
            Akismet ak = getAkismet();
            Collection<Comment> spam = dao.findSpam();
            for (Comment comment : spam) {
                try {
                    submitSpam(req, ak, comment);
                    dao.delete(comment);
                }
                catch (Exception e) {
                    log.error("Unable to mark comment as spam.", e);
                }
            }
            return super.execute(mapping, form, req, res);
        }
        catch (Exception e)
        {
	        e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private Akismet getAkismet() {
        DmgConfig config = new DmgConfig();
        String apiKey = config.getProperty("spam.akismet.apikey");
        Akismet ak = new Akismet(apiKey, "http://www.dreadedmonkeygod.net");
        return ak;
    }

    /**
     * Sumbmits this comment to the spamtrap service (at the moment, Akismet)
     * and let's them know it was WRONGLY tagged as spam.
     */
    private void submitSpam(HttpServletRequest req, Akismet ak, Comment comment)
            throws MalformedURLException
    {
        String permalink = getPermalink(req, comment);
        ak.submitSpam(comment.getIpAddress()
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

}
