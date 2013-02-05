package net.kukido.blog.test.dataaccess;

import java.util.Date;

import org.junit.* ;
import static org.junit.Assert.* ;

import net.kukido.blog.datamodel.Attachment;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.dataaccess.LogDao;
import net.kukido.sql.NamedParamStatement;

public class AttachmentDaoTest
{
    private final Date now = new Date();
    private int entryId; // For cleanup.
    
    
    @Before
    public void createLogEntry() throws DataAccessException
    {
        LogDao logDao = new LogDao();
        LogEntry entry = buildLogEntry();
        LogEntry created = logDao.create(entry); // Tested in LogDaoTest.  Assume correct functioning here.
        entryId = created.getEntryId();    	
    }
    
    @After
    public void test_delete() throws DataAccessException
    {
        LogDao dao = new LogDao();
        dao.delete(entryId); // Tested in LogDaoTest.  Assume here that it works correctly.
    }
    
    @Test
    public void test_create() throws DataAccessException
    {

    }
    
    private LogEntry buildLogEntry() 
    {
        LogEntry entry = new LogEntry();
        entry.setAllowComments(true);
        entry.setBody("body");
        entry.setDatePosted(now);
        entry.setEntryId(-1); // Confirm later that this got properly set.
        entry.setImageFileName("image-file-name");
        entry.setImageFileType(Attachment.FileType.document);
        entry.setIntro("intro");
        entry.setLastUpdated(now);
        entry.setTitle("title");
        entry.setUserId(-1);
        entry.setUserName("junit");
        entry.setViaText("via-text");
        entry.setViaTitle("via-title");
        entry.setViaUrl("via-url");
        //entry.setTags(tags); // We don't need this to test attachments.
       
        return entry;
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
}
