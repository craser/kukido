/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.action;


import org.apache.struts.action.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

import net.kukido.blog.mail.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import java.util.*;


/**
 *
 * @author craser
 */
public class LoadMailAttachments extends Action {
    
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            User user = (User)req.getSession().getAttribute("user");
            UserAuthenticator userAuth = new UserAuthenticator(user);
            
            try { loadAttachments(user); }
            catch (Exception e) {
            	ByteArrayOutputStream out = new ByteArrayOutputStream();
            	e.printStackTrace(new PrintStream(out));
            	ActionMessages errors = new ActionMessages(getErrors(req));
            	errors.add("foo", new ActionMessage("error.email.load.failure", out.toString()));
            	this.addErrors(req, errors);
            }
            Collection<Attachment> attachments = (Collection<Attachment>)new AttachmentDao().findUnattached();
            
            for (Attachment a : attachments) {
                System.out.println("LOADED: " + a.getFileName());
            }
            
            req.setAttribute("attachments", attachments);
            
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
        	e.printStackTrace(System.out);
            throw new ServletException(e);
        }
    }        
    
    private void loadAttachments(User user) throws MessagingException
    {
        Store store = null;
        Folder folder = null;
        boolean expunge = true;
        try
        {   
            Properties p = System.getProperties();
            //p.setProperty("mail.store.protocol", "pop3");
            //p.setProperty("mail.debug", "true");
            Session session = Session.getDefaultInstance(p);
            
            store = session.getStore("pop3"); // FIXME: Should be configurable by user.
            store.connect("dreadedmonkeygod.net", 110, "uploads@dreadedmonkeygod.net", "m3t@b0l1zm");
            //store.connect("pop.gmail.com", 995, "chris.raser@gmail.com", "c0r0gati0n");
            
            folder = store.getDefaultFolder();
            folder = folder.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            
            int numMessages = Math.min(folder.getMessageCount(), 100);
            Message[] messages = folder.getMessages(1, numMessages); // Fetch first 100 messages.
            System.out.println("RETRIEVED " + messages.length + " MESSAGES");
            for (Message m : messages) {
                try {
                    if (!isSpam(m)) {
                        boolean loadedAll = loadAttachments(m, user);
                        if (loadedAll) {
                            System.out.println("DELETING MESSAGE");
                            folder.setFlags(new Message[] { m }, new Flags(Flags.Flag.DELETED), true);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        finally 
        {
            try { folder.close(expunge); } catch (Exception ignored) {}
            try { store.close(); } catch (Exception ignored) {}
        }
            
    }
    
    private boolean isSpam(Message message)
    {
        try {
            String dmgAddress = "chris@dreadedmonkeygod.net";
            String gmailAddress = "chris.raser@gmail.com";
            
            Address[] from = message.getFrom();
            for (Address a : from) {
                InternetAddress ia = (InternetAddress)a;
                if (dmgAddress.equalsIgnoreCase(ia.getAddress())) {
                    return false;
                }
                else if (gmailAddress.equalsIgnoreCase(ia.getAddress())) {
                    return false;
                }
            }
            
            return true;
        }
        catch (Exception e)
        {
            return true;
        }
    }
    
    private boolean loadAttachments(Message message, User user) throws IOException, MessagingException, DataAccessException
    {
        boolean loadedAll = true;
        if (message.isMimeType("multipart/*")) {
            Multipart multiPart = (Multipart)message.getContent();
            int numParts = multiPart.getCount();
            for (int i = 0; i < numParts; i++) { 
                Part p = multiPart.getBodyPart(i);
                System.out.println("    PARTS[" + i + "]: (" + p.getContentType() + ") " + p.getFileName());
                
                // This breaks for GPX attachments sent from Trails on my iPhone.  
                // Using getDisposition() fails universally. 
                //if (p.getFileName() != null)
                    try { 
                        loadAttachment(message, p, user); 
                    }
                    catch (Exception e) { 
                        e.printStackTrace(System.err);
                        loadedAll = false; 
                    }
                //}
            }
        }
        
        return loadedAll;
    }
    
    private void loadAttachment(Message message, Part part, User user) throws IOException, MessagingException, DataAccessException
    {
        AttachmentDao dao = new AttachmentDao();
        
        Attachment a = new Attachment();
        a.setTitle(message.getSubject() == null ? part.getFileName() : message.getSubject());
        a.setUserName(user.getUserName());
        a.setUserId(user.getUserId());
        a.setFileType(guessFileType(part));
        a.setDatePosted(new Date());
        a.setFileName(getSafeFileName(part, dao));
        
        InputStream partIn = null;
        try {
            partIn = part.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[256];
            for (int r = partIn.read(buff); r > 0; r = partIn.read(buff)) {
                out.write(buff, 0, r);
            }
            out.flush();
            
            a.setBytes(out.toByteArray());
            System.out.println("Creating new attachment: " + a.getFileName());
            dao.create(a);
        }
        finally {
            try { partIn.close(); } catch (Exception ignored) {}
        }
    }
    
    private String getSafeFileName(Part part, AttachmentDao dao) throws DataAccessException, MessagingException
    {
        String fileName = part.getFileName();
        if (fileName == null) { 
            fileName = "unknown";
        }
        if (!dao.fileNameExists(fileName)) {
            return fileName;
        }
        else {
            int prefix = 1;
            while (dao.fileNameExists(prefix + "_" + fileName)) {
                prefix++;
            }
            return prefix + "_" + fileName;
        }
    }
    
    private Attachment.FileType guessFileType(Part part) throws IOException, MessagingException
    {
        if (part.isMimeType("image/*")) {
            return Attachment.FileType.image;
        }
        else if (part.getFileName() != null && part.getFileName().endsWith(".gpx")) {
            return Attachment.FileType.map;
        }
        else {
            return Attachment.FileType.document;
        }
    }

}
