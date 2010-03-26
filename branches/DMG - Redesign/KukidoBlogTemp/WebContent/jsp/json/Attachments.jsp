<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="net.kukido.blog.datamodel.*" %>
<jsp:useBean id="attachments" type="java.util.Collection" scope="request"/>
<%--
Attachment:
    private int attachmentId;
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
    private Collection geotags;
    //private byte[] bytes;
    
Geotag:
    private int tagId;
    private int mapId; // Attachment_ID for GPX map used to generate this tag, if appropriate.
    private int attachmentId; // Attachment this tag locates.
    private Date dateTagged;
    private Date timestamp;
    private float latitude;
    private float longitude;
    private float elevation;
--%>
([
    <nested:iterate name="attachments" id="a" type="Attachment">
        {   attachmentId: <nested:write name="a" property="attachmentId" />,
            entryId: <nested:write name="a" property="entryId" />,
            isGalleryImage: <nested:write name="a" property="isGalleryImage" />,
            fileName: "<nested:write name="a" property="fileName" />",
            mimeType: "<nested:write name="a" property="mimeType" />",
            fileType: "<nested:write name="a" property="fileType" />",
            userId: <nested:write name="a" property="userId" />,
            userName: "<nested:write name="a" property="userName" />",
            datePosted: "<nested:write name="a" property="datePosted" />",
            dateTaken: "<nested:write name="a" property="dateTaken" />",
            title: "<nested:write name="a" property="title" />",
            description: "<nested:write name="a" property="description" />",
            geotags: [
                <nested:iterate name="a" property="geotags" id="t" type="Geotag">
                {   tagId: <nested:write name="t" property="tagId" />,
                    mapId: <nested:write name="t" property="mapId" />,
                    attachmentId: <nested:write name="t" property="attachmentId" />,
                    dateTagged: "<nested:write name="t" property="dateTagged" />",
                    timestamp: "<nested:write name="t" property="timestamp" />",
                    latitude: <nested:write name="t" property="latitude" />,
                    longitude: <nested:write name="t" property="longitude" />,
                    elevation: <nested:write name="t" property="elevation" />
                },
                </nested:iterate>
            ]
        },
    </nested:iterate>
    ])