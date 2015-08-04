<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="ffEquipmentFullWidth" data-items="true">
    <span class="ffMainAttribute"><spring:message code="page.ff.equipment" /></span>
    <c:set var="isFirst" value="true" />
    <c:forEach var="item" items="${charEquipments.items}"><c:if test="${!isFirst}">,</c:if>
        <c:set var="isFirst" value="false" />
        <c:if test="${item.equipInfo.equippable}">
            [<c:if test="${item.equipInfo.equipped}">*</c:if><c:if test="${!item.equipInfo.equipped}">&nbsp;&nbsp;</c:if>]&nbsp;<!--
        --></c:if><!--
        --><span data-item-id="${item.id}" data-item-equippable="${item.equipInfo.equippable}" data-item-equipped="${item.equipInfo.equipped}" data-item-removable="${item.equipInfo.removable}">${item.name}</span><!--
    --></c:forEach>

    <c:if test="${equipmentIncludesPotions}">
	    <c:forEach var="item" items="${charEquipments.potions}"><c:if test="${!isFirst}">,</c:if>
	       <c:set var="isFirst" value="false" />
	       <span data-item-id="${item.id}" data-item-potion>${item.name}<c:if test="${item.dose > 1}">
	         [<spring:message code="page.ff.attribute.provision.doses" arguments="${item.dose}" />]</c:if></span></c:forEach>
    </c:if>

</div>
