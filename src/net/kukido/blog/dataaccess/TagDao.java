/*
 * TagDao.java
 *
 * Created on August 7, 2005, 11:37 AM
 */

package net.kukido.blog.dataaccess;

import java.sql.*;
import java.util.*;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.util.BucketMap;
import net.kukido.sql.*;
/**
 *
 * @author  craser
 */
public class TagDao extends Dao 
{
    static private final String CREATE_TAG_SQL = 
        "insert into TAGS (Name) values (:Name)";
    
    static private final String FIND_ALL_TAGS =
        "select * from TAGS";
    
    static private final String FIND_BY_TAG_ID =
        "select * from TAGS where Tag_ID = :Tag_ID";
    
    static private final String FIND_BY_TAG_IDS = 
        "select * from TAGS where Tag_ID in "; // NOTE INVALID SQL!
    
    static private final String FIND_BY_TAG_NAME =
        "select * from TAGS where Name = :Name";
    
    static private final String FIND_BY_OBJECT_IDS =
        "select TAG_LINKS.Object_ID, TAGS.* from TAG_LINKS, TAGS"
        + " where TAGS.Tag_ID = TAG_LINKS.Tag_ID"
        + " and TAG_LINKS.Object_ID in "; // NOTE INCOMPLETE SQL!
    
    static private final String UPDATE_TAG =
        "update TAGS set Name = :Name where Tag_ID = :Tag_ID";
    
    static private final String DELETE_TAG =
        "delete from TAGS where Tag_ID = :Tag_ID";

    /**
     * Creates all tags in the given collection.
     * @param tags a Collection of Tags
     */
    public Collection<Tag> create(Collection<Tag> tags)
        throws DataAccessException
    {
        Collection<Tag> newTags = new ArrayList<Tag>();
        for (Tag tag : tags) {
            newTags.add(create(tag));
        }
        
        return newTags;
    }
    
    /**
     * Creates a tag IFF it does not already exist in the database.
     */
    public Tag create(Tag tag)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement create = null;
        try
        {
            Tag newTag = tag;
            
            conn = getConnection();
            if (!tagExists(conn, tag))
            {
                create = new NamedParamStatement(conn, CREATE_TAG_SQL);
                create.setString("Name", tag.getName());
                create.executeUpdate();
            }
            
            newTag = findByName(tag.getName());
            
            return newTag;
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to create tag \"" + tag + "\"", e);
        }
        finally
        {
            try { create.close(); } catch (Exception e) {}
            try { conn.close(); } catch (Exception e) {}
        }
    }
    
    private boolean tagExists(Connection conn, Tag tag)
        throws DataAccessException
    {
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            find = new NamedParamStatement(conn, FIND_BY_TAG_NAME);
            find.setString("Name", tag.getName());
            results = find.executeQuery();
            return results.next();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to determine whether tag \"" + tag + "\" exists.", e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
        }
    }
    
    public Collection<Tag> find(SearchForm form)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try
        {
            List<String> criteria = new ArrayList<String>();
            Map<String, String> params = new HashMap<String, String>();
            
            if (form.getSearchTerm() != null)
            {
                criteria.add("TAGS.Name like :SearchTerm");
                params.put("SearchTerm", "%" + form.getSearchTerm() + "%");
            }
            
            StringBuffer sql = new StringBuffer("select * from TAGS");
            if (!criteria.isEmpty())
            {
                sql.append(" where" );
                for (Iterator<String> i = criteria.iterator(); i.hasNext(); ) {
                    sql.append((String)i.next());
                    if (i.hasNext()) { sql.append(" and "); }
                }
            }
            
            conn = getConnection();
            find = new NamedParamStatement(conn, sql.toString());
            find.bindAll(params);
            rs = find.executeQuery();
            
            Collection<Tag> tags = new ArrayList<Tag>();
            while (rs.next()) {
                Tag tag = populateTag(rs);
                tags.add(tag);
            }
            
            return tags;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Caught while retrieving tags per search form.", e);
        }
        finally
        {
            try { rs.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}            
        }
    }
    
    public Collection<Tag> findAllTags()
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet tags = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_ALL_TAGS);
            tags = find.executeQuery();
            
            Collection<Tag> allTags = new LinkedList<Tag>();
            while (tags.next()) {
                allTags.add(populateTag(tags));
            }
            return allTags;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Error while retrieving all tags.", e);
        }
        finally
        {
            try { tags.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    
    public Tag findByTagId(int tagId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet tags = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_TAG_ID);
            find.setInt("Tag_ID", tagId);
            tags = find.executeQuery();
            
            if (!tags.next())
                throw new DataAccessException("No tag exists with id " + tagId);
            
            return populateTag(tags);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to find Tag with id " + tagId, e);
        }
        finally
        {
            try { tags.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Collection<Tag> findByTagIds(Collection<Integer> tagIds)
        throws DataAccessException
    {
        // Short-circuit if tagIds is empty.
        if (tagIds.isEmpty()) {
            return new ArrayList<Tag>(0);
        }
        
        Connection conn = null;
        PreparedStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            String sql = FIND_BY_TAG_IDS + buildParamList(tagIds.size());
            find = conn.prepareStatement(sql);
            
            int paramIndex = 1;
            for (Integer tagId : tagIds) {
                find.setInt(paramIndex, tagId.intValue());
            }
            
            results = find.executeQuery();
                
            Collection<Tag> tags = new ArrayList<Tag>();
            while (results.next()) {
                tags.add(populateTag(results));
            }
            
            return tags;
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Unable to find tags.", e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
            try { conn.close(); }    catch (Exception ignored) {}
        }
    }
    
    public Tag findByName(String name)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet tags = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_TAG_NAME);
            find.setString("Name", name);
            tags = find.executeQuery();
            
            if (!tags.next())
                throw new DataAccessException("No tag exists with name \"" + name + "\"");
            
            return populateTag(tags);
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to find Tag with name \"" + name + "\"", e);
        }
        finally
        {
            try { tags.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
 
    public Collection<Tag> findByNames(Collection<String> names)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_TAG_NAME);
            Collection<Tag> tags = new ArrayList<Tag>();
            
            for (String name : names) {
                find.setString("Name", name);
                results = find.executeQuery();
                if (!results.next()) throw new DataAccessException("No tag exists with name \"" + name + "\"");
                tags.add(populateTag(results));
            }
            
            return tags;
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Unable to find tags.", e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
            try { conn.close(); }    catch (Exception ignored) {}
        }
    }
    
    public Map findByObjectIds(Collection ids)
        throws DataAccessException
    {  
        if (ids.isEmpty()) { return new BucketMap(); }
        
        Connection conn = null;
        PreparedStatement find = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            String sql = FIND_BY_OBJECT_IDS + buildParamList(ids.size());
            find = conn.prepareStatement(sql);
            int p = 1;
            for (Iterator i = ids.iterator(); i.hasNext(); p++) {
                int id = ((Integer)i.next()).intValue();
                find.setInt(p, id);
            }
            rs = find.executeQuery();
            
            BucketMap map = new BucketMap(); // Integer id --> List Tags
            while (rs.next()) {
                Integer id = new Integer(rs.getInt("Object_ID"));
                Tag tag = populateTag(rs);
                map.put(id, tag);
            }
            
            return map;
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to find tags with Object_ID " + ids, e);
        }
        finally
        {
            try { rs.close(); }      catch (Exception ignored) {}
            try { find.close(); }    catch (Exception ignored) {}
            try { conn.close(); }    catch (Exception ignored) {}
        }
            
    }
    
    public void update(Tag tag)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement update = null;
        try
        {
            conn = getConnection();
            update = new NamedParamStatement(conn, UPDATE_TAG);
            update.setString("Name", tag.getName());
            update.executeUpdate();            
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Unable to update tag " + tag, e);
        }
        finally
        {
            try { update.close(); } catch (Exception ignored) {}
            try { conn.close(); }   catch (Exception ignored) {}
        }
    }
    
    public void delete(int tagId)
        throws DataAccessException
    {
        Connection conn = null;
        try
        {
            conn = getConnection();
            delete(conn, tagId);
        }
        finally
        {
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public void delete(Connection conn, int tagId)
        throws DataAccessException
    {
        NamedParamStatement delete = null;
        try
        {
            delete = new NamedParamStatement(conn, DELETE_TAG);
            delete.setInt("Tag_ID", tagId);
            delete.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Unable to delete tag with id " + tagId, e);
        }
        finally
        {
            try { delete.close(); } catch (Exception ignored) {}
        }
    }
    
    private Tag populateTag(ResultSet results)
        throws SQLException
    {
        Tag tag = new Tag();
        tag.setTagId(results.getInt("Tag_ID"));
        tag.setName(results.getString("Name"));
        return tag;
    }
}
