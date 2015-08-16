<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="bookRules">
  <h1 class="bookTitle"><spring:message code="page.rules.bookTitle" arguments="${helpDescriptor.info.title}" /></h1>

  <c:forEach var="section" items="${helpDescriptor.sections}">
    <jsp:include page="${section.location}/${section.jsp}.jsp" />
  </c:forEach>
</div>
