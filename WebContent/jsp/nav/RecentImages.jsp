<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>

<jsp:useBean id="recentImages" type="java.util.Collection" scope="request"/>
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title">Photos</tiles:put>
  <tiles:put type="string" name="content">
    <table border="0" cellpadding="2" cellspacing="0" align="center">
      <tr>
        <logic:iterate name="recentImages" id="image" type="net.kukido.blog.datamodel.Attachment" length="2">
        <bean:define id="fileName" name="image" property="fileName" />
        <bean:define id="attachmentId" name="image" property="attachmentId" />
        <bean:define id="title" name="image" property="title" />
        <td><a href="galleries/<%= attachmentId %>"><img class="thumbnail" height="75" width="75" title="<%= title %>" alt="<%= title %>" src="attachments/thumbs/<%= fileName %>" /></a></td>
        </logic:iterate>
      </tr>
      
      <tr>
        <logic:iterate name="recentImages" id="image" type="net.kukido.blog.datamodel.Attachment" length="2" offset="2">
        <bean:define id="fileName" name="image" property="fileName" />
        <bean:define id="attachmentId" name="image" property="attachmentId" />
        <bean:define id="title" name="image" property="title" />
        <td><a href="galleries/<%= attachmentId %>"><img class="thumbnail" height="75" width="75" title="<%= title %>" alt="<%= title %>" src="attachments/thumbs/<%= fileName %>" /></a></td>
        </logic:iterate>
      </tr>
      <tr>
        <logic:iterate name="recentImages" id="image" type="net.kukido.blog.datamodel.Attachment" length="2" offset="4">
        <bean:define id="fileName" name="image" property="fileName" />
        <bean:define id="attachmentId" name="image" property="attachmentId" />
        <bean:define id="title" name="image" property="title" />
        <td><a href="galleries/<%= attachmentId %>"><img class="thumbnail" height="75" width="75" title="<%= title %>" alt="<%= title %>" src="attachments/thumbs/<%= fileName %>" /></a></td>
        </logic:iterate>
      </tr>
    </table>
  </tiles:put>
</tiles:insert>
