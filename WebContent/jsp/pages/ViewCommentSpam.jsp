<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean id="comments" scope="request" type="java.util.Collection" />

<tiles:insert definition="pageLayout">
  <tiles:put name="title">Trapped Comment Spam</tiles:put>
  
  <tiles:put type="string" name="content">
    <div class="entrymetainfo">(<html:link action="DeleteCommentSpam">delete all comment spam</html:link>)</div>
    <tiles:insert definition="comments" flush="false">
      <tiles:put name="title" value="Trapped Spam" />
      <tiles:put name="comments" beanName="comments" />
      <tiles:put name="showRaw" value="true" />
    </tiles:insert>
  </tiles:put>
</tiles:insert>

