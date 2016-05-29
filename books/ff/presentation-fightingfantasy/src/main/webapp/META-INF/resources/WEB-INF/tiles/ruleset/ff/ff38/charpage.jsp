<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${hasInventory || hasMap}">
	<h1>
        <spring:message code="page.menu.ff.adventureSheet" />
	</h1>
    <div id="inventory">
        <tiles:insertTemplate template="../charpage/ssl4.jsp" />

		<div class="ffMainAttribute4">
		    <span class="ffMainAttribute"><spring:message code="page.ff38.attribute.faith" /></span>
		    <div class="ffMainAttributeValue">${data.faith}</div>
		</div>

		<div class="ffMainAttribute4">
		    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.gold" /></span>
		    <div class="ffMainAttributeValue">${data.gold}</div>
		</div>
        <div class="ffMainAttribute4">
            <span class="ffMainAttribute"><spring:message code="page.ff.attribute.provision" /></span>
            <div data-items="true">
                <c:set var="isFirst" value="true" />
                <c:forEach var="item" items="${data.provisions}"><c:if test="${!isFirst}">,</c:if>
                    <c:set var="isFirst" value="false" />
                    <span data-item-id="${item.id}" data-item-provision>${item.amount} x ${item.name}</span></c:forEach><c:forEach var="item" items="${data.potions}"><c:if test="${!isFirst}">,</c:if>
		           <c:set var="isFirst" value="false" />
		           <span data-item-id="${item.id}" data-item-potion>${item.name}<c:if test="${item.dose > 1}">
		             [<spring:message code="page.ff.attribute.provision.doses" arguments="${item.dose}" />]</c:if></span></c:forEach>
            </div>
        </div>
        <div class="ffMainAttribute4">
            <span class="ffMainAttribute"><spring:message code="page.ff38.attribute.afflictions" /></span>
            <div data-items="true">
                <c:set var="isFirst" value="true" />
                <c:forEach var="item" items="${data.shadows}"><c:if test="${item.id > 4100 && item.id < 4200}"><c:if test="${!isFirst}">,</c:if>
                    <c:set var="isFirst" value="false" />${item.name}</c:if></c:forEach>
            </div>
        </div>
        <div class="ffMainAttribute4">
            <span class="ffMainAttribute"><spring:message code="page.ff38.attribute.spells" /></span>
            <div data-items="true">
                <c:set var="isFirst" value="true" />
                <c:forEach var="item" items="${data.shadows}"><c:if test="${item.id > 4200 && item.id < 4300}"><c:if test="${!isFirst}">,</c:if>
                    <c:set var="isFirst" value="false" /><span data-item-id="${item.id}" data-item-provision>${item.name}</span></c:if></c:forEach>
            </div>
        </div>


		<tiles:insertTemplate template="../charpage/eq.jsp" />
		<tiles:insertTemplate template="../charpage/map.jsp" />
		<tiles:insertTemplate template="../charpage/notes.jsp" />
	</div>
</c:if>
