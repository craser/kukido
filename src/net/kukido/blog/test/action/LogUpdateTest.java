package net.kukido.blog.test.action;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import net.kukido.blog.action.LogUpdate;
import net.kukido.blog.dataaccess.LogDao;
import net.kukido.blog.dataaccess.TagDao;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.blog.datamodel.Tag;
import net.kukido.blog.datamodel.User;
import net.kukido.blog.forms.LogUpdateForm;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.Test;

public class LogUpdateTest 
{
	private final int USER_ID = -1;
	private final String USER_NAME = "junit";

    private final Date now = new Date();
    private Collection<Tag> tags = Arrays.asList(new Tag[] { 
            new Tag("foo"),
            new Tag("bar"),
            new Tag("zaz")
    });
    private int entryId; // For cleanup.
    

	
    @Test
    public void test_execute() throws Exception
    {
    	try {
    		this.entryId = -1;
	        this.tags = new TagDao().create(tags); // Tested in TagDaoTest.  Rely on its correct functioning here.
	        
	        MockHttpServletRequest req = buildRequest();
	        MockHttpServletResponse res = new MockHttpServletResponse();
	        ActionMapping map = buildActionMapping();
	    	LogUpdateForm form = new LogUpdateForm();
	    	LogEntry entry = buildLogEntry();
			form.setEntry(entry);
	        LogUpdate action = new LogUpdate();
	    	ActionForward fwd = action.execute(map, form, req, res);
	    	
	    	// Make sure that the Action passed back a new LogEntry.
	    	LogEntry created = (LogEntry)req.getAttribute("entry");
	    	this.entryId = created.getEntryId();
	    	assertTrue(created != null);
	    	
	    	// Make sure that the action was successful.
	    	assertTrue("success".equals(fwd.getName()));
	    	
	    	assertSameEntries(entry, created);
	    	assertTrue(withinFiveMinutes(created.getDatePosted(), now));
	    	assertFalse(entry.getEntryId() == created.getUserId());
    	}
    	finally {
    		if (entryId != -1) {
	            LogDao dao = new LogDao();
	            dao.delete(entryId);
    		}
    	}
    }
    
    private void assertSameEntries(LogEntry a, LogEntry b) 
    {	
		// Make sure that the LogEntry was created correctly. (We'll assume that
		// the LogDao works correctly here. It's tested in LogDaoTest.) 
        assertTrue(a.getAllowComments() == b.getAllowComments());
        assertEquals(a.getBody(), b.getBody());
        assertEquals(a.getImageFileName(), b.getImageFileName());
        assertEquals(a.getImageFileType(), b.getImageFileType());
        assertEquals(a.getIntro(), b.getIntro());
        assertEquals(a.getTitle(), b.getTitle());
        assertEquals(a.getUserId(), b.getUserId());
        assertEquals(a.getUserName(), b.getUserName());
        assertEquals(a.getViaText(), b.getViaText());
        assertEquals(a.getViaTitle(), b.getViaTitle());
        assertEquals(a.getViaUrl(), b.getViaUrl());
        assertTrue(sameTags(a.getTags(), b.getTags()));
    }

	private MockHttpServletRequest buildRequest() {
		User user = new User();
		user.setUserId(USER_ID);
		user.setUserName(USER_NAME);
		
		MockHttpServletRequest req =  new MockHttpServletRequest();
		req.getSession().setAttribute("user", user);
		
		return req;
	}

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
        entry.setTags(tags);
		return entry;
	}
    
    /**
     * Determines whether two Collections of Tag objects contain the
     * same Tags.
     * @param a
     * @param b
     * @return
     */
    private boolean sameTags(Collection<Tag> a, Collection<Tag> b) {
        return a.containsAll(b) && b.containsAll(a) && (a.size() == b.size());
    }
    
    /**
     * Determines whether two dates are within five minute of each other.
     * Since the DAO is supposed to assign create/update dates, we can't
     * directly check what they should be.  Instead, we just make sure 
     * they're something reasonable.  "Reasonable" in this case means 
     * "within five minutes of the current time."
     */
    private boolean withinFiveMinutes(Date a, Date b) {
        return Math.abs(a.getTime() - b.getTime()) < 30000; // 30k ms == 1000 * 60 * 5
    }
    
    public ActionMapping buildActionMapping() 
    {
        ActionMapping map = new ActionMapping();
        ActionForward success = new ActionForward("success", "path", false);
    	map.addForwardConfig(success);
    	
    	return map;
    }
}
