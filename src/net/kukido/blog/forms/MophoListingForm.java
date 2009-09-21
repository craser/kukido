package net.kukido.blog.forms;

import org.apache.struts.action.*;
import javax.servlet.http.*;

public class MophoListingForm extends MophoSelectionForm
{
    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
	super.reset(mapping, req);
        setPageSize(10);
    }
}
