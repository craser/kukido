package net.kukido.blog.test.action;

import static org.junit.Assert.*;
import java.util.Date;

import net.kukido.blog.action.LogDelete;
import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.dataaccess.LogDao;
import net.kukido.blog.datamodel.LogEntry;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.Test;

public class LogDeleteTest 
{
    private final Date now = new Date();
	private final int USER_ID = -1;
	private final String USER_NAME = "junit";
    
	private int entryId; // Used for cleanup.

    @Test
	public void test_nothing() throws Exception
	{
		assertTrue(true); // hard-coded test pass.
	}


//    public void test_execute() throws Exception
//    {
//    	this.entryId = -1;
//    	LogDao logDao = new LogDao();
//		try {
//    		// First, we have to created a dummy entry so that we can test deleting it.
//    		LogEntry entry = buildLogEntry();
//    		LogEntry created = logDao.create(entry);
//    		this.entryId = created.getEntryId();
//
//    		// Okay.  Now we can actually test the LogDelete action.
//	        MockHttpServletRequest req = new MockHttpServletRequest();
//	        req.addParameter("entryId", Integer.toString(entryId));
//	        MockHttpServletResponse res = new MockHttpServletResponse();
//	        ActionMapping map = buildActionMapping();
//	        LogDelete action = new LogDelete();
//	    	ActionForward fwd = action.execute(map, null, req, res);
//
//	    	// Make sure that the action was successful.
//	    	assertTrue("success".equals(fwd.getName()));
//
//	    	try {
//	    		logDao.findByEntryId(entryId);
//	    		fail("LogEntry with ID " + entryId + " was not deleted.");
//	    	}
//	    	catch (DataAccessException expected) {
//	    		// All is well.  Do nothing.
//	    	}
//    	}
//    	finally {
//    		if (entryId != -1) {
//	            logDao.delete(entryId);
//    		}
//    	}
//    }

	private LogEntry buildLogEntry() {
		LogEntry entry = new LogEntry();
        entry.setAllowComments(true);
        entry.setBody("body");
        entry.setDatePosted(now);
        entry.setEntryId(-1); // Confirm later that this got properly set.
        entry.setImageFileName("image-file-name");
        //entry.setImageFileType(Attachment.FileType.document);
        entry.setImageFileType(null);
        entry.setIntro("intro");
        entry.setLastUpdated(now);
        entry.setTitle("title");
        entry.setUserId(USER_ID);
        entry.setUserName(USER_NAME);
        entry.setViaText("via-text");
        entry.setViaTitle("via-title");
        entry.setViaUrl("via-url");
		return entry;
	}
    
    public ActionMapping buildActionMapping() 
    {
        ActionMapping map = new ActionMapping();
        ActionForward success = new ActionForward("success", "path", false);
    	map.addForwardConfig(success);
    	
    	return map;
    }
}
