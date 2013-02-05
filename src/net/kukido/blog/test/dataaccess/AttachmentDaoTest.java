package net.kukido.blog.test.dataaccess;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

import org.junit.* ;
import static org.junit.Assert.* ;

import net.kukido.blog.datamodel.Attachment;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.blog.dataaccess.AttachmentDao;
import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.dataaccess.LogDao;
import net.kukido.sql.NamedParamStatement;

public class AttachmentDaoTest
{
	private static final String TEST_FILE = "/Users/craser/Development/workspace/KukidoBlogTemp/sample-files/garmin-logbook-sample.xml";
	
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
    public void deleteLogEntry() throws DataAccessException
    {
        LogDao dao = new LogDao();
        dao.delete(entryId); // Tested in LogDaoTest.  Assume here that it works correctly.
    }
    
    @Test
    public void test_create()
    {
    	try {
	    	Attachment attachment = buildAttachment(entryId);
	    	AttachmentDao attachmentDao = new AttachmentDao();
	    	attachmentDao.create(attachment);
	    	
	    	Collection<Attachment> attachments = attachmentDao.findByEntryId(entryId);
	    	assertFalse(attachments.isEmpty());
	    	Attachment created = getFirstAttachment(attachments);
	    	
	    	assertEquals(attachment.getFileName(), created.getFileName());
	    	assertEquals(entryId, created.getEntryId());
	    	assertEquals(attachment.getFileType(), created.getFileType());
	    	assertEquals(attachment.getMimeType(), created.getMimeType());
	    	assertEquals(attachment.getTitle(), created.getTitle());
	    	assertTrue(created.getBytes() == null); // Not yet retrieved.
	    	attachmentDao.populateBytes(created); // Retrieve
	    	assertTrue(created.getBytes() != null); // Retrieved
	    	assertArrayEquals(attachment.getBytes(), created.getBytes());
	    	
	    	
    	}
    	catch (Exception e) {
    		fail("Exception trying to create attachment: " + e);
    	}
    }
    
    @Test
    public void test_delete() 
    {
    	try {
    		Attachment attachment = buildAttachment(entryId);
    		AttachmentDao attachmentDao = new AttachmentDao();
    		attachmentDao.create(attachment); // Assume this works.  Tested elsewhere.
    		Attachment created = getFirstAttachment(attachmentDao.findByEntryId(entryId));
    		attachmentDao.delete(created);
    		
    		try {
    			attachmentDao.findByFileName(TEST_FILE); // SHOULD throw exception
    			fail("Attachment with name " + TEST_FILE + " should have been deleted!");
    		}
    		catch (DataAccessException e) {
    			// All is well.  This is expected.
    		}
    	}
    	catch (Exception e) {
    		fail("Exception trying to delete attachment: " + e);
    	}
    }
    
    @Test
    public void test_backup() 
    {
    	try {
    		Attachment attachment = buildAttachment(entryId);
    		AttachmentDao attachmentDao = new AttachmentDao();
    		attachmentDao.create(attachment); // Assume this works.  Tested elsewhere.
    		Attachment created = getFirstAttachment(attachmentDao.findByEntryId(entryId));
    		
    		attachmentDao.makeBackup(created);
    		
    		assertTrue("Backup not created!", attachmentDao.hasBackup(created));    		
    	}
    	catch (Exception e) {
    		fail("Exception trying to delete attachment: " + e);
    	}
    }
    
    /**
     * Just isolating my silly hack.
     * @param attachments
     * @return
     */
    private Attachment getFirstAttachment(Collection<Attachment> attachments) {
    	return (Attachment)attachments.toArray()[0];
    }

    private Attachment buildAttachment(int entryId) throws IOException
    {
    	File file = new File(TEST_FILE);
    	Attachment attachment = new Attachment();
    	attachment.setEntryId(entryId);
    	attachment.setFileType(Attachment.FileType.map);
    	attachment.setFileName(file.getName());
    	attachment.setBytes(getFileContents(file));
    	attachment.setMimeType("xml");
    	attachment.setTitle("test file");
    	
    	return attachment;
    }
    
    /** 
     * Retrieves the contents fo the file from the filesystem for testing.
     * @param fileName
     * @return
     */
    private byte[] getFileContents(File file) throws IOException
    {
    	assertTrue("Test file doesn't exist!", file.exists());
    	InputStream in = new FileInputStream(file);
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	byte[] buf = new byte[1024];
    	for (int read = in.read(buf); read > 0; read = in.read(buf)) {
    		out.write(buf, 0, read);
    	}
    	
    	return out.toByteArray();
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
