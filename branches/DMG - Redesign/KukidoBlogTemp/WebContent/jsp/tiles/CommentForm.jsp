<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<tiles:useAttribute id="entry" name="entry" classname="net.kukido.blog.datamodel.LogEntry" />

<logic:equal name="entry" property="allowComments" value="true">
  <div class="comments">
    <nested:form action="CreateComment.do" method="POST">
      <nested:hidden property="comment.entryId" value="<%= String.valueOf(entry.getEntryId()) %>" />
        <h2><a name="commentform">Post a Comment</a></h2>
        <html:errors/>
        <table>
          <tr>
            <td>Name:</td>
            <td><nested:text size="30" property="comment.userName" /></td>
          </tr>
          <tr>
            <td>Email (Never, ever displayed.)</td>
            <td><nested:text size="30" property="comment.userEmail" /></td>
          </tr>
          <tr>
            <td>URL:</td>
            <td><nested:text size="30" property="comment.userUrl" /></td>
          </tr>
          <tr>
            <td>Remember me next time.</td>
            <td><nested:checkbox property="remember" /></td>
          <tr>
            <td colspan="2">Comments (Sorry, no HTML allowed.  Space paragraphs with a blank line.):
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <nested:textarea property="comment.comment" style="width: 100%" cols="40" rows="10" />
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <nested:submit value="Speak" />
            </td>
          </tr>
        </table>
    </nested:form>
  </div>
</logic:equal>
<logic:notEqual name="entry" property="allowComments" value="true">
  <div class="comment"><i>The author has disabled comments for this post.</i></div>
</logic:notEqual>