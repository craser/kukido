package net.kukido.blog.test.action;

import javax.servlet.ServletException;

import net.kukido.blog.action.ResolveElevation;
import net.kukido.blog.action.ThrowAction;
import net.kukido.blog.dataaccess.AttachmentDao;
import net.kukido.blog.datamodel.Attachment;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.*;

import static org.junit.Assert.*;

public class ResolveElevationTest 
{
    @Test
    public void test_execute() throws Exception
    {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        ResolveElevation action = new ResolveElevation();
        ActionMapping map = buildActionMapping();
        AttachmentDao dao = new AttachmentDao();
        String fileName = "1026385_bark-park-full-counterclockwise-loop.gpx";
    	req.addParameter("file", fileName);
    	req.addParameter("entryId", "42");
    	ActionForward fwd = action.execute(map, null, req, res);
    	
    	Attachment att = dao.findByFileName(fileName);
    	assertTrue(dao.hasBackup(att));
    	assertTrue(fwd.getPath().endsWith("42"));
    	assertTrue("success".equals(fwd.getName()));
    }
    
    public ActionMapping buildActionMapping() 
    {
        ActionMapping map = new ActionMapping();
        ForwardConfig success = new ForwardConfig("success", "path", false);
    	map.addForwardConfig(success);
    	
    	return map;
    }
}
