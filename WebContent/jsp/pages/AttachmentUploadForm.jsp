<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<jsp:useBean id="user" class="net.kukido.blog.datamodel.User" scope="session"/>

<tiles:insert definition="pageLayout">
<tiles:put type="string" name="content">
<nested:form action="UploadAttachment.do" enctype="multipart/form-data" focus="title">
<table border="0" cellpadding="1" cellspacing="1">
  <tr>
    <td class="inputlabel">
      Title:
    </td>
    <td>
      <nested:text property="attachment.title"/>
      <nested:hidden property="entryId" />
    </td>
  </tr>
  <tr>
    <td class="inputlabel">
      File Type:
    </td>
    <td>
        <nested:select property="attachment.fileType">
            <nested:options name="attachmentForm" property="fileTypeOptions" />
        </nested:select>
    </td>
  </tr>
  <tr>
    <td>
      <nested:checkbox property="attachment.isGalleryImage" /> 
    </td>
    <td class="inputlabel">
      Use as Gallery Image
    </td>
  </tr>
  <tr>
    <td>
      <nested:checkbox property="useAsGalleryThumb" /> 
    </td>
    <td class="inputlabel">
      Use as Gallery Thumbnail
    </td>
  </tr>
  <tr>
    <td class="inputlabel">
      Display thumbnail as:
    </td>
    <td>
        <nested:select property="imageDisplayClass">
          <nested:options property="displayClassOptions" />
        </nested:select>
    </td>
  </tr>
  <tr>
    <td>
      <nested:checkbox property="isZipFile" /> 
    </td>
    <td class="inputlabel">
      File is a Zip File
    </td>
  </tr>
  <tr>
    <td class="inputlabel">
      File:
    </td>
    <td>
      <nested:file property="file"/>
    </td>
  </tr>
  <tr>
      <td class="inputlabel">
          <nested:checkbox property="loadFromUrl" /> Load file from URL:
      </td>
      <td>
          <nested:text property="fileUrl" />
      </td>
  </tr>
  <tr>
      <td class="inputlabel">
          Save file as:
      </td>
      <td>
          <nested:text property="attachment.fileName" />
      </td>
  </tr>
  <tr>
    <td  class="inputlabel" valign="top">Comments:</td>
    <td><nested:textarea property="attachment.description" cols='40' rows='20'/>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <html:submit/>
    </td>
  </tr>
</table>
</nested:form>
</tiles:put>
</tiles:insert>