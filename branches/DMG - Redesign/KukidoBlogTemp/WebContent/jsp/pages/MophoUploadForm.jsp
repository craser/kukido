<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="pageLayout">
  <tiles:put type="string" name="content">
    <!-- MophoUploadForm.jsp -->
    <html:errors/>
    <html:form action="MophoUpload" focus="image" enctype="multipart/form-data">
    Image: <html:file property="image"/>
    <br>
    Title: <html:text property="title"/>
    <br>
    <html:submit/>
    </html:form>
    <!-- End MophoUploadForm.jsp -->
  </tiles:put>
</tiles:insert>

