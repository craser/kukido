<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<tiles:insert definition="pageLayout">
    <tiles:put type="string" name="content">
    <!-- Login.jsp -->
    <jsp:include page="UserUpdateForm.jsp" />
    <!-- end of Login.jsp -->
    </tiles:put>
    <tiles:put type="string" name="sidebar">
        <tiles:insert definition="sidebarElement" flush="false">
          <tiles:put type="string" name="title">Geotagging</tiles:put>
          <tiles:put type="string" name="content">
            <html:link action="GeotagMaps">Geotag all maps</html:link>
          </tiles:put>
        </tiles:insert>
    </tiles:put>
</tiles:insert>