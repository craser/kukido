/*
 * DropBoxUploadForm.java
 *
 * Created on March 1, 2004, 7:02 AM
 */

package net.kukido.blog.forms;

import java.io.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;

/**
 *
 * @author  craser
 */
public class DropBoxUploadForm extends ActionForm 
{
    private FormFile uploadFile;

    public void setUploadFile(FormFile uploadFile)
    {
	this.uploadFile = uploadFile;
    }

    public FormFile getUploadFile()
    {
	return uploadFile;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
	ActionErrors errors = new ActionErrors();
        if (null == uploadFile) 
        {
            errors.add("uploadFile", new ActionMessage("error.missing.file"));
        }
	return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
	setUploadFile(null);
    }
}
