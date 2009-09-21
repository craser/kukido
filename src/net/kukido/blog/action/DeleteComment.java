/*
 * CreateComment.java
 *
 * Created on February 21, 2006, 10:02 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

public class DeleteComment extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            int commentId = Integer.parseInt(req.getParameter("commentId"));
            CommentDao commentDao = new CommentDao();
            Comment comment = commentDao.findByCommentId(commentId);
            commentDao.delete(comment);
            
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
    
    private boolean isValid(CommentForm commentForm, ActionMapping mapping, HttpServletRequest req)
    {
        ActionErrors errors = commentForm.validate(mapping, req);
        return (null != errors && !errors.isEmpty());
    }
}
