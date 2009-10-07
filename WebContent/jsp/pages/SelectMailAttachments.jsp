<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<tiles:insert definition="pageLayout">
  <tiles:put name="head" type="string">
      <script type="text/JavaScript">
          function toggleSelection(i) {
              var checkbox = document.getElementById("file_" + i);
              checkbox.checked = !checkbox.checked;
              
              var div = document.getElementById("div_" + i);
              div.style.background = checkbox.checked ? "#555588" : null;
              return true;
          }
      </script>
  </tiles:put>    
  <tiles:put name="title" type="string">Uploaded Attachments</tiles:put>
  <tiles:put name="content" type="string">
    <!-- SelectMailAttachments.jsp -->
    <nested:form action="AttachMailAttachments.do">
        <nested:define id="entryId" name="unattachedAttachmentsForm" property="entryId" type="java.lang.Integer" /> 
        <!-- id="unattachedAttachmentsForm" type="net.kukido.blog.forms.UnattachedAttachmentsForm" scope="session"/ -->
        <nested:iterate property="attachments" id="a" type="net.kukido.blog.datamodel.Attachment" indexId="i">
          <div id="div_<%= i %>" class="searchresult" onclick="toggleSelection(<%= i %>)">
            <h2 class="title" style="border: 0; border-top: 1px dotted #ccc; margin-top: 0.5em;">
                <nested:equal value="image" name="a" property="fileType">
                    <img alt="<%= a.getTitle() %>" class="thumbnail" src="attachments/thumbs/<%= a.getFileName() %>">
                </nested:equal>
                <nested:checkbox property="entryId" value="<%= request.getParameter("entryId") %>" styleId="<%= "file_" + i %>" style="display: none" />
                <nested:text property="fileName" size="60" />
            </h2>
            <span class="entrymetainfo" style="padding: 2px;">
                Subject: <nested:write name="a" property="title" />
            </span>
          </div>
        </nested:iterate>
        <nested:submit value="Attached Selected Files" />
    </nested:form>
    <!-- End of SelectMailAttachments.jsp -->
  </tiles:put>
</tiles:insert>
