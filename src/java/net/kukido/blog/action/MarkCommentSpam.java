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

public class MarkCommentSpam extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            int commentId = Integer.parseInt(req.getParameter("commentId"));
            CommentDao commentDao = new CommentDao();
            Comment comment = commentDao.findByCommentId(commentId);
            comment.setIsSpam(true);
            commentDao.update(comment);
            
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
}
