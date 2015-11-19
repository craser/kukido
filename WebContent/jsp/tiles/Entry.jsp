<%@page contentType="application/xhtml+xml"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<tiles:useAttribute id="entry" name="entry" classname="net.kukido.blog.datamodel.LogEntryHeader" />
<tiles:useAttribute id="showContent" name="showContent" classname="java.lang.String" />
<tiles:useAttribute id="showBody" name="showBody" classname="java.lang.String" />

<!-- Entry Tile -->
<div class="entry">

  <h2 class="title"><dmg:entrylink entry="entry"><bean:write name="entry" property="title" /></dmg:entrylink></h2>
  <div class="entrymetainfo">  
    <logic:notEmpty name="entry" property="tags">
      <div style="float: right; font-variant: small-caps; text-transform: lowercase; margin-left: 1em;">
        Tags:
        <logic:iterate name="entry" property="tags" id="tag" type="net.kukido.blog.datamodel.Tag">
          <a href="tags/<%= tag %>"><bean:write name="tag" property="name" /></a>
        </logic:iterate>
      </div>
    </logic:notEmpty>
  
    <bean:write name="entry" property="datePosted" format="EEE, MM/dd/yy" locale="dmg.locale"/>

    <logic:present name="entry" property="viaText">
       &#8226; via <html:link title="<%= entry.getViaTitle() %>" href="<%= entry.getViaUrl() %>"><bean:write name="entry" property="viaText"/></html:link>
    </logic:present>
    &#8226; <dmg:entrylink entry="entry" anchor="comments">comments (<bean:write name="entry" property="numComments" />)</dmg:entrylink>
  </div>
  <logic:present name="user" scope="session">
    <jsp:useBean id="user" scope="session" class="net.kukido.blog.datamodel.User"/>
    <logic:equal name="entry" property="userName" value="<%= user.getUserName() %>">
      <div class="entrymetainfo">
        ( <html:link action="LogEditForm" paramId="entryId" paramName="entry" paramProperty="entryId">edit</html:link> |
        <html:link onclick="return confirm('Delete this entry?');" action="LogDelete" paramId="entryId" paramName="entry" paramProperty="entryId">delete</html:link> )
      </div>
    </logic:equal>
  </logic:present>
  <logic:equal name="showContent" value="true">
      <div class="entrycontent">
        <logic:present name="entry" property="imageFileName">
          <logic:equal name="entry" property="imageFileType" value="image">
            <p style="text-align: center">
              <img class="titleimage" alt="<%= entry.getTitle() %>" src="attachments/postcards/<%= entry.getImageFileName() %>">
            </p>
          </logic:equal>
          <logic:equal name="entry" property="imageFileType" value="map">
            <p style="text-align: center">
              <a class="maplink" href="maps/<%= entry.getImageFileName() %>" title="<%= entry.getTitle() %>">
                <dmg:mapImage styleClass="titleimage" map="<%= entry.getImageFileName() %>" />
              </a>
            </p>
          </logic:equal>
        </logic:present>
        <bean:write name="entry" property="intro" filter="false"/>

        <nested:iterate name="entry" property="attachments" id="a" type="net.kukido.blog.datamodel.Attachment">
          <nested:equal name="a" property="fileType" value="map">
              <p><a class="maplink" href="maps/<%= a.getFileName() %>" title="<%= a.getTitle() %>"><dmg:mapImage size="thumbnail" styleClass="thumbnail" map="<%= a.getFileName() %>" /> <bean:write name="a" property="title" /></a>
              </p>
          </nested:equal>
        </nested:iterate>        
        
        
        
        <logic:present name="entry" property="body">
          <logic:notEqual name="entry" property="body" value="">
            <logic:equal name="showBody" value="true">
              <bean:write name="entry" property="body" filter="false"/>
            </logic:equal>
            <logic:notEqual name="showBody" value="true">
              <dmg:entrylink styleClass="continuedlink" entry="entry">continued</dmg:entrylink>
            </logic:notEqual>
          </logic:notEqual>
        </logic:present>
      </div>
  </logic:equal>
  <div class="entrymetainfo entryfooter" style="text-align: right;">
    <dmg:entrylink entry="entry">permalink</dmg:entrylink>
    &#8226; <a title="Follow comments via RSS" href="feeds/comments/<%= entry.getEntryId() %>.xml">RSS</a>
    &#8226; <dmg:trackbackLink entry="entry">trackback (<bean:write name="entry" property="numTrackbacks" />)</dmg:trackbackLink>
  </div>
</div>
<!-- /Entry Tile -->