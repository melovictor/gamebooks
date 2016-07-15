<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<spring:message code="page.login.menu.login" />
<br />
<c:if test="${not empty nextUpdateTime}">
	<spring:message code="page.warning.serverRestart" arguments="${nextUpdateTime}" />
</c:if>
