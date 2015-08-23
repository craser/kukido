<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%-- <jsp:include page="MoPho.jsp" /> --%>
<%-- <jsp:include page="/jsp/nav/SearchNav.jsp" /> --%>
<%--
<jsp:include page="/CyclingStats.do" />
<jsp:include page="/RecentImages.do">
  <jsp:param name="numEntries" value="6" />
</jsp:include>
--%>
<logic:present name="user" scope="session">
  <jsp:include page="/RecentCommentsNav.do">
	<jsp:param name="numComments" value="10" />
  </jsp:include>
</logic:present>
<%--
<jsp:include page="/MostUsedTagsNav.do">
  <jsp:param name="title" value="Most-Used Tags" />
  <jsp:param name="numTags" value="50" />
</jsp:include>
<jsp:include page="/RecentTagsNav.do">
  <jsp:param name="title" value="Recent Tags" />
  <jsp:param name="numTags" value="50" />
</jsp:include>
--%>
<%-- jsp:include page="/DeliciousLinks" / --%>
<%-- jsp:include page="/GoogleShared" / --%>
<%-- <jsp:include page="/jsp/nav/BlogrollNav.jsp" /> --%>
<%-- <jsp:include page="/jsp/nav/FeedsNav.jsp" /> --%>
<%-- <jsp:include page="/jsp/nav/FirefoxNav.jsp" /> --%>

