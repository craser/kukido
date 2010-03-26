package net.kukido.blog.forms;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;

public class MophoUploadForm extends ActionForm
{
    private FormFile image;
    private String title;
    
    public void setImage(FormFile image)
    {
	this.image = image;
    }

    public FormFile getImage()
    {
	return image;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getTitle()
    {
	return title;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
	ActionErrors errors = new ActionErrors();
	if (null == title || "".equals(title))
	    errors.add("title", new ActionMessage("error.missing.title"));
	if (null == image)
	    errors.add("image", new ActionMessage("error.missing.image"));
	return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
	setImage(null);
	setTitle(null);
    }

}
