package net.kukido.blog.test.dataaccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.* ;
import static org.junit.Assert.* ;

import net.kukido.blog.datamodel.Attachment;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.blog.datamodel.Tag;
import net.kukido.blog.dataaccess.AttachmentDao;
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
        
        LogEntry entry = new LogEntry();
        entry.setAllowComments(true);
        entry.setBody("body");
        entry.setDatePosted(now);
        entry.setEntryId(-1); // Confirm later that this got properly set.
        entry.setImageFileName("image-file-name");
        entry.setImageFileType(Attachment.TYPE_DOCUMENT);
        entry.setIntro("intro");
        entry.setLastUpdated(now);
        entry.setTitle("title");
        entry.setUserId(-1);
        entry.setUserName("junit");
        entry.setViaText("via-text");
        entry.setViaTitle("via-title");
        entry.setViaUrl("via-url");
        entry.setTags(tags);
        
        LogEntry created = logDao.create(entry);
        entryId = created.getEntryId();
        
        // Make sure that the original 
        assertTrue(created.getAllowComments());
        assertTrue("body".equals(created.getBody()));
        assertTrue(withinFiveMinutes(created.getDatePosted(), now));
        assertFalse(created.getEntryId() == -1); // Confirm that an ID was assigned.
        assertTrue("image-file-name".equals(created.getImageFileName()));
        assertTrue(Attachment.TYPE_DOCUMENT.equals(created.getImageFileType()));
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
        assertTrue(Attachment.TYPE_DOCUMENT.equals(created.getImageFileType()));
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
    
    @After
    public void test_delete() throws DataAccessException
    {
        System.out.println("test_delete()");
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
