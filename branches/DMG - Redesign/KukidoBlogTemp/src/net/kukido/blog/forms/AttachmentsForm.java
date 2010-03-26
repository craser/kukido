package net.kukido.blog.forms;

import org.apache.struts.action.*;
import java.util.*;

import net.kukido.blog.datamodel.*;

public class AttachmentsForm extends ActionForm
{
    private Collection attachments = new ArrayList<Attachment>(0);
    
    public AttachmentsForm()
    {
    }
    
    public void setAttachments(Collection attachments)
    {
        this.attachments = attachments;
    }
    
    public Collection getAttachments()
    {
        return attachments;
    }
}
