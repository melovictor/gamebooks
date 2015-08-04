<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="l" %>

<div id="accessLogs">

  <h1><spring:message code="page.logs.baseLog" /></h1>
  <c:url var="logUrl" value="/logs/log-base.log/log" />
  <a href="${logUrl}"><spring:message code="page.logs.baseLog" /></a><br />

  <c:forEach items="${logFiles.byDate}" var="byDate">
    <h1>${byDate.date}</h1>
    <c:forEach items="${byDate.byUser}" var="byUser">
      <h2>${byUser.userId}</h2>
      <c:forEach items="${byUser}" var="log">
        <c:url var="logUrl" value="logs/log-${log.userId}-${log.timestamp}.log/log" />
        <a href="${logUrl}">${log.loginDateTime}</a><br />
      </c:forEach>
    </c:forEach>
  </c:forEach>
  
  <h1><spring:message code="page.logs.archivedLogs" /></h1>
  <c:forEach items="${archivedLogFiles}" var="log">
	  <c:url var="logUrl" value="/logs/archive.${log}.zip/zip" />
	  <a href="${logUrl}">${log}</a><br />
  </c:forEach>  
  
</div>
