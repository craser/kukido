package net.kukido.blog.dataaccess;

import net.kukido.blog.datamodel.*;

import java.sql.*;
import java.text.MessageFormat;
import javax.sql.*;
import javax.xml.crypto.Data;
import java.util.*;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.util.BucketMap;
import net.kukido.sql.*;
import net.kukido.blog.util.ImageTools;

public class AttachmentDao extends Dao {
	static private final String CREATE_SQL = "insert into ATTACHMENTS "
			+ "(Entry_ID, Is_Gallery_Image, File_Name, Activity_ID, Mime_Type, File_Type, User_ID, User_Name, Date_Posted, Title, Description, Date_Taken, Bytes)"
			+ " values " + "(:Entry_ID" // Entry_ID
			+ ",:Is_Gallery_Image" // Is_Gallery_Image
			+ ",:File_Name" // File_Name
			+ ",:Activity_ID" // Activity_ID
			+ ",:Mime_Type" // Mime_Type
			+ ",:File_Type" + ",:User_ID" // User_ID
			+ ",:User_Name" // User_Name
			+ ",:Date_Posted" // Date_Posted
			+ ",:Title" // Title
			+ ",:Description" // Description
			+ ",:Date_Taken" // Date_Taken
			+ ",:Bytes)"; // Bytes

	static private final String UPDATE_SQL = "update ATTACHMENTS set"
			+ " Entry_ID = :Entry_ID" // Entry_ID
			+ ",Is_Gallery_Image = :Is_Gallery_Image" // Is_Gallery_Image
			+ ",File_Name = :File_Name" // File_Name
			+ ",Activity_ID = :Activity_ID" // Activity_ID
			+ ",Mime_Type = :Mime_Type" // Mime_Type
			+ ",File_Type = :File_Type" + ",User_ID = :User_ID" // User_ID
			+ ",User_Name = :User_Name" // User_Name
			+ ",Date_Posted = :Date_Posted" // Date_Posted
			+ ",Title = :Title" // Title
			+ ",Description = :Description" // Description
			+ ",Date_Taken = :Date_Taken" // Date_Taken
			// + ",Bytes = :Bytes" // Bytes
			+ " where Attachment_ID = :Attachment_ID"; // Attachment_ID
	        
    static private final String UPDATE_BYTES_SQL = "update ATTACHMENTS set"
            + " Bytes = :Bytes" // Bytes
            + " where Attachment_ID = :Attachment_ID"; // Attachment_ID

	static private final String FIND_BY_FILE_TYPE_SQL = "select"
			+ " Attachment_ID" + ",Entry_ID" + ",Is_Gallery_Image"
			+ ",File_Name" + ",Activity_ID" + ",Mime_Type" + ",File_Type" + ",User_ID"
			+ ",User_Name" + ",Date_Posted" + ",Title" + ",Description"
			+ ",Date_Taken" + " from ATTACHMENTS where File_Type = :File_Type";

	static private final String FIND_BY_FILE_NAME_SQL = "select"
			+ " Attachment_ID" + ",Entry_ID" + ",Is_Gallery_Image"
			+ ",File_Name" + ",Activity_ID" + ",Mime_Type" + ",File_Type" + ",User_ID"
			+ ",User_Name" + ",Date_Posted" + ",Title" + ",Description"
			+ ",Date_Taken" + " from ATTACHMENTS where File_Name = :File_Name";
	
	static private final String HAS_BACKUP_SQL = "select"
			+ " count(*) as Num_Backups"
			+ " from ATTACHMENTS"
			+ " where File_Name = :File_Name";
			//+ " and File_Type = 'backup'";

	static private final String FIND_BY_ENTRY_ID_SQL = "select"
			+ " Attachment_ID"
			+ ",Entry_ID"
			+ ",Is_Gallery_Image"
			+ ",File_Name"
			+ ",Activity_ID"
			+ ",Mime_Type"
			+ ",File_Type"
			+ ",User_ID"
			+ ",User_Name"
			+ ",Date_Posted"
			+ ",Title"
			+ ",Description"
			+ ",Date_Taken"
			+ " from ATTACHMENTS where Entry_ID = :Entry_ID order by Date_Taken, Date_Posted asc";

	static private final String FIND_BY_ENTRY_IDS_SQL_FORMAT = "select"
			+ " Attachment_ID"
			+ ",Entry_ID"
			+ ",Is_Gallery_Image"
			+ ",File_Name"
			+ ",Activity_ID"
			+ ",Mime_Type"
			+ ",File_Type"
			+ ",User_ID"
			+ ",User_Name"
			+ ",Date_Posted"
			+ ",Title"
			+ ",Description"
			+ ",Date_Taken"
			+ " from ATTACHMENTS where Entry_ID in {0} order by Date_Taken, Date_Posted asc";

	static private final String FIND_BY_ATTACHMENT_ID_SQL = "select"
			+ " Attachment_ID" + ",Entry_ID" + ",Is_Gallery_Image"
			+ ",File_Name" + ",Activity_ID" + ",Mime_Type" + ",File_Type" + ",User_ID"
			+ ",User_Name" + ",Date_Posted" + ",Title" + ",Description"
			+ ",Date_Taken"
			+ " from ATTACHMENTS where Attachment_ID = :Attachment_ID";

	static private final String FIND_MAPS_BY_YEAR_SQL = "select"
			+ " Attachment_ID" + ",Entry_ID" + ",Is_Gallery_Image"
			+ ",File_Name" + ",Activity_ID" + ",Mime_Type" + ",File_Type" + ",User_ID"
			+ ",User_Name" + ",Date_Posted" + ",Title" + ",Description"
			+ ",Date_Taken" + " from ATTACHMENTS where File_Type = 'map'"
			+ " and year(Date_Posted) = :Year";

	static private final String DELETE_BY_ATTACHMENT_ID_SQL = "delete from ATTACHMENTS where Attachment_ID = :Attachment_ID";

	static private final String FIND_BYTES_BY_ATTACHMENT_ID_SQL = "select bytes from ATTACHMENTS where Attachment_ID = :Attachment_ID";

	static private final String FIND_RECENT_IMAGES_SQL = "select * from ATTACHMENTS where Is_Gallery_Image = 'true'"
			+ " order by Date_Posted desc limit :NumEntries";

	static private final String FIND_WITH_ACTIVITY_IDS = "select"
			+ " Attachment_ID" + ",Entry_ID" + ",Is_Gallery_Image"
			+ ",File_Name" + ",Activity_ID" + ",Mime_Type" + ",File_Type" + ",User_ID"
			+ ",User_Name" + ",Date_Posted" + ",Title" + ",Description"
			+ ",Date_Taken"
			+ " from ATTACHMENTS where Activity_ID is not null";


	public void delete(Attachment attachment) throws DataAccessException {
		delete(attachment.getAttachmentId());
	}

	public void delete(int attachmentId) throws DataAccessException {
		Connection conn = null;
		NamedParamStatement delete = null;
		try {

			new ThumbnailDao().deleteByAttachmentId(attachmentId);
			new GeotagDao().deleteByAttachmentId(attachmentId);

			conn = getConnection();
			delete = new NamedParamStatement(conn, DELETE_BY_ATTACHMENT_ID_SQL);
			delete.setInt("Attachment_ID", attachmentId);
			delete.executeUpdate();
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to delete attachment with ID " + attachmentId, e);
		} finally {
			try {
				delete.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public void deleteByEntryId(int entryId) throws DataAccessException {
		Connection conn = null;
		NamedParamStatement find = null;
		ResultSet attachments = null;
		try {
			conn = getConnection();
			find = new NamedParamStatement(conn, FIND_BY_ENTRY_ID_SQL);
			find.setInt("Entry_ID", entryId);
			attachments = find.executeQuery();

			while (attachments.next()) {
				int attachmentId = attachments.getInt("Attachment_ID");
				delete(attachmentId);
			}
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to delete attachments with Entry_ID " + entryId, e);
		} finally {
			try {
				attachments.close();
			} catch (Exception ignored) {
			}
			try {
				find.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public void create(Attachment attachment) throws DataAccessException {
		Connection conn = null;
		NamedParamStatement create = null;
		try {
			conn = getConnection();
			create = new NamedParamStatement(conn, CREATE_SQL);
			create.setInt("Entry_ID", attachment.getEntryId());
			create.setString("Is_Gallery_Image", Boolean.toString(attachment
					.getIsGalleryImage()));
			create.setString("File_Name", attachment.getFileName());
			create.setString("Activity_ID", attachment.getActivityId());
			create.setString("Mime_Type", attachment.getMimeType());
			create.setString("File_Type", attachment.getFileType());
			create.setInt("User_ID", attachment.getUserId());
			create.setString("User_Name", attachment.getUserName());
			create.setDate("Date_Posted", new java.util.Date());
			create.setString("Title", attachment.getTitle());
			create.setString("Description", attachment.getDescription());
			create.setTimestamp("Date_Taken", attachment.getDateTaken());
			create.setBytes("Bytes", attachment.getBytes());
			create.executeUpdate();
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to create attachment with name "
							+ attachment.getFileName(), e);
		} finally {
			try {
				create.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public void updateAll(Collection attachments) throws DataAccessException {
		for (Iterator i = attachments.iterator(); i.hasNext();) {
			Attachment attachment = (Attachment) i.next();
			update(attachment);
		}
	}

	public void update(Attachment attachment) throws DataAccessException {
		Connection conn = null;
		NamedParamStatement update = null;
		try {
			conn = getConnection();
			update = new NamedParamStatement(conn, UPDATE_SQL);
			update.setInt("Entry_ID", attachment.getEntryId());
			update.setString("Is_Gallery_Image", Boolean.toString(attachment
					.getIsGalleryImage()));
			update.setString("File_Name", attachment.getFileName());
			update.setString("Activity_ID", attachment.getActivityId());
			update.setString("Mime_Type", attachment.getMimeType());
			update.setString("File_Type", attachment.getFileType());
			update.setInt("User_ID", attachment.getUserId());
			update.setString("User_Name", attachment.getUserName());
			update.setDate("Date_Posted", new java.sql.Date(attachment
					.getDatePosted().getTime()));
			update.setString("Title", attachment.getTitle());
			update.setString("Description", attachment.getDescription());
			update.setTimestamp("Date_Taken", attachment.getDateTaken());
			update.setInt("Attachment_ID", attachment.getAttachmentId());
			update.executeUpdate();

			ThumbnailDao thumbDao = new ThumbnailDao();
			thumbDao.deleteByAttachmentId(attachment.getAttachmentId());

		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to update attachment with name "
							+ attachment.getFileName(), e);
		} finally {
			try {
				update.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}
	
	private String getBackupFileName(Attachment attachment) {
		return "BACKUP-" + attachment.getFileName();
	}
	
	public boolean hasBackup(Attachment attachment) throws DataAccessException {
		Connection conn = null;
		NamedParamStatement query = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			query = new NamedParamStatement(conn, HAS_BACKUP_SQL);
			query.setString("File_Name", getBackupFileName(attachment));
			results = query.executeQuery();
			if (!results.next()) {
				throw new DataAccessException("Unable to determine whether there's already a backup of file " + attachment.getFileName());
			}
			int numBackups = results.getInt("Num_Backups");
			return numBackups > 0;
		}
		catch (Exception e) {
			String fn = attachment == null ? "[null attachment]" : attachment.getFileName();
			throw new DataAccessException("Error counting backups for file " + fn, e);
		}
		finally {
			try { results.close(); } catch (Exception ignored) {}
			try { query.close(); } catch (Exception ignored) {}
			try { conn.close(); } catch (Exception ignored) {}
		}
	}
	
	public void makeBackup(Attachment attachment) throws DataAccessException {
		if (!hasBackup(attachment)) {
			if (attachment.getBytes() == null) {
				populateBytes(attachment);
			}
			Attachment backup = attachment.copy();
			backup.setFileType(Attachment.TYPE_BACKUP);
			backup.setFileName(getBackupFileName(attachment));
			create(backup);
		}
	}
	
	public Attachment findBackup(Attachment attachment) throws DataAccessException {
		return findByFileName(getBackupFileName(attachment));
	}

    public void updateBytes(Attachment attachment) throws DataAccessException {
        Connection conn = null;
        NamedParamStatement update = null;
        try {
            conn = getConnection();
            update = new NamedParamStatement(conn, UPDATE_BYTES_SQL);
            update.setInt("Entry_ID", attachment.getEntryId());
            update.setBytes("Bytes", attachment.getBytes());
            update.executeUpdate();

            ThumbnailDao thumbDao = new ThumbnailDao();
            thumbDao.deleteByAttachmentId(attachment.getAttachmentId());

        } catch (Exception e) {
            throw new DataAccessException(
                    "Unable to update attachment with name "
                            + attachment.getFileName(), e);
        } finally {
            try {
                update.close();
            } catch (Exception ignored) {
            }
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
    }

	public Attachment populateBytes(Attachment attachment)
			throws DataAccessException {
		Connection conn = null;
		NamedParamStatement find = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			find = new NamedParamStatement(conn,
					FIND_BYTES_BY_ATTACHMENT_ID_SQL);
			find.setInt("Attachment_ID", attachment.getAttachmentId());
			results = find.executeQuery();

			if (!results.next()) {
				throw new DataAccessException(
						"Unable to retrieve bytes for attachment ID "
								+ attachment.getAttachmentId());
			}

			attachment.setBytes(results.getBytes("Bytes"));
			return attachment;
		} catch (DataAccessException rethrown) {
			throw rethrown;
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to retrieve bytes for attacment ID "
							+ attachment.getAttachmentId() + ": "
							+ e.toString(), e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				find.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public Collection findUnattached() throws DataAccessException {
		return findByEntryId(0);
	}

	public Collection findByEntryId(int entryId) throws DataAccessException {
		Connection conn = null;
		NamedParamStatement findByName = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			findByName = new NamedParamStatement(conn, FIND_BY_ENTRY_ID_SQL);
			// "select * from ATTACHMENTS where File_Name = :File_Name";
			findByName.setInt("Entry_ID", entryId);
			results = findByName.executeQuery();

			Collection attachments = new ArrayList();
			while (results.next()) {
				attachments.add(populateAttachment(results));
			}
			return attachments;
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to find attachments for entry " + entryId, e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				findByName.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public Map findByEntryIds(Collection entryIds) throws DataAccessException {
		if (entryIds.isEmpty()) {
			return new BucketMap();
		}

		Connection conn = null;
		PreparedStatement find = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			String sql = MessageFormat.format(FIND_BY_ENTRY_IDS_SQL_FORMAT,
					new String[] { buildParamList(entryIds.size()) });
			find = conn.prepareStatement(sql);
			int p = 1;
			for (Iterator i = entryIds.iterator(); i.hasNext(); p++) {
				int id = ((Integer) i.next()).intValue();
				find.setInt(p, id);
			}
			results = find.executeQuery();

			Map m = new BucketMap(); // Integer entryId --> List of Attachment
			while (results.next()) {
				Attachment a = populateAttachment(results);
				Integer id = new Integer(a.getEntryId());
				m.put(id, a);
			}
			return m;
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to find attachments for entryIds " + entryIds, e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				find.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public Attachment findByAttachmentId(int attachmentId) // File_Name is a KEY
			throws DataAccessException {
		Connection conn = null;
		NamedParamStatement findByName = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			findByName = new NamedParamStatement(conn,
					FIND_BY_ATTACHMENT_ID_SQL);
			// "select * from ATTACHMENTS where File_Name = :File_Name";
			findByName.setInt("Attachment_ID", attachmentId);
			results = findByName.executeQuery();

			if (!results.next())
				throw new DataAccessException(
						"Unable to find attachment with id " + attachmentId);

			Attachment attachment = populateAttachment(results);
			return attachment;
		} catch (Exception e) {
			throw new DataAccessException("Unable to find attachment with id "
					+ attachmentId, e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				findByName.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public Collection findByFileType(String fileType) // File_Name is a KEY
			throws DataAccessException {
		Connection conn = null;
		NamedParamStatement findByName = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			findByName = new NamedParamStatement(conn, FIND_BY_FILE_TYPE_SQL);
			findByName.setString("File_Type", fileType);
			results = findByName.executeQuery();

			Collection attachments = new ArrayList();
			while (results.next()) {
				Attachment a = populateAttachment(results);
				attachments.add(a);
			}

			return attachments;
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to find attachment with type \"" + fileType + "\"",
					e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				findByName.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public Collection findMapsByYear(int year) throws DataAccessException {
		Connection conn = null;
		NamedParamStatement find = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			find = new NamedParamStatement(conn, FIND_MAPS_BY_YEAR_SQL);
			find.setInt("Year", year);
			results = find.executeQuery();

			Collection attachments = new ArrayList();
			while (results.next()) {
				Attachment a = populateAttachment(results);
				attachments.add(a);
			}

			return attachments;
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to find attachment with type \"map\" posted in year "
							+ year, e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				find.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}

	}

	public boolean fileNameExists(String fileName) // File_Name is a KEY
			throws DataAccessException {
		Connection conn = null;
		NamedParamStatement findByName = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			findByName = new NamedParamStatement(conn, FIND_BY_FILE_NAME_SQL);
			// "select * from ATTACHMENTS where File_Name = :File_Name";
			findByName.setString("File_Name", fileName);
			results = findByName.executeQuery();

			return results.next();
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to find attachment with name " + fileName, e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				findByName.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	public Attachment findByFileName(String fileName) // File_Name is a KEY
			throws DataAccessException {
		Connection conn = null;
		NamedParamStatement findByName = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			findByName = new NamedParamStatement(conn, FIND_BY_FILE_NAME_SQL);
			// "select * from ATTACHMENTS where File_Name = :File_Name";
			findByName.setString("File_Name", fileName);
			results = findByName.executeQuery();

			if (!results.next())
				throw new DataAccessException(
						"Unable to find attachment with name " + fileName);

			Attachment attachment = populateAttachment(results);
			return attachment;
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to find attachment with name " + fileName, e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				findByName.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Find a list of all Attachments that have ActivityID populated.
	 *
	 * @return
	 * @throws DataAccessException
	 */
	public Collection<Attachment> findWithActivityIds() throws DataAccessException {
		Connection conn = null;
		NamedParamStatement findIds = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			findIds = new NamedParamStatement(conn, FIND_WITH_ACTIVITY_IDS);
			results = findIds.executeQuery();

			List<Attachment> attachments = new ArrayList<Attachment>();
			while (results.next()) {
				Attachment a = populateAttachment(results);
				attachments.add(a);
			}

			return attachments;
		}
		catch (Exception e) {
			throw new DataAccessException("Unable to find activity IDs:", e);
		}
	}

	public Collection findRecentImages(int numEntries)
			throws DataAccessException {
		Connection conn = null;
		NamedParamStatement findRecent = null;
		ResultSet results = null;
		try {
			conn = getConnection();
			findRecent = new NamedParamStatement(conn, FIND_RECENT_IMAGES_SQL);
			findRecent.setInt("NumEntries", numEntries);
			results = findRecent.executeQuery();

			Collection attachments = new ArrayList();
			while (results.next()) {
				attachments.add(populateAttachment(results));
			}
			return attachments;
		} catch (Exception e) {
			throw new DataAccessException("Unable to find " + numEntries
					+ " recent attachments:", e);
		} finally {
			try {
				results.close();
			} catch (Exception ignored) {
			}
			try {
				findRecent.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}

	private Collection populateAttachments(ResultSet results)
			throws SQLException, DataAccessException {
		Collection attachments = new LinkedList();
		while (results.next()) {
			Attachment a = new Attachment();
			populateAttachmentHeader(results, a);
			attachments.add(a);
		}

		populateAttachmentDetails(attachments);

		return attachments;
	}

	private Attachment populateAttachment(ResultSet results)
			throws SQLException, DataAccessException {
		Attachment attachment = new Attachment();
		populateAttachmentHeader(results, attachment);
		Collection geotags = new GeotagDao().findByAttachmentId(attachment
				.getAttachmentId());
		attachment.setGeotags(geotags);
		return attachment;
	}

	private void populateAttachmentHeader(ResultSet results,
			Attachment attachment) throws SQLException, DataAccessException {
		attachment.setAttachmentId(results.getInt("Attachment_ID"));
		attachment.setEntryId(results.getInt("Entry_ID"));
		attachment.setIsGalleryImage(results.getBoolean("Is_Gallery_Image"));
		attachment.setFileName(results.getString("File_Name"));
		attachment.setActivityId(results.getString("Activity_ID"));
		attachment.setMimeType(results.getString("Mime_Type"));
		attachment.setFileType(results.getString("File_Type"));
		attachment.setUserId(results.getInt("User_ID"));
		attachment.setUserName(results.getString("User_Name"));
		attachment.setDatePosted(results.getDate("Date_Posted"));
		attachment.setDateTaken(results.getTimestamp("Date_Taken"));
		attachment.setTitle(results.getString("Title"));
		attachment.setDescription(results.getString("Description"));

		// If we don't already have a timestamp for an image, try to get one.
		// Ignore exceptions, since odd files might accidentally get marked
		// as images.
		if (attachment.getIsGalleryImage()
				&& (attachment.getDateTaken() == null)) {
			try {
				populateBytes(attachment);
				byte[] bytes = attachment.getBytes();

				ImageTools tools = new ImageTools();

				System.out.println("Getting date taken for \""
						+ attachment.getFileName() + "\"");
				java.util.Date dateTaken = tools.getDateTaken(bytes);
				System.out.println("Done getting date taken for \""
						+ attachment.getFileName() + "\"");

				if (dateTaken == null) {
					throw new NullPointerException("No Date Taken Found!");
				}
				attachment.setDateTaken(dateTaken);
				update(attachment); // Save the new Date Taken
			} catch (Exception ignored) {
				// This is a totally bogus way of handling the problem, but
				// it'll have to do for now. Just set the date to Jan 1, 1970
				// so that we won't try to parse it again.
				attachment.setDateTaken(new java.sql.Date(0l));
				update(attachment);
			}
		}
	}

	private void populateAttachmentDetails(Collection attachments)
			throws DataAccessException {
		Set ids = new HashSet();
		for (Iterator i = attachments.iterator(); i.hasNext();) {
			Attachment a = (Attachment) i.next();
			ids.add(new Integer(a.getAttachmentId()));
		}

		Map geotagsById = new GeotagDao().findByAttachmentIds(ids);
		for (Iterator i = attachments.iterator(); i.hasNext();) {
			Attachment a = (Attachment) i.next();
			Integer id = new Integer(a.getAttachmentId());
			Collection geotags = (Collection) geotagsById.get(id);
			a.setGeotags(geotags);
		}
	}
}
