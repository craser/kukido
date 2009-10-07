<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<nested:form action="UserUpdate" focus="password">
  <html:errors />
  <table>
    <tr>
      <td colspan="2"><h2><nested:write property="user.userName" />'s User Settings</h2></td>
    </tr>
    <tr>
      <td class="inputlabel">Password:</td>
      <td><nested:password property="password" /></td>
    </tr>
    <tr>
      <td class="inputlabel">Email:</td>
      <td><nested:text property="user.email" /></td>
    </tr>
    <tr>
      <td class="inputlabel">URL:</td>
      <td><nested:text property="user.url" /></td>
    </tr>
    <tr>
      <td class="inputlabel">New Password:</td>
      <td><nested:password property="newPassword" /></td>
    </tr>
    <tr>
      <td class="inputlabel">Confirm New Password:</td>
      <td><nested:password property="confirmNewPassword" /></td>
    </tr>
    <tr>
      <td colspan="2"><nested:submit title="save" value="save" /></td>
    </tr>
  </table>
</nested:form>