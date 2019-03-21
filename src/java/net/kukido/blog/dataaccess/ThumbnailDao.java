package net.kukido.blog.dataaccess;

import net.kukido.blog.datamodel.*;

import java.sql.*;
import javax.sql.*;
import java.util.*;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.sql.*;

public class ThumbnailDao extends Dao
{
    static private final String CREATE_SQL =
	"insert into THUMBNAILS "
	+ "(Attachment_ID, File_Name, Max_Dimension, Bytes)"
	+ " values "
	+ "(:Attachment_ID"	// Attachment_ID
	+ ",:File_Name"		// File_Name
	+ ",:Max_Dimension"	// Max_Dimension
	+ ",:Bytes)";		// Bytes

    static private final String FIND_BY_ATTACHMENT_ID_SQL =
	"select * from THUMBNAILS where Attachment_ID = :Attachment_ID";

    static private final String DELETE_BY_ATTACHMENT_ID_SQL =
	"delete from THUMBNAILS where Attachment_ID = :Attachment_ID";

    static private final String FIND_THUMBNAIL_BY_NAME_MAX_DIMENSION_SQL =
	"select * from THUMBNAILS where File_Name = :File_Name and Max_Dimension = :Max_Dimension";

    public void create(Thumbnail thumbnail)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement create = null;
	try
	{
	    conn = getConnection();
	    create = new NamedParamStatement(conn, CREATE_SQL);
	    create.setInt("Attachment_ID", thumbnail.getAttachmentId());
	    create.setString("File_Name", thumbnail.getFileName());
	    create.setInt("Max_Dimension", thumbnail.getMaxDimension());
	    create.setBytes("Bytes", thumbnail.getBytes());
	    create.executeUpdate();
	}
	catch (Exception e)
	{
	    throw new DataAccessException("Unable to create thumbnail with name " + thumbnail.getFileName(), e);	    
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
	catch (Exception e)
	{
	    throw new DataAccessException("Unable to delete tumbnails for Attachment_ID " + attachmentId, e);	    
	}
	finally
	{
	    try { delete.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }
		
    public Thumbnail findByAttachment(Attachment attachment)
        throws DataAccessException
    {
        return findByAttachmentId(attachment.getAttachmentId());
    }
    
    public Thumbnail findByAttachmentId(int attachmentId) // File_Name is a KEY
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement findByName = null;
	ResultSet results = null;
	try
	{
	    conn = getConnection();
	    findByName = new NamedParamStatement(conn, FIND_BY_ATTACHMENT_ID_SQL);
	    // "select * from THUMBNAILS where File_Name = :File_Name";
	    findByName.setInt("Attachment_ID", attachmentId);
	    results = findByName.executeQuery();

	    if (!results.next()) throw new DataAccessException("Unable to find thumbnail with id " + attachmentId);

	    Thumbnail thumbnail = populateThumbnail(results);
	    return thumbnail;
	}
	catch (Exception e)
	{
	    throw new DataAccessException("Unable to find thumbnail with id " + attachmentId, e);
	}
	finally
	{
	    try { results.close(); } catch (Exception ignored) {}
	    try { findByName.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }

    public Thumbnail findByFileNameMaxWidth(String fileName, int maxWidth) // File_Name, Max_Dimension is a KEY
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement findByName = null;
	ResultSet results = null;
	try
	{
	    conn = getConnection();
	    findByName = new NamedParamStatement(conn, FIND_THUMBNAIL_BY_NAME_MAX_DIMENSION_SQL);
	    // "select * from THUMBNAILS where File_Name = :File_Name";
	    findByName.setString("File_Name", fileName);
	    findByName.setInt("Max_Dimension", maxWidth);
	    results = findByName.executeQuery();

	    if (!results.next()) throw new DataAccessException("Unable to find thumbnail with name " + fileName);

	    Thumbnail thumbnail = populateThumbnail(results);
	    return thumbnail;
	}
	catch (Exception e)
	{
	    throw new DataAccessException("Unable to find thumbnail with name " + fileName, e);
	}
	finally
	{
	    try { results.close(); } catch (Exception ignored) {}
	    try { findByName.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }

    private Thumbnail populateThumbnail(ResultSet results)
	throws SQLException
    {
	Thumbnail thumbnail = new Thumbnail();
	thumbnail.setAttachmentId(results.getInt("Attachment_ID"));
	thumbnail.setFileName(results.getString("File_Name"));
	thumbnail.setBytes(results.getBytes("Bytes"));
	return thumbnail;
    }
}
