package net.kukido.blog.test.dataaccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.* ;
import static org.junit.Assert.* ;

import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.dataaccess.TagDao;
import net.kukido.blog.datamodel.Tag;

public class TagDaoTest
{
    private final List<Tag> TAGS = new ArrayList<Tag>(Arrays.asList(new Tag[] {
            new Tag("junit-foo"),
            new Tag("junit-bar"),
            new Tag("junit-zaz")
    }));
    
    @Before
    @After
    public void enforceNoTagsExist() {
        TagDao tagDao = new TagDao();
        for (Tag tag : TAGS) {
            try {
                Tag t = tagDao.findByName(tag.getName());
                tagDao.delete(t.getTagId());
            }
            catch (DataAccessException e) {
                continue;
            }
        }
    }
    
    @Test
    public void test_create_single() throws DataAccessException 
    {
        TagDao tagDao = new TagDao();
        Tag created = tagDao.create(TAGS.get(0));
        Tag retrieved = tagDao.findByName(created.getName());
        assertTrue(retrieved.equals(created));
        tagDao.delete(created.getTagId()); // Assume this works.  Tested later.
    }
    
    @Test 
    public void test_create_collection() throws DataAccessException
    {
        TagDao tagDao = new TagDao();
        Collection<Tag> created = tagDao.create(TAGS);
        assertTrue(sameTags(created, TAGS));
        for (Tag t : created) {
            tagDao.delete(t.getTagId()); // Assume this works.  Tested later.
        }
    }
    
    @Test
    public void test_findByName() throws DataAccessException
    {
        TagDao tagDao = new TagDao();
        Tag created = tagDao.create(TAGS.get(0));
        Tag tag = tagDao.findByName(created.getName());
        assertTrue(created.equals(tag));
    }
    
    @Test
    public void test_findByTagId() throws DataAccessException
    {
        TagDao tagDao = new TagDao();
        Tag created = tagDao.create(TAGS.get(0));
        Tag tag = tagDao.findByTagId(created.getTagId());
        assertTrue(created.equals(tag));
    }
    
    @Test
    public void test_update() throws DataAccessException
    {
        TagDao tagDao = new TagDao();
        Tag created = tagDao.create(TAGS.get(0));
        String newName = created.getName() + "-update";
        Tag update = new Tag(created.getTagId(), newName);
        tagDao.update(update);
        Tag retrieved = tagDao.findByTagId(created.getTagId());
        assertTrue(newName.equals(retrieved.getName()));
    }
    
    /**
     * Determines whether two Collections of Tag objects contain the
     * same Tags.
     * @param a
     * @param b
     * @return
     */
    private boolean sameTags(Collection<Tag> a, Collection<Tag> b) {
        List<String> aNames = new ArrayList<String>(a.size());
        for (Tag t : a) {
            aNames.add(t.getName());
        }
        List<String> bNames = new ArrayList<String>(b.size());
        for (Tag t : b) {
            bNames.add(t.getName());
        }
        return aNames.containsAll(bNames) && bNames.containsAll(aNames) && (aNames.size() == bNames.size());
    }

}
