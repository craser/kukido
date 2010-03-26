package net.kukido.blog.action;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import net.kukido.blog.forms.*;

public class DropBoxUpload extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
	FileOutputStream fileOut = null;
	try
	{
	    DropBoxUploadForm uploadForm = (DropBoxUploadForm)form;
	    FormFile uploadFormFile = uploadForm.getUploadFile();
        
	    // FIXME change hard coding of directory path to a properties lookup.
	    String saveDirPath = this.getServlet().getServletContext().getRealPath("/dropbox");
	    File saveDir = new File(saveDirPath);
	    if (!saveDir.exists() && !saveDir.mkdir())
		throw new ServletException("Could not create directory " + saveDir.getCanonicalPath());
        
	    File uploadFile = new File(saveDir, uploadFormFile.getFileName());
	    fileOut = new FileOutputStream(uploadFile);
	    byte[] fileData = uploadFormFile.getFileData(); 
	    fileOut.write(fileData);
        
            ActionMessages messages = new ActionMessages();
            messages.add("uploadFile", new ActionMessage("upload.file.success", uploadFormFile.getFileName()));
            this.saveMessages(req, messages);

	    return mapping.findForward("success");
	}
	finally
	{
	    try { fileOut.flush(); } catch (Exception ignored) {}
	    try { fileOut.close(); } catch (Exception ignored) {}
	}
    }
}
