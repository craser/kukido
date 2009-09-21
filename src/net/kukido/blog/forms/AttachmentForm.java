package net.kukido.blog.forms;

import java.net.URL;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;
import java.util.*;

import net.kukido.blog.datamodel.*;

public class AttachmentForm extends ActionForm
{
    private FormFile file;
    private Attachment attachment;
    private boolean useAsGalleryThumb;
    private boolean loadFromUrl;
    private boolean isZipFile;
    private String imageDisplayClass;
    private String fileUrl;


    public AttachmentForm()
    {
	this.attachment = new Attachment();
    }
    
    public void setEntryId(int entryId)
    {
	attachment.setEntryId(entryId);
    }
    
    public int getEntryId()
    {
        return attachment.getEntryId();
    }

    public void setAttachment(Attachment attachment)
    {
	this.attachment = attachment;
    }

    public Attachment getAttachment()
    {
	return attachment;
    }

    public void setUseAsGalleryThumb(boolean useAsGalleryThumb)
    {
        this.useAsGalleryThumb = useAsGalleryThumb;
    }

    public boolean getUseAsGalleryThumb()
    {
	return useAsGalleryThumb;
    }
    
    public void setLoadFromUrl(boolean loadFromUrl)
    {
        this.loadFromUrl = loadFromUrl;
    }
    
    public boolean getLoadFromUrl()
    {
        return loadFromUrl;
    }
    
    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }
    
    public String getFileUrl()
    {
        return this.fileUrl;
    }
    
    
    public void setImageDisplayClass(String imageDisplayClass)
    {
        this.imageDisplayClass = imageDisplayClass;
    }
    
    public String getImageDisplayClass() 
    {
        return imageDisplayClass;
    }
    
    public Collection getDisplayClassOptions()
    {
        return LogEntry.getDisplayClassOptions();
    }

    public FormFile getFile()
    {
	return file;
    }

    public void setFile(FormFile file)
    {
	try
	{
            if (file.getFileSize() > 0) { // Make sure the file is non-empty.
                this.file = file;
                attachment.setFileName(file.getFileName());
                attachment.setBytes(file.getFileData());
            }
	}
	catch (Exception e)
	{
	    attachment.setFileName(null);
	    attachment.setBytes(null);
	    e.printStackTrace();
	}
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
	ActionErrors errors = super.validate(mapping, req);
        
        if (loadFromUrl) {
            try {
                new URL(fileUrl); // Attempt to parse the given URL.
            }
            catch (Exception e) {
                errors.add("fileUrl", new ActionMessage("error.invalid.url"));
            }
            
            if (attachment.getFileName() == null) {
                errors.add("attachment.fileName", new ActionMessage("error.missing.fileName"));
            }
        }
        else {
            if (attachment.getBytes() == null)
                errors.add("attachment", new ActionMessage("error.missing.image"));
        }
        
	return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        super.reset(mapping, req);
	this.useAsGalleryThumb = false;
        this.loadFromUrl = false;
        this.fileUrl = null;
    }
    
    /**
     * Getter for property isZipFile.
     * @return Value of property isZipFile.
     */
    public boolean getIsZipFile() {
        return isZipFile;
    }
    
    /**
     * Setter for property isZipFile.
     * @param isZipFile New value of property isZipFile.
     */
    public void setIsZipFile(boolean isZipFile) {
        this.isZipFile = isZipFile;
    }
    
    public Collection getFileTypeOptions() {
        return Attachment.getFileTypeOptions();
    }
}
