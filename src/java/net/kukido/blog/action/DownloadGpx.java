package net.kukido.blog.action;

import net.kukido.blog.dataaccess.AttachmentDao;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * All map attachments are currently stored as GPX files, so we don't
 * need to do any conversion or formatting. Just set the HTTP headers
 * and dump the file contents.
 */
public class DownloadGpx extends Action
{
    Logger log = Logging.getLogger(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
            throws ServletException
    {
        try {
            String fileName = req.getParameter("f");
            log.info("Fetching attachment: \"" + fileName + "\".");
            AttachmentDao dao = new AttachmentDao();
            Attachment attachment = dao.findByFileName(fileName);
            dao.populateBytes(attachment);

            res.setHeader("Content-Disposition", attachment.getFileName());
            res.getOutputStream().write(attachment.getBytes());

            log.debug("Done rendering TCX for attachment \"" + fileName + "\".");
            return null; // Prevent further output to the stream.
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
