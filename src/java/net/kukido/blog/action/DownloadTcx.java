package net.kukido.blog.action;

import net.kukido.blog.dataaccess.AttachmentDao;
import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.datamodel.Attachment;
import net.kukido.blog.log.Logging;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxParser;
import net.kukido.maps.TcxFormatter;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * All map attachments are currently stored in GPX format. So for this
 * Action we can kind of stupidly just grab them and convert the data
 * and the original file name.
 */
public class DownloadTcx extends Action
{
    private Logger log = Logging.getLogger(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        try {
            String fileName = req.getParameter("f");
            log.info("Generating TCX from attachment: \"" + fileName + "\".");
            AttachmentDao dao = new AttachmentDao();
            log.debug("Fetching attachment \"" + fileName + "\".");
            Attachment attachment = dao.findByFileName(fileName);
            dao.populateBytes(attachment);
            log.debug("Parsing GPX attachment file.");
            List<GpsTrack> tracks = new GpxParser().parse(attachment.getBytes());
            TcxFormatter formatter = new TcxFormatter();

            res.setHeader("Content-Disposition", getDispositionHeader(fileName));
            log.debug("Rendering TCX");
            formatter.format(tracks, res.getOutputStream());

            log.debug("Done rendering TCX for attachment \"" + fileName + "\".");
            return null; // Prevent further output to the stream.
        }
        catch (Exception e) {
            log.fatal("Unable to generate TCX from attachment.", e);
            throw new ServletException(e);
        }
    }

    public String getDispositionHeader(String fileName) {
        String attachmentName = getDownloadFileName(fileName);
        return "attachment; filename=\"" + attachmentName + "\"";
    }

    public String getDownloadFileName(String fileName) {
        return fileName.replace(".gpx", ".tcx");
    }
}
