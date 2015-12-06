package net.kukido.blog.action;

import net.kukido.blog.datamodel.User;
import net.kukido.blog.log.Logging;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.TcxParser;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by craser on 12/18/15.
 */
public class GpsImportForm extends Action
{
    private Logger log = Logging.getLogger(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        try {
            String entryId = req.getParameter("entryId");
            log.debug("entryId: " + entryId);
            req.setAttribute("entryId", entryId);
            return mapping.findForward("success");
        }
        catch (Exception e) {
            log.fatal("Error processing incoming Garmin XML", e);
            throw new ServletException(e);
        }
    }
}
