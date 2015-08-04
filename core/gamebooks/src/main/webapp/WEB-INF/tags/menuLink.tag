<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@attribute name="selectedId" required="true" %>
<%@attribute name="url" required="true" %>
<%@attribute name="labelKey" required="false" %>

<li>
  <c:if test="${selectedMenu != selectedId}">
    <a href="${url}">
      <spring:message code="${labelKey}" />
    </a>
  </c:if>
  <c:if test="${selectedMenu == selectedId}">
    <spring:message code="${labelKey}" />
  </c:if>
</li>