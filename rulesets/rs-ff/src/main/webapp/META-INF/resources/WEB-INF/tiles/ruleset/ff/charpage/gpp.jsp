<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="width">
    <tiles:insertAttribute defaultValue="3" name="width" />
</c:set>
<div class="ffMainAttribute${width}">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.gold" /></span>
    <div class="ffMainAttributeValue" data-attribute-gold><fmt:formatNumber value="${data.gold}" groupingUsed="true" /></div>
</div>
<div class="ffMainAttribute${width}">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.provision" /></span>
    <div data-items="true">
        <c:set var="isFirst" value="true" />
        <c:forEach var="item" items="${data.provisions}"><c:if test="${!isFirst}">,</c:if>
           <c:set var="isFirst" value="false" />
           <span data-item-id="${item.id}" data-item-provision>${item.amount} x ${item.name}</span></c:forEach>
    </div>
</div>
<div class="ffMainAttribute${width}">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.potions" /></span>
    <div data-items="true">
        <c:set var="isFirst" value="true" />
        <c:forEach var="item" items="${data.potions}"><c:if test="${!isFirst}">,</c:if>
           <c:set var="isFirst" value="false" />
           <span data-item-id="${item.id}" data-item-potion>${item.name}<c:if test="${item.dose > 1}">
             [<spring:message code="page.ff.attribute.provision.doses" arguments="${item.dose}" />]</c:if></span></c:forEach>
    </div>
</div>
