package net.kukido.blog.action;

import net.kukido.blog.dataaccess.AttachmentDao;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Created by craser on 12/20/15.
 */
public class ActivityIds extends Action
{
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
            throws ServletException
    {
        try {
            AttachmentDao dao = new AttachmentDao();
            Collection<String> activityIds = dao.findActivityIds();
            req.setAttribute("activityIds", activityIds);
            return mapping.findForward("success");
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
