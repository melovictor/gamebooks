<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2><spring:message code="page.ff.rules.resetStamina.title" /></h2>
<spring:message code="page.ff.rules.resetStamina.text.intro" />

<c:if test="${stdHelp_hasFood}">
    <spring:message code="page.ff.rules.resetStamina.text.startWithFood" arguments="${stdHelp_foodAmount}" />
</c:if>
<c:if test="${!stdHelp_hasFood}">
    <spring:message code="page.ff.rules.resetStamina.text.startWithoutFood" />
</c:if>

<c:set var="resetStaminaClosingKey" value="page.ff.rules.resetStamina.text.closing" />
<c:if test="${stdHelp_hasPotions}">
    <c:set var="resetStaminaClosingKey" value="page.ff.rules.resetStamina.text.closing.potions" />
</c:if>
<spring:message code="${resetStaminaClosingKey}" />
