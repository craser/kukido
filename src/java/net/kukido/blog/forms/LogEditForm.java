/*
 * LogEditForm.java
 *
 * Created on February 23, 2004, 11:34 AM
 */

package net.kukido.blog.forms;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import javax.servlet.http.*;
import java.util.Iterator;
import org.apache.struts.action.*;

/**
 *
 * @author  craser
 */
public class LogEditForm extends LogUpdateForm 
{
   
    public void setEntryId(int entryId)
    {
        try
        {
            LogEntry entry = new LogDao().findByEntryId(entryId);
	    setEntry(entry);
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace(System.out);
            // We'll handle the error case in validate()
        }
    }
    
    public int getEntryId()
    {
        return getEntry().getEntryId();
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
        ActionErrors errors = new ActionErrors();
        try 
        {
            new LogDao().findByEntryId(getEntryId()); 
            errors.add(super.validate(mapping, req));
        }
        catch (DataAccessException e)
        {
            errors.add("entryId", new ActionMessage("error.entryid.not.found", new Integer(getEntryId())));
        }
        return errors;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        getEntry().setAllowComments(false);
        for (Iterator as = getEntry().getAttachments().iterator(); as.hasNext(); )
        {
            ((Attachment)as.next()).setIsGalleryImage(false);
        }
    }
}
