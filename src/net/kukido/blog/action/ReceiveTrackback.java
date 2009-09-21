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
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import net.kukido.blog.config.*;
import net.sf.akismet.Akismet;


public class ReceiveTrackback extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            TrackbackForm commentForm = (TrackbackForm)form;
            ActionErrors errors = commentForm.validate(mapping, req);
            
            ActionMessage foo = null;
            saveErrors(req, errors);
            
            Trackback trackback = commentForm.getTrackback();
            trackback.setDirection(TrackbackDao.DIRECTION_RECEIVED);
            if (null == errors || errors.isEmpty())
            {
                trackback.setIsSpam(isSpam(req, trackback));                
                new TrackbackDao().create(trackback);
            }

            // Curse the struts people for not making ActionErrors a Collection!
            List errorsList = new LinkedList(); // List of ActionMessage
            for (Iterator itr = errors.get(); itr.hasNext(); ) {
                errorsList.add(itr.next());
            }
            
            req.setAttribute("trackback", trackback);
            req.setAttribute("errors", errorsList);
            
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    private boolean isSpam(HttpServletRequest req, Trackback trackback)
        throws MalformedURLException
    {
        DmgConfig config = new DmgConfig();
        String apiKey = config.getProperty("spam.akismet.apikey");
        Akismet ak = new Akismet(apiKey, "http://www.dreadedmonkeygod.net");
        String permalink = getPermalink(req, trackback);        
        boolean isSpam = ak.commentCheck(trackback.getIpAddress()
                ,trackback.getUserAgent()
                ,trackback.getReferrer()
                ,permalink
                ,"trackback"
                ,null // user name
                ,null // user email
                ,trackback.getUrl()
                ,trackback.getExcerpt()
                ,null);

        return isSpam;        
    }
    
    /**
     * @return the base url of this page, including the trailing slash.
     **/
    private String getPermalink(HttpServletRequest req, Trackback trackback)
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
        baseUrl.append(trackback.getEntryId());

	return baseUrl.toString();
    }

}
