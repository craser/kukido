<%@ page import="net.kukido.blog.datamodel.*" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean class="java.util.Date" id="now" />

<nested:define id="entryId" name="logEditForm" property="entry.entryId" scope="session" />
<tiles:insert definition="pageLayout">

  <tiles:put type="string" name="head">
    <link rel="stylesheet" type="text/css" href="css/logedit.css">
    <script type="text/JavaScript" src="javascript/jquery-1.11.3.js"></script>
    <script type="text/JavaScript" src="javascript/tags-typeahead.js"> </script>
    <script type="text/JavaScript">
      $(window).load(function() { TagsTypeAhead.init($("#tagsInput")); });
    </script>
  </tiles:put>

  <tiles:put type="string" name="content">
    <nested:form action="LogEdit.do">
      
      <html:errors/>
      
      <div class="entry">

        <div class="title">
          <nested:text property="entry.title" size="60"/>
        </div>

        <div class="entrymetainfo">
          <bean:write name="now" format="EEE, M/d h:mma" />
        </div>

        <div class="entrycontent">          
          
          <nested:present property="entry.imageFileName">
            <nested:define id="thumbnailFileType" name="logEditForm" property="entry.imageFileType" />
            <nested:equal name="logEditForm" property="entry.imageFileType" value="image">
              <nested:define id="thumbnailFilename" name="logEditForm" property="entry.imageFileName" />
              <img class="thumbnail" src="attachments/thumbs/<%= thumbnailFilename %>">
            </nested:equal>
            <nested:equal name="logEditForm" property="entry.imageFileType" value="map">
              <nested:define id="thumbnailFilename" name="logEditForm" property="entry.imageFileName" type="java.lang.String" />
              <dmg:mapImage size="thumbnail" map="<%= "" + thumbnailFilename %>" styleClass="thumbnail" />
            </nested:equal>
          </nested:present>
          
          <table>
            <tr>
              <td class="inputlabel">Via (title):</td>
              <td><nested:text property="entry.viaTitle"/></td>
            </tr>
            <tr>
              <td class="inputlabel">Via (text):</td>
              <td><nested:text property="entry.viaText"/></td>
            </tr>
            <tr>
              <td class="inputlabel">Via (URL):</td>
              <td><nested:text property="entry.viaUrl"/></td>
            </tr>
            <tr>
              <td class="inputlabel">Tags (space-separated):</td>
              <td><nested:text property="tags" styleId="tagsInput" /></td>
            </tr>
            <tr>
              <td class="inputlabel">Allow Comments:</td>
              <td><nested:checkbox property="entry.allowComments" /></td>
            </tr>
            <tr>
              <td class="inputlabel">Syndicate:</td>
              <td><nested:checkbox property="entry.syndicate" /></td>
            </tr>
          </table>
          
          <nested:textarea property="entry.intro" rows="20" cols="60"/>
          <nested:textarea property="entry.body" rows="20" cols="60"/>
        </div>
        <nested:iterate id="a" property="entry.attachments" type="net.kukido.blog.datamodel.Attachment">
          <nested:hidden property="isGalleryImage" />
        </nested:iterate>
        <nested:image src="img/icon_save.png" title="Save Changes" />
        <nested:define id="entry" name="logEditForm" property="entry" />
        <dmg:entrylink entry="entry"><html:img src="img/icon_cancel.png" title="Cancel" /></dmg:entrylink>
      </div>      
    </nested:form>
  </tiles:put>
  
  <tiles:put type="string" name="sidebar">

        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Attachments</tiles:put>
          <tiles:put type="string" name="content">
            <nested:form action="LogEdit.do">
              <table cellspacing="0">
                <tr>
                  <th>File Type</th>
                  <th width="16"><img class="icon" src="img/icon_image.png" title="Display this file as a gallery image?" /></th>
                  <th width="16"><img class="icon" src="img/icon_document.png" title="Use this image as the front page thumbnail?" /></th>
                  <th width="16"><img class="icon" src="img/icon_delete.png" title="Delete this file?" /></th>
                  <th>Filename</th>
                </tr>
                <nested:iterate id="a" property="entry.attachments" type="net.kukido.blog.datamodel.Attachment">
                  <tr>
                    <td>
                        <nested:select property="fileType">
                            <nested:options name="logEditForm" property="attachmentFileTypeOptions" />
                        </nested:select>
                    </td>
                    <td><nested:checkbox property="isGalleryImage" /></td>
                    <td><html:radio property="entry.imageFileName" value="<%= a.getFileName() %>" /></td>
                    <td><nested:link action="DeleteAttachment" paramId="attachmentId" paramName="a" paramProperty="attachmentId" onclick="return confirm('Delete this attachment?');"><img class="icon" src="img/icon_delete.png" title="Delete this file?" /></nested:link></td>
                    <td><a href="attachments/<%= a.getFileName() %>"><bean:write name="a" property="fileName" /></a></td> 
                  </tr> 
                </nested:iterate>
                <tr>
                  <td colspan="2">&nbsp;</td>
                  <td><nested:radio property="entry.imageFileName" value="" /></td>
                  <td>&nbsp;</td>
                  <td><i style="color: #777">No Thumbnail</i></td>
                </tr>
                <bean:define type="java.util.Collection" id="attachments" name="logEditForm" property="entry.attachments" />
                <bean:define id="numAttachments" value="<%= "" + attachments.size() %>" />
                <nested:equal name="numAttachments" value="0">
                  <tr><td colspan="4" nowrap><i style="color: #777">No Attachments</i></td></tr>
                </nested:equal>
                <tr>
                  <td colspan="4" nowrap>
                    <nested:hidden property="entry.allowComments" />
                    <nested:submit value="Save"/>
                    <html:button onclick="<%= "window.location = 'AttachmentUploadForm.do?entryId=" + entryId + "'" %>" property="entry.entryId" value="Add Attachment" />
                    <html:button onclick="<%= "window.location = 'LoadMailAttachments.do?entryId=" + entryId + "'" %>" property="entry.entryId" value="Load Attachment" />
                  </td>	
                </tr>
              </table>
            </nested:form>
          </tiles:put>
        </tiles:insert>
        
        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Geotagging</tiles:put>
          <tiles:put type="string" name="content">
            <nested:form action="CalculateGeotags" method="GET">
              <table>
                <tr>
                  <td class="inputlabel">Map:</td>
                  <td>
                    <nested:select property="mapId">
                      <nested:iterate name="logEditForm" property="entry.attachments" id="a" type="Attachment">
                        <nested:equal property="fileType" value="map">
                          <option value="<%= a.getAttachmentId() %>"><bean:write name="a" property="fileName" /></option>
                        </nested:equal>
                      </nested:iterate>
                    </nested:select>
                  </td>
                </tr>
                <tr>
                  <td class="inputlabel">Offset (sec.):</td>
                  <td><nested:text property="timeOffset" /></td>
                </tr>
                <tr>
                  <td class="inputlabel">Replace existing geotags?</td>
                  <td><nested:checkbox property="replaceExistingTags" /></td>
                </tr>
                <tr>
                  <td colspan="2">
                    <nested:hidden property="entryId" value="<%= request.getParameter("entryId") %>" />
                    <nested:submit value="GO" />
                  </td>
                </tr>
              </table>
            </nested:form>
          </tiles:put>
        </tiles:insert>
        
        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Elevation Correction</tiles:put>
          <tiles:put type="string" name="content">
            <ul class="navmenu">
              <nested:iterate name="logEditForm" property="entry.attachments" id="a" type="Attachment">
                <nested:equal property="fileType" value="map">
                  <bean:define id="url">/ResolveElevation?entryId=<bean:write name="entry" property="entryId" />&amp;fileName=<bean:write name="a" property="fileName" /></bean:define>
                  <li><a href="<%= url %>"><bean:write name="a" property="fileName" /></a></li>
                </nested:equal>
              </nested:iterate>
            </ul>
          </tiles:put>
        </tiles:insert>

        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Gallery</tiles:put>
          <tiles:put type="string" name="content">
            <ul class="gallerymenu">
              <nested:iterate name="logEditForm" property="entry.attachments" id="image" type="net.kukido.blog.datamodel.Attachment">
                <logic:equal name="image" property="fileType" value="image">
                  <li><a href="attachments/<%= image.getAttachmentId() %>" title="<%= image.getTitle() %>">
                      <img src="attachments/thumbs/<%= image.getFileName() %>">
                      </a>
                  </li>
                </logic:equal>
              </nested:iterate>
            </ul>
          </tiles:put>
        </tiles:insert>

  </tiles:put>
</tiles:insert>

