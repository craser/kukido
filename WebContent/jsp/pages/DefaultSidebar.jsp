<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%-- <jsp:include page="MoPho.jsp" /> --%>
<%-- <jsp:include page="/jsp/nav/SearchNav.jsp" /> --%>
<%--
<jsp:include page="/RecentImages.do">
  <jsp:param name="numEntries" value="6" />
</jsp:include>
--%>
<logic:present name="user" scope="session">
  <jsp:include page="/RecentCommentsNav.do">
	<jsp:param name="numComments" value="10" />
  </jsp:include>
</logic:present>

