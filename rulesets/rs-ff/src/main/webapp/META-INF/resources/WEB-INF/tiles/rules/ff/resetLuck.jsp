<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2><spring:message code="page.ff.rules.resetLuck.title" /></h2>

<c:set var="resetLuckKey" value="page.ff.rules.resetLuck.text" />
<c:if test="${stdHelp_hasPotions}">
    <c:set var="resetLuckKey" value="page.ff.rules.resetLuck.text.potions" />
</c:if>
<spring:message code="${resetLuckKey}" />
