package net.kukido.blog.forms;

import org.apache.struts.action.*;
import javax.servlet.http.*;

/**
 * This is used for multiple Actions: LogEntryListing, GalleryEntryListing
 **/
public class EntryListingForm extends SearchForm
{
    public EntryListingForm()
    {
    }

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
	super.reset(mapping, req);
	setPageSize(10);
    }
}
