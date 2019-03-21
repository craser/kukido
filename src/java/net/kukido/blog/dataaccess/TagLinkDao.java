/*
 * TagLinkDao.java
 *
 * Created on August 7, 2005, 12:19 PM
 */

package net.kukido.blog.dataaccess;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import net.kukido.blog.datamodel.*;
import net.kukido.sql.*;

/**
 *
 * @author  craser
 */
public class TagLinkDao extends Dao
{
    static private final String CREATE_TAG_LINK =
        "insert into TAG_LINKS (Object_ID, Tag_ID, Date_Tagged) values (:Object_ID, :Tag_ID, :Date_Tagged)";
    
    static private final String FIND_TAG =
        "select * from TAG_LINKS where Object_ID = :Object_ID and Tag_ID = :Tag_ID";
    
    static private final String FIND_RECENT_TAGS =
        "select distinct Tag_ID from TAG_LINKS order by Date_Tagged desc limit :Num_Tags";
    
    static private final String FIND_MOST_USED_TAGS =
        "select Tag_ID, count(*) Num_Links from TAG_LINKS"
        + " group by Tag_ID order by Num_Links desc"
        + " limit :Num_Tags";
    
    static private final String FIND_BY_OBJECT_ID =
        "select * from TAG_LINKS where Object_ID = :Object_ID";
    
    static private final String FIND_BY_TAG_ID = 
        "select * from TAG_LINKS where Tag_ID = :Tag_ID";
    
    static private final String DELETE_TAG_LINK = 
        "delete from TAG_LINKS where Object_ID = :Object_ID and Tag_ID = :Tag_ID";
    
    static private final String DELETE_BY_OBJECT_ID =
        "delete from TAG_LINKS where Object_ID = :Object_ID";
    
    static private final String DELETE_BY_TAG_ID = 
        "delete from TAG_LINKS where Tag_ID = :Tag_ID";
    
    
    /** Creates a new instance of TagLinkDao */
    public TagLinkDao() {
    }
    
    /**
     * Sets the tags for a given database object to be the given set, and
     * only the given set.
     * @param objectId the database record to be "tagged"
     * @param tags a Collection of Tags.
     */
    public void assignTags(int objectId, Collection tags)
        throws DataAccessException
    {
        Connection conn = null;
        try
        {
            conn = getConnection();
            TagDao tagDao = new TagDao();
            for (Iterator i = tags.iterator(); i.hasNext(); )
            {
                Tag tag = (Tag)i.next();
                tag = tagDao.create(tag); // Ensure that the tag exists.
                createLink(conn, objectId, tag);
            }
            
            Collection obsoleteTags = findTagsByObjectId(objectId);
            obsoleteTags.removeAll(tags);
            for (Iterator i = obsoleteTags.iterator(); i.hasNext(); )
            {
                Tag tag = (Tag)i.next();
                deleteLink(conn, objectId, tag);
            }            
        }
        finally
        {
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public void createLink(int objectId, Tag tag)
        throws DataAccessException
    {
        Connection conn = null;
        try
        {
            conn = getConnection();
            createLink(conn, objectId, tag);
        }
        finally
        {
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    /**
     * Creates link from database object to tag IFF link doesn't 
     * already exist.
     */
    protected void createLink(Connection conn, int objectId, Tag tag)
        throws DataAccessException
    {
        NamedParamStatement create = null;
        try
        {
            if (!linkExists(conn, objectId, tag.getTagId()))
            {
                Timestamp now = new Timestamp(new java.util.Date().getTime());
                create = new NamedParamStatement(conn, CREATE_TAG_LINK);
                create.setInt("Object_ID", objectId);
                create.setInt("Tag_ID", tag.getTagId());
                create.setTimestamp("Date_Tagged", now);
                create.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to create link from object " + objectId + " to tag " + tag, e);
        }
        finally
        {
            try { create.close(); } catch (Exception ignored) {}
        }
    }
    
    private boolean linkExists(Connection conn, int objectId, int tagId)
        throws DataAccessException
    {
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            find = new NamedParamStatement(conn, FIND_TAG);
            find.setInt("Object_ID", objectId);
            find.setInt("Tag_ID", tagId);
            results = find.executeQuery();
            return results.next();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to determine if link [" + objectId + " --> " + tagId + "]", e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
        }
    }
    
    /**
     * @return Collection of Tags
     */
    public Collection findRecentTags(int numTags)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_RECENT_TAGS);
            find.setInt("Num_Tags", numTags);
            results = find.executeQuery();
            
            Collection tagIds = new ArrayList();
            while (results.next()) {
                tagIds.add(new Integer(results.getInt("Tag_ID")));
            }
            
            return new TagDao().findByTagIds(tagIds);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Exception while retrieving recent tags.", e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
            try { conn.close(); }    catch (Exception ignored) {}
        }
    }
    
    /**
     * @return Collection of Tags
     */
    public Collection findMostUsedTags(int numTags)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_MOST_USED_TAGS);
            find.setInt("Num_Tags", numTags);
            results = find.executeQuery();
            
            Collection tagIds = new ArrayList();
            while (results.next()) {
                tagIds.add(new Integer(results.getInt("Tag_ID")));
            }
            
            return new TagDao().findByTagIds(tagIds);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Exception while retrieving most-used tags.", e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
            try { conn.close(); }    catch (Exception ignored) {}
        }
    }
    
    /**
     * @return Collection of Tags
     */
    public Collection findTagsByObjectId(int objectId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_OBJECT_ID);
            find.setInt("Object_ID", objectId);
            results = find.executeQuery();
            
            Collection tagIds = new ArrayList();
            while (results.next()) {
                tagIds.add(new Integer(results.getInt("Tag_ID")));
            }
            
            return new TagDao().findByTagIds(tagIds);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to find tags for object id " + objectId, e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
            try { conn.close(); }    catch (Exception ignored) {}
        }
    }
    
    /**
     * Finds the objects which match at least 'minMatches' tags in the given collection,
     * sorting them by the number of tags matched.  The 'start' and 'numObjects' define
     * the slice of the result set to include in the returned Collection.
     *
     * @param tags The Collection of Tag objects to match against
     * @param minMatches The minimum number of tags matched for a given object to be included.
     *
     * @return a Collection of Integer objects, reflecting the Object_ID field.
     */
    public Collection findLinksByTagSet(Collection tags, int minMatches)
        throws DataAccessException
    {
        Connection conn = null;
        Statement find = null;
        ResultSet results = null;
        try
        {
            StringBuffer sql = new StringBuffer();
            sql.append("select Object_ID, count(*) Num_Matched from TAG_LINKS");
            sql.append(" where Tag_ID in (");
            
            for (Iterator i = tags.iterator(); i.hasNext(); )
            {
                sql.append(((Tag)i.next()).getTagId());
                if (i.hasNext()) sql.append(",");
            }
            
            sql.append(")");
            sql.append(" group by Object_ID");
            sql.append(" having Num_Matched >= " + minMatches);
            sql.append(" order by Num_Matched desc");
            //sql.append(" limit " + start + "," + numObjects);
            
            conn = getConnection();
            find = conn.createStatement();
            results = find.executeQuery(sql.toString());
            
            Collection objectIds = new LinkedList();
            while (results.next())
                objectIds.add(new Integer(results.getInt("Object_ID")));
            
            return objectIds;            
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to find objects for tag set.", e);
        }
        finally
        {
            try { results.close(); } catch (Exception e) {}
            try { find.close(); }    catch (Exception e) {}
            try { conn.close(); }    catch (Exception e) {}
        }
    }
    
    public void deleteLink(int objectId, Tag tag)
        throws DataAccessException
    {
        Connection conn = null;
        try
        {
            conn = getConnection();
            deleteLink(conn, objectId, tag);
        }
        finally
        {
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public void deleteLink(Connection conn, int objectId, Tag tag)
        throws DataAccessException
    {
        NamedParamStatement delete = null;
        try
        {
            delete = new NamedParamStatement(conn, DELETE_TAG_LINK);
            delete.setInt("Object_ID", objectId);
            delete.setInt("Tag_ID", tag.getTagId());
            delete.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to delete link from object ID " + objectId + " to tag " + tag, e);
        }
        finally
        {
            try { delete.close(); } catch (Exception ignored) {}
        }
    }
    
    public void deleteByObjectId(int objectId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement delete = null;
        try
        {
            conn = getConnection();
            delete = new NamedParamStatement(conn, DELETE_BY_OBJECT_ID);
            delete.setInt("Object_ID", objectId);
            delete.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to delete tag links for object id " + objectId, e);
        }
        finally
        {
            try { delete.close(); } catch (Exception ignored) {}
            try { conn.close(); }   catch (Exception ignored) {}
        }
    }
    
    
    private TagLink populateTagLink(ResultSet results)
        throws SQLException, DataAccessException
    {
        TagLink link = new TagLink();
        link.setObjectId(results.getInt("Object_ID"));
        link.setDateTagged(results.getTimestamp("Date_Tagged"));
        
        int tagId = results.getInt("Tag_ID");
        Tag tag = new TagDao().findByTagId(tagId);
        link.setTag(tag);
        
        return link;
    }
    
    private Collection createTagLinks(int objectId, Collection tags)
    {
        Collection links = new ArrayList();
        for (Iterator i = tags.iterator(); i.hasNext(); )
        {
            TagLink link = new TagLink();
            link.setObjectId(objectId);
            link.setTag((Tag)i.next());
            links.add(link);
        }
        return links;
    }
    
}
