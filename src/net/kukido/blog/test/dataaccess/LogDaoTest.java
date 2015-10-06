package net.kukido.blog.test.dataaccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.* ;
import static org.junit.Assert.* ;

import net.kukido.blog.datamodel.Attachment;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.blog.datamodel.Tag;
import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.dataaccess.LogDao;
import net.kukido.blog.dataaccess.TagDao;

public class LogDaoTest
{
    private final Date now = new Date();
    private Collection<Tag> tags = Arrays.asList(new Tag[] { 
            new Tag("foo"),
            new Tag("bar"),
            new Tag("zaz")
    });
    private int entryId; // For cleanup.
    
    @Before
    public void test_create() throws DataAccessException
    {
        LogDao logDao = new LogDao();
        this.tags = new TagDao().create(tags); // Tested in TagDaoTest.  Rely on its correct functioning here.
        
        LogEntry entry = buildLogEntry();
        
        LogEntry created = logDao.create(entry);
        entryId = created.getEntryId();
        
        // Make sure that the original 
        assertTrue(created.getAllowComments());
        assertTrue(created.getSyndicate());
        assertTrue("body".equals(created.getBody()));
        assertTrue(withinFiveMinutes(created.getDatePosted(), now));
        assertFalse(created.getEntryId() == -1); // Confirm that an ID was assigned.
        assertTrue("image-file-name".equals(created.getImageFileName()));
        //assertTrue(Attachment.FileType.document.equals(created.getImageFileType()));
        assertNull(created.getImageFileType());
        assertTrue("intro".equals(created.getIntro()));
        assertTrue(withinFiveMinutes(created.getLastUpdated(), now));
        assertTrue("title".equals(created.getTitle()));
        assertTrue(-1 == created.getUserId());
        assertTrue("junit".equals(created.getUserName()));
        assertTrue("via-text".equals(created.getViaText()));
        assertTrue("via-title".equals(created.getViaTitle()));
        assertTrue("via-url".equals(created.getViaUrl()));
        assertTrue(sameTags(created.getTags(), tags));
    }

	private LogEntry buildLogEntry() {
		LogEntry entry = new LogEntry();
        entry.setAllowComments(true);
        entry.setSyndicate(true);
        entry.setBody("body");
        entry.setDatePosted(now);
        entry.setEntryId(-1); // Confirm later that this got properly set.
        entry.setImageFileName("image-file-name");
        //entry.setImageFileType(Attachment.FileType.document);
        entry.setImageFileType(null);
        entry.setIntro("intro");
        entry.setLastUpdated(now);
        entry.setTitle("title");
        entry.setUserId(-1);
        entry.setUserName("junit");
        entry.setViaText("via-text");
        entry.setViaTitle("via-title");
        entry.setViaUrl("via-url");
        entry.setTags(tags);
		return entry;
	}
	
	@Test
    public void test_fileTypes_update() throws DataAccessException
    {
        LogDao logDao = new LogDao();
        Collection<String> types = Attachment.getFileTypeOptions();
        Collection<String> typesWithNull = new ArrayList<String>(types);
        typesWithNull.add(null);
        
        for (String fileType : typesWithNull) {
        	try {
		        LogEntry entry = logDao.findByEntryId(entryId);
		        entry.setImageFileType(fileType);
		        logDao.update(entry);
		        LogEntry updated = logDao.findByEntryId(entryId);
		        entryId = updated.getEntryId();
		        assertEquals(fileType, updated.getImageFileType());
        	}
        	catch (Exception e) {
        		fail("Error setting thumnail file type to " + fileType + ": " + e);
        	}
        }
    }
    
    @Test
    public void test_find_by_id() throws DataAccessException 
    {
        LogDao logDao = new LogDao();
        LogEntry created = logDao.findByEntryId(entryId);
        entryId = created.getEntryId();
        
        // Make sure that the original 
        assertTrue(created.getAllowComments());
        assertTrue("body".equals(created.getBody()));
        assertTrue(withinFiveMinutes(created.getDatePosted(), now));
        assertFalse(created.getEntryId() == -1); // Confirm that an ID was assigned.
        assertTrue("image-file-name".equals(created.getImageFileName()));
        //assertTrue(Attachment.FileType.document.equals(created.getImageFileType()));
        assertNull(created.getImageFileType());
        assertTrue("intro".equals(created.getIntro()));
        assertTrue(withinFiveMinutes(created.getLastUpdated(), now));
        assertTrue("title".equals(created.getTitle()));
        assertTrue(-1 == created.getUserId());
        assertTrue("junit".equals(created.getUserName()));
        assertTrue("via-text".equals(created.getViaText()));
        assertTrue("via-title".equals(created.getViaTitle()));
        assertTrue("via-url".equals(created.getViaUrl()));
        assertTrue(sameTags(created.getTags(), tags));
    }
    
    @Test
    public void test_update() throws DataAccessException
    {
        LogDao logDao = new LogDao();
        LogEntry created = logDao.findByEntryId(entryId);
        
        List<Tag> tempTags = new LinkedList<Tag>(tags);
        tempTags.remove(0); // Test tag removal
        tempTags.add(new Tag("update-tag")); // Test updating tags.
        Collection<Tag> updateTags = new TagDao().create(tempTags);

        // Double-check that the existing values aren't the same as the new
        // values we'll assign later.
        assertTrue(created.getAllowComments());
        assertTrue(created.getSyndicate());
        assertFalse("update-body".equals(created.getBody()));
        assertFalse("update-image-file-name".equals(created.getImageFileName()));
        assertFalse(Attachment.TYPE_IMAGE.equals(created.getImageFileType()));
        assertFalse("update-intro".equals(created.getIntro()));
        assertFalse("update-title".equals(created.getTitle()));
        assertFalse(-3 == created.getUserId());
        assertFalse("update-junit".equals(created.getUserName()));
        assertFalse("update-via-text".equals(created.getViaText()));
        assertFalse("update-via-title".equals(created.getViaTitle()));
        assertFalse("update-via-url".equals(created.getViaUrl()));
        assertFalse(sameTags(created.getTags(), updateTags));
        
        
        created.setAllowComments(false);
        created.setSyndicate(false);
        created.setBody("update-body");
        created.setImageFileName("update-image-file-name");
        created.setImageFileType(Attachment.TYPE_IMAGE);
        created.setIntro("update-intro");
        created.setTitle("update-title");
        created.setUserId(-3);
        created.setUserName("update-junit");
        created.setViaText("update-via-text");
        created.setViaTitle("update-via-title");
        created.setViaUrl("update-via-url");
        created.setTags(updateTags);
        
        logDao.update(created);
        
        LogEntry updated = logDao.findByEntryId(created.getEntryId());
        assertFalse(updated.getAllowComments());
        assertFalse(updated.getSyndicate());
        assertTrue("update-body".equals(updated.getBody()));
        assertTrue(withinFiveMinutes(updated.getDatePosted(), now));
        assertFalse(updated.getEntryId() == -1); // Confirm that an ID was assigned.
        assertTrue("update-image-file-name".equals(updated.getImageFileName()));
        assertTrue(Attachment.TYPE_IMAGE.equals(updated.getImageFileType()));
        assertTrue("update-intro".equals(updated.getIntro()));
        assertTrue(withinFiveMinutes(updated.getLastUpdated(), now));
        assertTrue("update-title".equals(updated.getTitle()));
        assertTrue(-3 == updated.getUserId());
        assertTrue("update-junit".equals(updated.getUserName()));
        assertTrue("update-via-text".equals(updated.getViaText()));
        assertTrue("update-via-title".equals(updated.getViaTitle()));
        assertTrue("update-via-url".equals(updated.getViaUrl()));
        assertTrue(sameTags(updated.getTags(), updateTags));
    }
    
    /**
     * 
     * @throws DataAccessException
     */
    @Test
    public void test_encoding() throws DataAccessException
    {
        LogDao logDao = new LogDao();
        LogEntry entry = buildLogEntry();
        entry.setTitle("La Petite Trotte à Léon?");
        entry.setBody("“an enlarged tour around Mont Blanc.”");
        
        LogEntry created = logDao.create(entry);
        entryId = created.getEntryId();
        
        // Make sure that the original 
        assertTrue(created.getAllowComments());
        assertTrue(entry.getBody().equals(created.getBody()));
        assertTrue(withinFiveMinutes(created.getDatePosted(), now));
        assertFalse(created.getEntryId() == -1); // Confirm that an ID was assigned.
        assertTrue("image-file-name".equals(created.getImageFileName()));
        //assertTrue(Attachment.FileType.document.equals(created.getImageFileType()));
        assertNull(created.getImageFileType());
        assertTrue("intro".equals(created.getIntro()));
        assertTrue(withinFiveMinutes(created.getLastUpdated(), now));
        assertTrue(entry.getTitle().equals(created.getTitle()));
        assertTrue(-1 == created.getUserId());
        assertTrue("junit".equals(created.getUserName()));
        assertTrue("via-text".equals(created.getViaText()));
        assertTrue("via-title".equals(created.getViaTitle()));
        assertTrue("via-url".equals(created.getViaUrl()));
        assertTrue(sameTags(created.getTags(), tags));
    }
    
    @After
    public void test_delete() throws DataAccessException
    {
        LogDao dao = new LogDao();
        dao.delete(entryId);
        
        try { 
            dao.findByEntryId(entryId);
            assert(false); // There should be no entry with the given ID, and the DAO should throw an error.
        }
        catch (DataAccessException e) {
            // All is well.  Do nothing.
        }
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
}
