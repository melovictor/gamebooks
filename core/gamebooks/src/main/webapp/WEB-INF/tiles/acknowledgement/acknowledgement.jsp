<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<spring:message code="page.acknowledgement.intro" />
<ul id="acknowledgementList">
	<c:forEach var="user" items="${users}">
		<li>${user}</li>
	</c:forEach>
</ul>
<spring:message code="page.acknowledgement.outro" />
