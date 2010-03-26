/*
 * CommentForm.java
 *
 * Created on February 21, 2006, 10:04 PM
 */

package net.kukido.blog.forms;

import net.kukido.blog.datamodel.Comment;
import net.kukido.blog.servlet.CommentCookie;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import java.net.URI;


public class CommentForm extends ActionForm
{
    // This detects attempts at inserting HTML into 
    // comments.
    static private String XSS_REGEXP = "/((\\%3C)|<)[^\\n]+((\\%3E)|>)/I";
    
    private Comment comment;
    private boolean remember;
    
    public Comment getComment()
    {
        return comment;
    }
    
    public void setComment(Comment comment)
    {
        this.comment = comment;
    }

    /**
     * Ensures that we have values for both the title and intro fields.  Does
     * no checking for presence or validity of the body field.
     **/
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
	ActionErrors errors = new ActionErrors();
        String commentString = comment.getComment();
	if ("".equals(commentString) || null == commentString)
	    errors.add("comment.comment", new ActionMessage("error.missing.comment"));
        else if (commentString.matches(XSS_REGEXP))
            errors.add("comment.comment", new ActionMessage("error.no.html.allowed"));
        else if (!comment.getComment().equals(comment.getComment().replaceAll("<[^>]*>", "")))
            errors.add("comment.comment", new ActionMessage("error.no.html.allowed"));
        
        
        if (!"".equals(comment.getUserUrl())) {
            try { URI uri = new URI(comment.getUserUrl()); }
            catch (Exception e) { errors.add("comment.userUrl", new ActionMessage("error.invalid.url")); }
        }
        
	if ("".equals(comment.getUserName()) || null == comment.getUserName())
	    errors.add("comment.userName", new ActionMessage("error.missing.username"));
	return errors;
    }

   /**
    * Resets this form with a new blank comment, and sets up a few values
    * that come from the request, rather than user input.
    */
    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        setComment(new Comment());
        setRemember(false);
        comment.setIpAddress(req.getRemoteAddr());
        comment.setUserAgent(req.getHeader("user-agent"));
        comment.setReferrer(req.getHeader("referrer"));
        try {
            // Use default values saved in a user cookie, if it's available.
            
            // CommentCookie.getCommentCookie throws NullPointerException if
            // no cookie is found.
            CommentCookie commentCookie = CommentCookie.getCommentCookie(req);
            comment.setUserName(commentCookie.getUserName());
            comment.setUserEmail(commentCookie.getEmail());
            comment.setUserUrl(commentCookie.getUrl());
            setRemember(true);
        }
        catch (Exception ignored) {
            // Catch and ignore CommentCookie's NullPointerException
            //ignored.printStackTrace(System.err);
        }
    }
    
    /**
     * Getter for property remember.
     * @return Value of property remember.
     */
    public boolean getRemember() {
        return remember;
    }
    
    /**
     * Setter for property remember.
     * @param remember New value of property remember.
     */
    public void setRemember(boolean remember) {
        this.remember = remember;
    }
    
}
