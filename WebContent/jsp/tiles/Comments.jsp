<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<tiles:useAttribute id="comments" name="comments" classname="java.util.Collection" />
<tiles:useAttribute id="showRaw" name="showRaw" classname="java.lang.String" ignore="true" />
<div class="comments">
  <h2><a name="comments"><tiles:getAsString name="title" /></a></h2>
  <nested:iterate name="comments" id="comment" type="net.kukido.blog.datamodel.Comment">
    <div class="comment">
      <div class="commentcontent">
        <logic:equal name="showRaw" value="true">
          <bean:write name="comment" property="comment" filter="false" />
        </logic:equal>
        <logic:notEqual name="showRaw" value="true">
          <bean:write name="comment" property="commentHtml" filter="false" />
        </logic:notEqual>
      </div>

      <div class="commentmetainfo"> 
        <logic:present name="comment" property="userUrl">
          <html:link href="<%= comment.getUserUrl() %>"><bean:write name="comment" property="userName" /></html:link>
        </logic:present>
        <logic:notPresent name="comment" property="userUrl">
          <bean:write name="comment" property="userName" />
        </logic:notPresent>
        | <dmg:entrylink comment="comment"><bean:write name="comment" property="datePosted" format="MM/dd/yy h:mma" /></dmg:entrylink>
        <logic:present name="user" scope="session">
          | <html:link onclick="return confirm('Delete this comment?');" action="DeleteComment" paramId="commentId" paramName="comment" paramProperty="commentId">delete</html:link>
          <logic:equal name="comment" property="spam" value="true">
            | <html:link onclick="return confirm('Mark this comment as NOT SPAM?');" action="UnmarkCommentSpam" paramId="commentId" paramName="comment" paramProperty="commentId">not spam</html:link>
          </logic:equal>
          <logic:equal name="comment" property="spam" value="false">
            | <html:link action="MarkCommentSpam" paramId="commentId" paramName="comment" paramProperty="commentId">mark as spam</html:link>
          </logic:equal>
        </logic:present>
      </div>
    </div>
  </nested:iterate>
</div>
