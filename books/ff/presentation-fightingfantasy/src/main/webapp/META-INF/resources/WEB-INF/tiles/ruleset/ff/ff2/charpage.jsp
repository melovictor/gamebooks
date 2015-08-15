<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:if test="${hasInventory || hasMap}">
    <h1>
        <spring:message code="page.menu.ff.adventureSheet" />
    </h1>
    <div id="inventory">
        <tiles:insertTemplate template="../charpage/ssl3.jsp" />
		<div class="ffMainAttribute3">
		    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.gold" /></span>
		    <div class="ffMainAttributeValue" data-attribute-gold><fmt:formatNumber value="${charEquipments.gold}" groupingUsed="true" /></div>
		</div>
        <div class="ffMainAttribute32">
            <span class="ffMainAttribute"><spring:message code="page.ff2.attribute.spells" /></span>
            <div data-items="true">
                <c:set var="isFirst" value="true" />
                <c:forEach var="item" items="${charEquipments.spells}"><c:if test="${!isFirst}">,</c:if>
                   <c:set var="isFirst" value="false" />
                   ${item.amount}&nbsp;x&nbsp;<span data-item-id="${item.id}" data-item-provision>${item.name}</span></c:forEach>
            </div>
        </div>

       <tiles:insertTemplate template="../charpage/eq.jsp" />
       <tiles:insertTemplate template="../charpage/notes.jsp" />
    </div>
</c:if>
