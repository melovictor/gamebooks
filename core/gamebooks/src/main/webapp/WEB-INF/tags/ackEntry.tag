<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@attribute name="role" required="true" %>

<div class="roleTitle"><spring:message code="page.acknowledgement.title.${role}" /></div>
<div class="roleHolder">
    <c:forEach items="${users[role]}" var="userName" varStatus="stat">
        ${userName}
        <c:if test="${!stat.last}">
            <br />
        </c:if>
    </c:forEach>
</div>
