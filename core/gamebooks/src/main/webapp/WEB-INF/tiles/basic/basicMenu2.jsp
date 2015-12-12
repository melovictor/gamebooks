<%@taglib tagdir="/WEB-INF/tags" prefix="l"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:url var="bookListUrl" value="/booklist" />
<c:url var="settingsUrl" value="/settings" />
<c:url var="logsUrl" value="/logs" />
<c:url var="rulesUrl" value="/rules" />
<c:url var="zagorPingUrl" value="/ping" />
<tiles:insertTemplate template="../basic/basicMenu.jsp" />
<div id="pageTitle">
  <ul>
    <l:menuLink selectedId="bookList" url="${bookListUrl}" labelKey="page.title.books" />
    <l:menuLink selectedId="settings" url="${settingsUrl}" labelKey="page.title.settings" />
    <l:menuLink selectedId="rules" url="${rulesUrl}" labelKey="page.title.rules" />
    <c:if test="${user.admin}">
      <l:menuLink selectedId="logs" url="${logsUrl}" labelKey="page.title.logs" />
      <l:menuLink selectedId="ping" url="${zagorPingUrl}" labelKey="page.title.zagorping" />
    </c:if>
  </ul>
</div>
