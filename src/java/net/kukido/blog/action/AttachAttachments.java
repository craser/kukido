/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.action;


import org.apache.struts.action.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

/**
 *
 * @author craser
 */
public class AttachAttachments extends Action 
{
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
        int entryId = Integer.parseInt(req.getParameter("entryId"));
        String[] fileNames = req.getParameterValues("fileName");
        
        AttachmentDao dao = new AttachmentDao();
        for (String fileName : fileNames) {
            try {
                Attachment a = dao.findByFileName(fileName);
                a.setEntryId(entryId);
                dao.update(a);
            }
            catch (Exception e) {
                System.err.println("Exception while attaching attachments:");
                e.printStackTrace(System.err);
            }
        }
        
        ActionForward fw = mapping.findForward("success");
        String newPath = fw.getPath() + entryId;
        ActionForward newFw = new ActionForward(fw);
        newFw.setPath(newPath);
        
        return newFw;
    }

}
