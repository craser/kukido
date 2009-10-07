<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<jsp:useBean id="mophoDao" scope="page" class="net.kukido.blog.dataaccess.MophoDao"/>
<bean:define id="mophoEntry" name="mophoDao" property="latest" type="net.kukido.blog.datamodel.MophoEntry"/>
<table class="mopho" align="center" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="mophoimage">
    <html:link page="/mopho/"><img src="mopho/<%= mophoEntry.getFileName() %>" height="128" width="170" alt="<%= mophoEntry.getTitle() %>" /></html:link>
    </td>
  </tr>
  <tr>
    <td class="mophotext"><bean:write name="mophoEntry" property="title"/></td>
  </tr>
</table>