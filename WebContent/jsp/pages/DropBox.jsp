<%@page contentType="text/html"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="pageLayout">
  <tiles:put type="string" name="content">
    <!-- DropBoxUploadForm.jsp -->
    <% 
        String bgColor="#cccccc";
        int i = 0;
    %>
    <link rel="stylesheet" type="text/css" href="css/dropbox.css" />
    <table width="100%">
      <logic:iterate name="fileNames" scope="request" id="fileName">
      <tr>
        <td class="controllink"><a href="DropBoxDelete.do?fileName=<%= fileName %>">delete</a></th>
        <td class="filename" bgcolor="<%= (i++%2)==0?bgColor:"" %>"><a href="dropbox/<%= fileName %>"><bean:write name="fileName"/></a></td>
      </tr>
      </logic:iterate>
    </table>
    <hr>
    <html:errors/>
    <html:form action="DropBoxUpload" focus="password" enctype="multipart/form-data">
      <table>
          <tr>
              <td class="inputlabel"><bean:message key="prompt.file.upload"/></th>
              <td><html:file property="uploadFile"/></td>
          </tr>
          <tr>
              <td colspan="2">
              <html:submit />
              </td>
          </tr>
      </table>
    </html:form>
    <!-- /DropBoxUploadForm.jsp -->
  </tiles:put>
</tiles:insert>