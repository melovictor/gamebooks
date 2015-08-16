<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="equipmentTitleKey" value="page.ff.rules.equipment.title" />
<c:set var="equipmentTextKey" value="page.ff.rules.equipment.text" />
<c:if test="${stdHelp_hasPotions}">
    <c:set var="equipmentTitleKey" value="page.ff.rules.equipment.title.potions" />
    <c:set var="equipmentTextKey" value="page.ff.rules.equipment.text.potions" />
</c:if>

<c:set var="equipmentLanternText" value="" />
<c:if test="${stdHelp_hasLantern}">
    <spring:message code="page.ff.rules.equipment.text.lantern" var="equipmentLanternText" />
</c:if>

<h2><spring:message code="${equipmentTitleKey}" /></h2>
<spring:message code="${equipmentTextKey}" arguments="${equipmentLanternText}|${stdHelp_potionAmount}|${stdHelp_potionAmountTimes}" argumentSeparator="|" />
