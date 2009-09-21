<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean id="attachments" type="java.util.Collection" scope="request"/>
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
    <form action="AttachAttachments.do">
        <input type="hidden" name="entryId" value="<%= request.getParameter("entryId") %>" />
        <logic:iterate name="attachments" id="attachment" type="net.kukido.blog.datamodel.Attachment" indexId="i">
          <div id="div_<%= i %>" class="searchresult" onclick="toggleSelection(<%= i %>)">
            <h2 class="title" style="border: 0; border-top: 1px dotted #ccc; margin-top: 0.5em;">
                <logic:equal value="image" name="attachment" property="fileType">
                    <img alt="<%= attachment.getTitle() %>" class="thumbnail" src="attachments/thumbs/<%= attachment.getFileName() %>">
                </logic:equal>
                <input style="display: none" id="file_<%= i %>" type="checkbox" name="fileName" value="<%= attachment.getFileName() %>" /> 
                <bean:write name="attachment" property="fileName" /> 
            </h2>
            <span class="entrymetainfo" style="padding: 2px;">
                Subject: <bean:write name="attachment" property="title" />
            </span>
          </div>
        </logic:iterate>
        <input type="submit" value="Attach Selected Files" />
    </form>
    <!-- End of SelectMailAttachments.jsp -->
  </tiles:put>
</tiles:insert>
