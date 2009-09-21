package net.kukido.blog.forms;

import org.apache.struts.action.*;
import javax.servlet.http.*;

/**
 * This is used for multiple Actions: LogEntryListing, GalleryEntryListing
 **/
public class MophoSelectionForm extends ActionForm
{
    private int page;		// 0-indexed.
    private int pageSize;	// Number of entries per page
    private int entryId;        // Entry ID to retrieve
    private int userId;		// User for which to find entries

    public void setPage(int page)
    {
	this.page = page;
    }

    public int getPage()
    {
	return page;
    }

    public void setPageSize(int pageSize)
    {
	this.pageSize = pageSize;
    }

    public int getPageSize()
    {
	return pageSize;
    }
    
    public void setEntryId(int entryId)
    {
        this.entryId = entryId;
    }
    
    public int getEntryId()
    {
        return entryId;
    }

    public void setUserId(int userId)
    {
	this.userId = userId;
    }

    public int getUserId()
    {
	return userId;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
        return new ActionErrors();
    }

    public void reset()
    {
	reset(null, null);
    }

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
	this.page = 0;
	this.pageSize = 0;
	this.entryId = -1;
        this.userId = -1;
    }
}    
