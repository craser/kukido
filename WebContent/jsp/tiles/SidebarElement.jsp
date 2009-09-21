<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<tiles:useAttribute name="title" id="title" ignore="true" />
<div class="navitem">
<logic:notEmpty name="title">
  <span class="navtitle"><tiles:getAsString name="title" /></span>
</logic:notEmpty>
<tiles:get name="content" />
</div>
