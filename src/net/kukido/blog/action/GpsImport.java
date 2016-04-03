/*
 * LogEdit.java
 *
 * Created on February 25, 2004, 9:49 AM
 */

package net.kukido.blog.action;

import net.kukido.blog.dataaccess.AttachmentDao;
import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.dataaccess.LogDao;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.datamodel.Attachment;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import net.kukido.blog.forms.GpsImportForm;
import net.kukido.blog.log.Logging;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxFormatter;
import net.kukido.maps.TcxParser;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

/**
 * For the moment, I'm just taking direct import from my Garmin Edge 500, which emits TCX-formatted XML. So this
 * code assumes that input is TCX.
 *
 * I also STORE all activities in GPX format. So everything is parsed into abstract GPS data, then rendered as
 * GPX before being pushed into the database.
 *
 * @author craser
 */
public class GpsImport extends Action
{
    private Logger log = Logging.getLogger(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        try {
            GpsImportForm gpsForm = (GpsImportForm)form;
            User user = (User) req.getSession().getAttribute("user");
            int entryId = gpsForm.getEntryId();
            String fileName = gpsForm.getFileName();
            String title = gpsForm.getTitle();
            String activityId = gpsForm.getActivityId();
            String xml = gpsForm.getXml();
            log.debug(xml);

            List<GpsTrack> tracks = new TcxParser().parse(xml.getBytes());
            GpsTrack track = tracks.get(0); // Horrible hack.
            createAttachment(user, entryId, fileName, title, activityId, track);

            req.setAttribute("status", "success");
            req.setAttribute("message", "");
            log.debug("Returning status: success.");
            return mapping.findForward("results");
        }
        catch (Exception e) {
            log.fatal("Error processing incoming Garmin XML", e);
            req.setAttribute("status", "failure");
            req.setAttribute("message", e.toString());
            return mapping.findForward("results");
        }
    }

    /**
     * Creates a GPX-formatted file attachment for the given entry.
     *
     * @param user
     * @param entryId
     * @param fileName
     * @param activityId
     * @param track
     * @param track
     */
    public void createAttachment(User user, int entryId, String fileName, String title, String activityId, GpsTrack track) throws DataAccessException, IOException {
        LogEntry entry = new LogDao().findByEntryId(entryId);

        Attachment a = new Attachment();
        a.setUserName(user.getUserName());
        a.setUserId(user.getUserId());
        a.setEntryId(entryId);
        a.setFileType("map");
        a.setFileName(fileName);
        a.setActivityId(activityId);
        a.setTitle(title);

        GpxFormatter formatter = new GpxFormatter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        List<GpsTrack> tracks = new ArrayList<GpsTrack>();
        tracks.add(track);

        formatter.format(tracks, baos);
        log.debug(new String(baos.toByteArray()));
        a.setBytes(baos.toByteArray());

        new AttachmentDao().create(a);
    }
}