<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>

<jsp:useBean id="tags" type="java.util.Collection" scope="request" />
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title"><bean:write  name="title" scope="request" /></tiles:put>
  <tiles:put type="string" name="content">
    <div class="tagcloud">
      <logic:iterate name="tags" id="tag" type="net.kukido.blog.datamodel.Tag">
        <bean:define id="tagName" name="tag" property="name" />
        <a href="tags/<%= tagName %>"><bean:write name="tag" property="name" /></a>
      </logic:iterate>
    </div>
  </tiles:put>
</tiles:insert>