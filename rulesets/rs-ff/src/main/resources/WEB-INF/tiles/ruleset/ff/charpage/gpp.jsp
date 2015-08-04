<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="ffMainAttribute3">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.gold" /></span>
    <div class="ffMainAttributeValue" data-attribute-gold><fmt:formatNumber value="${charEquipments.gold}" groupingUsed="true" /></div>
</div>
<div class="ffMainAttribute3">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.provision" /></span>
    <div data-items="true">
        <c:set var="isFirst" value="true" />
        <c:forEach var="item" items="${charEquipments.provisions}"><c:if test="${!isFirst}">,</c:if>
           <c:set var="isFirst" value="false" />
           <span data-item-id="${item.id}" data-item-provision>${item.amount} x ${item.name}</span></c:forEach>
    </div>
</div>
<div class="ffMainAttribute3">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.potions" /></span>
    <div data-items="true">
        <c:set var="isFirst" value="true" />
        <c:forEach var="item" items="${charEquipments.potions}"><c:if test="${!isFirst}">,</c:if>
           <c:set var="isFirst" value="false" />
           <span data-item-id="${item.id}" data-item-potion>${item.name}<c:if test="${item.dose > 1}">
             [<spring:message code="page.ff.attribute.provision.doses" arguments="${item.dose}" />]</c:if></span></c:forEach>
    </div>
</div>
