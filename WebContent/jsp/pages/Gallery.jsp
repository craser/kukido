<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<jsp:useBean id="galleryImage" class="net.kukido.blog.datamodel.Attachment" scope="request" />
<jsp:useBean id="entry" class="net.kukido.blog.datamodel.LogEntry" scope="request" />
<jsp:useBean id="galleryImages" type="java.util.Collection" scope="request" />

<tiles:insert definition="pageLayout">

  <tiles:put type="string" name="title" beanName="galleryImage" beanProperty="title" />

  <tiles:put type="string" name="head">
      <link rel="stylesheet" type="text/css" href="css/gallery.css" />
  </tiles:put> 

  <tiles:put type="string" name="content">
    <div class="entry">
      <span class="title"><bean:write name="galleryImage" property="title" /></span>
      <span class="entrymetainfo">[<bean:write name="galleryImage" property="datePosted" />] Attached to: <dmg:entrylink entry="entry" scope="request"><bean:write name="entry" property="title"/></dmg:entrylink></span>
      <span class="entrymetainfo">Taken <bean:write name="galleryImage" property="dateTaken" format="MM/dd/yy hh:mm:ssa zz" /></span>
      <div>
        <div style="text-align: center">
          <a class="unmarked" href="attachments/<%= galleryImage.getFileName() %>"><img class="postcard" src="attachments/posters/<%= galleryImage.getFileName() %>" /></a>
          <%-- <div style="font: 0.8em sans-serif"><a class="unmarked" href="attachments/<%= galleryImage.getFileName() %>">(click to zoom)</a></div> --%>
        </div>
        
        <span class="entrycontent"><bean:write name="galleryImage" property="description" filter="false"/></span>
      </div>
    </div>
  </tiles:put>

  <tiles:put type="string" name="sidebar">
    <tiles:insert definition="sidebarElement" flush="false">
      <tiles:put type="string" name="title">Gallery</tiles:put>
      <tiles:put type="string" name="content">
        <ul class="gallerymenu">
          <nested:iterate name="entry" property="attachments" id="image" type="net.kukido.blog.datamodel.Attachment">
            <logic:equal name="image" property="isGalleryImage" value="true">
                <li><a href="galleries/<%= image.getAttachmentId() %>" title="<%= image.getTitle() %>">
                <img class="thumbnail" src="attachments/thumbs/<%= image.getFileName() %>" />
                </a></li>
            </logic:equal>
          </nested:iterate>
        </ul>
      </tiles:put>
    </tiles:insert>
  </tiles:put>
  
</tiles:insert>

