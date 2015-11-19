<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<jsp:useBean id="entry" scope="request" class="net.kukido.blog.datamodel.LogEntry" />
<jsp:useBean id="nextEntryId" scope="request" type="java.lang.Integer" />
<jsp:useBean id="prevEntryId" scope="request" type="java.lang.Integer" />


<tiles:insert definition="pageLayout">

  <tiles:put name="title"><bean:write name="entry" property="title"/></tiles:put>

  <tiles:put type="string" name="content">
    <tiles:insert definition="entry" flush="false">
      <tiles:put name="entry" beanName="entry" />
      <tiles:put name="showContent" value="true" />
      <tiles:put name="showBody" value="true" />     
    </tiles:insert>
    
    <div class="traversallinks">
      <div class="preventrylink">
        <logic:greaterThan name="prevEntryId" value="0">
          <dmg:entrylink entryId="<%= prevEntryId.intValue() %>">Previous</dmg:entrylink>
        </logic:greaterThan>
        <logic:equal name="prevEntryId" value="0">
          Previous
        </logic:equal>
      </div>
      <div class="nextentrylink">
        <logic:greaterThan name="nextEntryId" value="0">
          <dmg:entrylink entryId="<%= nextEntryId.intValue() %>">Next</dmg:entrylink>
        </logic:greaterThan>
        <logic:equal name="nextEntryId" value="0">
          Next
        </logic:equal>
      </div>
    </div>

    <logic:greaterThan name="entry" property="numComments" value="0">
      <tiles:insert definition="comments" flush="false">
        <tiles:put name="comments" beanName="entry" beanProperty="comments" />
      </tiles:insert>
    </logic:greaterThan>
    <tiles:insert definition="commentForm" flush="false">
      <tiles:put name="entry" beanName="entry" />
    </tiles:insert>

  </tiles:put>

  <tiles:put type="string" name="sidebar">

    <jsp:include page="/jsp/nav/SearchNav.jsp" flush="false" />

    <logic:equal name="entry" property="hasGalleryImages" value="true">
        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Gallery</tiles:put>
          <tiles:put type="string" name="content">
            <ul class="gallerymenu">
              <nested:iterate name="entry" property="attachments" id="image" type="net.kukido.blog.datamodel.Attachment">
                <logic:equal name="image" property="fileType" value="image">
                  <li><a href="attachments/posters/<%= image.getFileName() %>" data-lightbox="<%= entry.getEntryId() %>" title="<%= image.getTitle() %>">
                    <img class="thumbnail" src="attachments/thumbs/<%= image.getFileName() %>">
                    </a>
                  </li>
                </logic:equal>
              </nested:iterate>
            </ul>
          </tiles:put>
        </tiles:insert>
    </logic:equal>

    <logic:equal name="entry" property="hasMaps" value="true">
        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Maps</tiles:put>
          <tiles:put type="string" name="content">
            <ul class="navmenu">
              <nested:iterate name="entry" property="attachments" id="a" type="net.kukido.blog.datamodel.Attachment">
                <logic:equal name="a" property="fileType" value="map">
                  <li><a href="maps/<%= a.getFileName() %>" title="<%= a.getTitle() %>"><dmg:attachmentIcon attachmentType="<%= a.getFileType() %>" />&nbsp;<bean:write name="a" property="title" /></a>
                  </li>
                </logic:equal>
              </nested:iterate>
            </ul>
          </tiles:put>
        </tiles:insert>
    </logic:equal>

    <logic:equal name="entry" property="hasDocuments" value="true">
        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Attachments</tiles:put>
          <tiles:put type="string" name="content">
            <ul class="navmenu">
              <nested:iterate name="entry" property="attachments" id="a" type="net.kukido.blog.datamodel.Attachment">
                <nested:notEqual name="a" property="fileType" value="image">
                  <li><a href="attachments/<%= a.getFileName() %>" title="<%= a.getTitle() %>"><dmg:attachmentIcon attachmentType="<%= a.getFileType() %>" />&nbsp;<bean:write name="a" property="fileName" /></a>
                </nested:notEqual>
              </nested:iterate>
            </ul>
          </tiles:put>
        </tiles:insert>
    </logic:equal>
    
    <logic:notEmpty name="entry" property="trackbacks">
      <tiles:insert definition="sidebarElement" flush="false">
        <tiles:put type="string" name="title">Trackbacks</tiles:put>
        <tiles:put type="string" name="content">
          <ul class="navmenu">
            <nested:iterate name="entry" property="trackbacks" id="trackback" type="net.kukido.blog.datamodel.Trackback">
              <li><a href="<%= trackback.getUrl() %>"><bean:write name="trackback" property="title"/></a>
              </li>
            </nested:iterate>
          </ul>
        </tiles:put>
      </tiles:insert>
    </logic:notEmpty>
    
    <logic:notEmpty name="entry" property="tags">
      <tiles:insert definition="sidebarElement" flush="false">
        <tiles:put type="string" name="title">Related Links</tiles:put>
        <tiles:put type="string" name="content">
            <nested:iterate name="entry" property="tags" id="tag" type="net.kukido.blog.datamodel.Tag">
              <jsp:include page="/TaggedLinks" flush="false">
                <jsp:param name="cacheUrl" value="<%= "https://api.del.icio.us/v1/posts/recent?tag=" + tag.getName() %>" />
                <jsp:param name="fileName" value="<%= "delicious_links_by_tag_" + tag.getName() + ".html" %>" />
                <jsp:param name="resourcePath" value="<%= "/delicious_links_by_tag_" + tag.getName() + ".html" %>" />
              </jsp:include>
            </nested:iterate>
        </tiles:put>
      </tiles:insert>
    </logic:notEmpty>

  </tiles:put>

</tiles:insert>
