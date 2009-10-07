<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>

<jsp:useBean id="entries" type="java.util.Collection" scope="request" />
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title">Recent Comments (<html:link href="comments.xml">RSS</html:link>)</tiles:put>
  <tiles:put type="string" name="content">
    <ul class="navmenu">
      <logic:iterate name="entries" id="entry" type="net.kukido.blog.datamodel.LogEntry">
        <li><dmg:entrylink entry="entry" styleClass="headerlink"><bean:write name="entry" property="title" /></dmg:entrylink>
            <logic:iterate name="entry" property="comments" id="comment">
              <dmg:entrylink comment="comment"><bean:write name="comment" property="datePosted" format="MM/dd/yy" />: <bean:write name="comment" property="userName" /></dmg:entrylink>
            </logic:iterate>
        </li>
      </logic:iterate>
    </ul>
  </tiles:put>
</tiles:insert>