/*
 * CreateComment.java
 *
 * Created on February 21, 2006, 10:02 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.servlet.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import net.kukido.blog.config.*;
import net.sf.akismet.Akismet;

public class CreateComment extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException
    {
        try {
            ActionForward spam = mapping.findForward("forbidden");
            return spam; 
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    public ActionForward execute_DISABLED(ActionMapping mapping, ActionForm form, HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException
    {
        try {
            CommentForm commentForm = (CommentForm) form;
            Comment comment = commentForm.getComment();
            comment.setIsSpam(isSpam(req, comment));
            
            if (comment.isSpam()) {
                ActionForward spam = mapping.findForward("spam");
                return spam; // Short circuit.
            }
            
            ActionErrors errors = commentForm.validate(mapping, req);
            saveErrors(req, errors);

            LogEntry entry = new LogDao().findByEntryId(comment.getEntryId());
            if (entry.getAllowComments() && errors.isEmpty()) {
                new CommentDao().create(comment);
            }

            req.setAttribute("comment", comment);
            req.setAttribute("entry", entry);

            if (commentForm.getRemember()) {
                CommentCookie c = new CommentCookie(comment.getUserName(), comment.getUserEmail(),
                        comment.getUserUrl());
                c.setMaxAge(60 * 60 * 24 * 21); // Persist for three weeks.
                res.addCookie(c);
            }

            ActionForward forward = null;
            if (errors.isEmpty()) {
                ActionForward oldFw = mapping.findForward("success");
                String newPath = oldFw.getPath() + comment.getEntryId();
                ActionForward newFw = new ActionForward(oldFw);
                newFw.setPath(newPath);
                forward = newFw;
            }
            else {
                ActionForward oldFw = mapping.findForward("error");
                String newPath = oldFw.getPath() + comment.getEntryId();
                ActionForward newFw = new ActionForward(oldFw);
                newFw.setPath(newPath);
                forward = newFw;
            }

            return forward;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private boolean isSpam(HttpServletRequest req, Comment comment) throws MalformedURLException
    {
        DmgConfig config = new DmgConfig();
        String apiKey = config.getProperty("spam.akismet.apikey");
        Akismet ak = new Akismet(apiKey, "http://www.dreadedmonkeygod.net");
        String permalink = getPermalink(req, comment);
        boolean isSpam = ak.commentCheck(comment.getIpAddress(), comment.getUserAgent(), comment
                .getReferrer(), permalink, "comment", comment.getUserName(),
                comment.getUserEmail(), comment.getUserUrl(), comment.getComment(), null);

        return isSpam;
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
