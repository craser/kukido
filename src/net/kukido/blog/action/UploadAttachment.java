/*
 * MophoUpload.java
 *
 * Created on October 16, 2003, 7:00 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.util.*;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.zip.*;

/**
 * @author  craser
 * @version
 */
public class UploadAttachment extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
	try
	{
	    User user = (User)req.getSession().getAttribute("user");
	    AttachmentForm attachmentForm = (AttachmentForm)form;
            
            if (attachmentForm.getLoadFromUrl()) {
                handleUrlUpload(attachmentForm, user);
            }
            else if (attachmentForm.getIsZipFile()) {
                handleZipFileUpload(attachmentForm, user);
            }
            else {
                handleSingleFileUpload(attachmentForm, user);
            }

            return mapping.findForward("success");
	}
	catch (Exception e)
	{
	    e.printStackTrace(System.out);
	    throw new ServletException(e);
	}
    }
    
    private void handleUrlUpload(AttachmentForm attachmentForm, User user)
        throws IOException, DataAccessException
    {
        Attachment attachment = attachmentForm.getAttachment();
        attachment.setUserId(user.getUserId());
        attachment.setUserName(user.getUserName());
        
        URL fileUrl = new URL(attachmentForm.getFileUrl());
        InputStream urlIn = fileUrl.openStream();
        ByteArrayOutputStream fileDataOut = new ByteArrayOutputStream();
        byte[] buff = new byte[256];
        for (int r = 0; r >= 0; ) {
            fileDataOut.write(buff, 0, r);
            r = urlIn.read(buff, 0, 256);
        }
        fileDataOut.flush();
        attachment.setBytes(fileDataOut.toByteArray());
        
        new AttachmentDao().create(attachment);
    }
    
    private void handleZipFileUpload(AttachmentForm attachmentForm, User user)
        throws IOException, DataAccessException
    {
        Attachment zipAttachment = attachmentForm.getAttachment();
        AttachmentDao attachmentDao = new AttachmentDao();
        FormFile file = attachmentForm.getFile();
        byte[] zipData = file.getFileData();
        ByteArrayInputStream byteIn = new ByteArrayInputStream(zipData);
        ZipInputStream zipIn = new ZipInputStream(byteIn);
        for (ZipEntry z = zipIn.getNextEntry(); z != null; z = zipIn.getNextEntry())
        {
            Attachment a = new Attachment();
            a.setUserId(user.getUserId());
            a.setUserName(user.getUserName());
            a.setFileName(z.getName());
            a.setFileType(Attachment.TYPE_DOCUMENT);
            a.setTitle(zipAttachment.getTitle());
            a.setEntryId(zipAttachment.getEntryId());
            
            byte[] buff = new byte[256];
            ByteArrayOutputStream fileDataOut = new ByteArrayOutputStream();
            for (int r = 0; r >= 0; ) { // Maybe this will work?
                fileDataOut.write(buff, 0, r);
                r = zipIn.read(buff, 0, 256);
            }
            fileDataOut.flush();
            a.setBytes(fileDataOut.toByteArray());
            
            attachmentDao.create(a);
        }
    }
    
    private void handleSingleFileUpload(AttachmentForm attachmentForm, User user)
        throws DataAccessException, IOException, java.text.ParseException, org.apache.sanselan.ImageReadException
    {
        Attachment attachment = attachmentForm.getAttachment();
	attachment.setUserName(user.getUserName());
	attachment.setUserId(user.getUserId());
        
        try {
            if (attachment.getIsGalleryImage()) {
                ImageTools tools = new ImageTools();
                Date dateTaken = tools.getDateTaken(attachment.getBytes());
                attachment.setDateTaken(dateTaken);
            }
        }
        catch (Exception ignored) { // Setting the date isn't vital.
            ignored.printStackTrace(System.err);
        }
        
        new AttachmentDao().create(attachment);

        if (attachmentForm.getUseAsGalleryThumb())
        {
            LogDao logDao = new LogDao();
            LogEntry entry = logDao.findByEntryId(attachment.getEntryId());
            entry.setImageFileName(attachment.getFileName());
            entry.setImageDisplayClass(attachmentForm.getImageDisplayClass());
            logDao.update(entry);
        }        
    }
}
