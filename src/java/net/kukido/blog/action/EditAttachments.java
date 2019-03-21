/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.action;


import org.apache.struts.action.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

/**
 *
 * @author craser
 */
public class EditAttachments extends Action 
{
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
        try {
            AttachmentsForm editForm = (AttachmentsForm)form;
            AttachmentDao dao = new AttachmentDao();
            dao.updateAll(editForm.getAttachments());

            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }

}
