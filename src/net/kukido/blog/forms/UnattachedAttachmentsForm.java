/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.forms;

import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import org.apache.struts.action.*;
import javax.servlet.http.*;
import java.util.*;

/**
 *
 * @author craser
 */
public class UnattachedAttachmentsForm extends AttachmentsForm 
{
    private int entryId;
    
    public UnattachedAttachmentsForm()
    {
    }
    
    public void setEntryId(int entryId)
    {
        this.entryId = entryId;
    }
    
    public int getEntryId()
    {
        return entryId;
    }
    
    public void reset(ActionMapping map, HttpServletRequest req)
    {
        try {
            super.reset(map, req);
            AttachmentDao dao = new AttachmentDao();
            Collection attachments = dao.findUnattached();
            setAttachments(attachments);
        }
        catch (DataAccessException e) {
            e.printStackTrace(System.err);
            setAttachments(new ArrayList()); // Blank out the list
        }
    }
}
