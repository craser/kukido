/*
 * CommentForm.java
 *
 * Created on February 21, 2006, 10:04 PM
 */

package net.kukido.blog.forms;

import net.kukido.blog.datamodel.Trackback;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;
import java.net.URL;


public class TrackbackForm extends ActionForm
{
    
    private Trackback trackback;
    
    public Trackback getTrackback()
    {
        return trackback;
    }
    
    public void setTrackback(Trackback tb)
    {
        this.trackback = trackback;
    }
    
    public void setEntryId(int entryId)
    {
        this.trackback.setEntryId(entryId);
    }
    
    public void setTitle(String title)
    {
        trackback.setTitle(title);
    }
    
    public void setExcerpt(String excerpt)
    {
        trackback.setExcerpt(excerpt);
    }
    
    public void setUrl(String url)
    {
        trackback.setUrl(url);
    }
    
    public void setBlog_name(String blogName)
    {
        System.out.println("setBlog_name");
        trackback.setBlogName(blogName);
    }
    
    

    /**
     * Ensures that we have values for both the title and intro fields.  Does
     * no checking for presence or validity of the body field.
     **/
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
	ActionErrors errors = new ActionErrors();
	if ("".equals(trackback.getUrl()) || null == trackback.getUrl())
        {
	    errors.add("trackback.url", new ActionMessage("error.trackback.missing.url"));
        }
        else
        {
            try { new URL(trackback.getUrl()); } // Quick sanity check
            catch (Exception e) { 
                errors.add("trackback.url", new ActionMessage("error.trackback.invalid.url", trackback.getUrl())); 
            }
        }
        
	return errors;
    }

   /**
    * Resets this form with a new blank comment, and sets up a few values
    * that come from the request, rather than user input.
    */
    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        trackback = new Trackback();
        trackback.setIpAddress(req.getRemoteAddr());
        trackback.setUserAgent(req.getHeader("user-agent"));
        trackback.setReferrer(req.getHeader("referrer"));
    }
}
