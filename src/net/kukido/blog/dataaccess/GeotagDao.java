/*
 * GeotagDao.java
 *
 * Created on October 23, 2007, 7:48 PM
 */

package net.kukido.blog.dataaccess;

import net.kukido.blog.datamodel.*;
import net.kukido.sql.NamedParamStatement;
import java.sql.*;
import java.util.*;


/**
 *
 * @author  craser
 */
public class GeotagDao extends Dao
{
    static private final String CREATE_SQL = 
        "insert into GEOTAGS (Map_ID, Attachment_ID, Date_Tagged, Timestamp, Latitude"
        + ",Longitude, Elevation)"
        + " values "
        + "(:Map_ID"
        + ",:Attachment_ID"
        + ",:Date_Tagged"
        + ",:Timestamp"
        + ",:Latitude"
        + ",:Longitude"
        + ",:Elevation)";
    
    static private final String FIND_BY_TAG_ID_SQL = 
        "select * from GEOTAGS where Tag_ID = :Tag_ID";
    
    static private final String FIND_BY_BOUNDS_SQL = 
        "select * from GEOTAGS"
        + " where Latitude >= :Min_Latitude"
        + " and Latitude <= :Max_Latitude"
        + " and Longitude >= :Min_Longitude"
        + " and Longitude <= :Max_Longitude";
    
    static private final String FIND_BY_FILE_TYPE_SQL = 
        "select GEOTAGS.* from GEOTAGS, ATTACHMENTS"
        + " where GEOTAGS.Attachment_ID = ATTACHMENTS.Attachment_ID"
        + " and ATTACHMENTS.File_Type = :File_Type";
    
    static private final String FIND_BY_ATTACHMENT_SQL =
        "select * from GEOTAGS where Attachment_ID = :Attachment_ID";
    
    static private final String FIND_BY_ATTACHMENT_IDS_SQL = 
        "select * from GEOTAGS where Attachment_ID in "; // Note incomplete SQL!
   
    static private final String DELETE_SQL = 
        "delete from GEOTAGS where Tag_ID = :Tag_ID";
    
    static private final String DELETE_BY_ATTACHMENT_ID_SQL =
        "delete from GEOTAGS where Attachment_ID = :Attachment_ID";
        
    
    public Geotag create(Geotag geotag)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement create = null;
        try
        {
            conn = getConnection();
            create = new NamedParamStatement(conn, CREATE_SQL);
            create.setInt("Map_ID", geotag.getMapId());
            create.setInt("Attachment_ID", geotag.getAttachmentId());
            create.setTimestamp("Date_Tagged", geotag.getDateTagged());
            create.setTimestamp("Timestamp", geotag.getTimestamp());
            create.setFloat("Latitude", geotag.getLatitude());
            create.setFloat("Longitude", geotag.getLongitude());
            create.setFloat("Elevation", geotag.getElevation());
            create.executeUpdate();
            
            return findByTagId(getLastCreatedId(conn));
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to create geotag: " + e.getMessage(), e);
        }
        finally
        {
            try { create.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public void deleteByAttachmentId(int attachmentId)
        throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement delete = null;
	try 
        {           
	    conn = getConnection();
	    delete = new NamedParamStatement(conn, DELETE_BY_ATTACHMENT_ID_SQL);
	    delete.setInt("Attachment_ID", attachmentId);
	    delete.executeUpdate();
	}
	catch (SQLException e) {
	    throw new DataAccessException("Caught while deleting geotag with Attachment_ID " + attachmentId, e);
	}
	finally {
	    try { delete.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }
    
    public void delete(Geotag tag)
        throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement delete = null;
	try 
        {           
	    conn = getConnection();
	    delete = new NamedParamStatement(conn, DELETE_SQL);
	    delete.setInt("Tag_ID", tag.getTagId());
	    delete.executeUpdate();
	}
	catch (SQLException e) {
	    throw new DataAccessException("Caught while deleting geotag with ID " + tag.getTagId(), e);
	}
	finally {
	    try { delete.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }
    
    public Geotag findByTagId(int tagId) 
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try 
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_TAG_ID_SQL);
            find.setInt("Tag_ID", tagId);
            rs = find.executeQuery();
            
            if (!rs.next()) { throw new DataAccessException("No geotag found with ID " + tagId); }
            Geotag geotag = populateGeotag(rs);
            return geotag;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to get geotag with ID " + tagId, e);
        }
        finally
        {
            try { rs.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Collection<Geotag> findByBounds(float minLat, float maxLat, float minLon, float maxLon)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_BOUNDS_SQL);
            find.setFloat("Min_Latitude", minLat);
            find.setFloat("Max_Latitude", maxLat);
            find.setFloat("Min_Longitude", minLon);
            find.setFloat("Max_Longitude", maxLon);
            rs = find.executeQuery();
            
            List<Geotag> geotags = new LinkedList<Geotag>();
            while (rs.next()) {
                Geotag geotag = populateGeotag(rs);
                geotags.add(geotag);
            }
            
            return geotags;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to find geotags by range lat(" + minLon + "," + maxLon + ") lon(" + minLon + "," + maxLon + ")", e);
        }
        finally
        {
            try { rs.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }   
    }
    
    public Collection<Geotag> findByFileType(Attachment.FileType fileType)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_FILE_TYPE_SQL);
            find.setString("File_Type", fileType.toString());
            rs = find.executeQuery();
            
            Collection<Geotag> geotags = new ArrayList<Geotag>();
            while (rs.next()) {
                Geotag geotag = populateGeotag(rs);
                geotags.add(geotag);
            }
            
            return geotags;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to find geotags by file type \"" + fileType + "\"", e);
        }
        finally
        {
            try { rs.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }  
        
    }
    
    public Map<Integer, Geotag> findByAttachmentIds(Collection<Integer> attachmentIds)
        throws DataAccessException
    {
        Connection conn = null;
        PreparedStatement find = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            String sql = FIND_BY_ATTACHMENT_IDS_SQL + buildParamList(attachmentIds.size());
            find = conn.prepareStatement(sql);
            int p = 1; // Parameter index
            for (Iterator<Integer> i = attachmentIds.iterator(); i.hasNext(); p++) {
                Integer id = (Integer)i.next();
                find.setInt(p, id.intValue());
            }
            
            rs = find.executeQuery();
            Map<Integer, Geotag> geotagsByAttachmentId = new HashMap<Integer, Geotag>();
            while (rs.next()) {
                int id = rs.getInt("Attachment_ID");
                Geotag tag = populateGeotag(rs);
                geotagsByAttachmentId.put(new Integer(id), tag);
            }
            return geotagsByAttachmentId;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to find geotags.", e);
        }
        finally
        {
            try { rs.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        } 
    }
    
    public Collection<Geotag> findByAttachmentId(int attachmentId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_ATTACHMENT_SQL);
            find.setInt("Attachment_ID", attachmentId);
            rs = find.executeQuery();
            
            Collection<Geotag> geotags = new ArrayList<Geotag>();
            while (rs.next()) {
                Geotag geotag = populateGeotag(rs);
                geotags.add(geotag);
            }
            
            return geotags;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to find geotags by attachment id " + attachmentId, e);
        }
        finally
        {
            try { rs.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }   
    }
    
    /**
     *    private int tagId;
    private int mapId; // Attachment_ID for GPX map used to generate this tag, if appropriate.
    private int attachmentId; // Attachment this tag locates.
    private Date dateTagged;
    private float latitude;
    private float longitude;
    private float elevation;
     *
     */
    private Geotag populateGeotag(ResultSet rs)
        throws SQLException
    {
        Geotag g = new Geotag();
        g.setTagId(rs.getInt("Tag_ID"));
        g.setMapId(rs.getInt("Map_ID"));
        g.setAttachmentId(rs.getInt("Attachment_ID"));
        g.setDateTagged(rs.getTimestamp("Date_Tagged"));
        g.setTimestamp(rs.getTimestamp("Timestamp"));
        g.setLatitude(rs.getFloat("Latitude"));
        g.setLongitude(rs.getFloat("Longitude"));
        g.setElevation(rs.getFloat("Elevation"));
        
        return g;
    }
    
    
    
}
