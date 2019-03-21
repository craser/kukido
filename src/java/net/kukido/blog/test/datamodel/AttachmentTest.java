package net.kukido.blog.test.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.kukido.blog.datamodel.Attachment;
import net.kukido.blog.datamodel.Geotag;

import org.junit.*;
import static org.junit.Assert.*;

public class AttachmentTest 
{
	
	@Test
	public void test_copy()
	{
		/*
		 *     private int attachmentId;
    private int entryId;
    private boolean isGalleryImage;
    private String fileName;
    private String mimeType;
    private String fileType;
    private int userId;
    private String userName;
    private Date datePosted;
    private Date dateTaken;
    private String title;
    private String description;
    private Collection<Geotag> geotags;
    private byte[] bytes;
		 */
		int entryId = 42;
		boolean isGalleryImage = true;
		String fileName = "FILE_NAME";
		String activityId = "2015-12-14T19:22:04Z";
		String mimeType = "MIME_TYPE";
		String fileType = Attachment.TYPE_IMAGE;
		int userId = 1138;
		String userName = "USER_NAME";
		Date datePosted = new Date();
		Date dateTaken = new Date();
		String title = "TITLE";
		String description = "DESCRIPTION";
		List<Geotag> geotags = new ArrayList<Geotag>();
		byte[] bytes = new byte[0];
		
		Attachment att = new Attachment();
		att.setEntryId(entryId);
		att.setIsGalleryImage(isGalleryImage);
		att.setFileName(fileName);
		att.setActivityId(activityId);
		att.setMimeType(mimeType);
		att.setFileType(fileType);
		att.setUserId(userId);
		att.setUserName(userName);
		att.setDatePosted(datePosted);
		att.setDateTaken(dateTaken);
		att.setTitle(title);
		att.setDescription(description);
		att.setGeotags(geotags);
		att.setBytes(bytes);
		
		Attachment copy = att.copy();
		
		assertTrue(copy.getEntryId() == att.getEntryId());
		assertTrue(copy.getIsGalleryImage() == att.getIsGalleryImage());
		assertTrue(copy.getFileName().equals(att.getFileName()));
		assertTrue(copy.getActivityId().equals(att.getActivityId()));
		assertTrue(copy.getMimeType().equals(att.getMimeType()));
		assertTrue(copy.getFileType().equals(att.getFileType()));
		assertTrue(copy.getUserId() == att.getUserId());
		assertTrue(copy.getUserName().equals(att.getUserName()));
		assertTrue(copy.getDatePosted().equals(att.getDatePosted()));
		assertTrue(copy.getDateTaken().equals(att.getDateTaken()));
		assertTrue(copy.getTitle().equals(att.getTitle()));
		assertTrue(copy.getDescription().equals(att.getDescription()));
		//assertTrue(copy.getBytes() == att.getBytes());
		//assertTrue(copy.getGeotags() == att.getGeotags());
	}

}
