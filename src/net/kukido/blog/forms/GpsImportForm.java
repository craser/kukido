package net.kukido.blog.forms;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by craser on 12/6/15.
 */
public class GpsImportForm extends ActionForm
{
    private int entryId;
    private String activityId;
    private String fileName;
    private String title;
    private String xml;

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getEntryId() {
        return entryId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
        ActionErrors errors = new ActionErrors();
        //errors.add("title", new ActionMessage("error.missing.title"));
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        //setEntry(new LogEntry());
    }
}
