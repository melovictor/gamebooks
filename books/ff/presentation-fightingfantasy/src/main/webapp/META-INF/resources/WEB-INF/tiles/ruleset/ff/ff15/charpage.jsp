<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${hasInventory || hasMap}">
    <h1>
        <spring:message code="page.menu.ff.adventureSheet" />
    </h1>
    <div id="inventory">
        <tiles:insertTemplate template="../charpage/ssl4.jsp" />

        <div class="ffMainAttribute4">
            <span class="ffMainAttribute"><spring:message code="page.ff15.attribute.teka" /></span>
            <div class="ffMainAttributeValue" data-attribute-gold><fmt:formatNumber value="${data.gold}" groupingUsed="true" /></div>
        </div>

        <div class="ffMainAttribute4">
            <span class="ffMainAttribute"><spring:message code="page.ff15.attribute.weaponStrength" /></span>
            <span class="ffInitialMainAttribute"><spring:message code="page.ff15.attribute.weaponStrength.initial" /> ${data.ship.initialWeaponStrength}</span>
            <div class="ffMainAttributeValue" data-attribute-fear>${data.ship.weaponStrength}</div>
        </div>
        <div class="ffMainAttribute4">
            <span class="ffMainAttribute"><spring:message code="page.ff15.attribute.shield" /></span>
            <span class="ffInitialMainAttribute"><spring:message code="page.ff15.attribute.shield.initial" /> ${data.ship.initialShield}</span>
            <div class="ffMainAttributeValue" data-attribute-fear>${data.ship.shield}</div>
        </div>
        <div class="ffMainAttribute4">
            <span class="ffMainAttribute"><spring:message code="page.ff15.attribute.smartMissile" /></span>
            <div class="ffMainAttributeValue">${data.ship.smartMissile}</div>
        </div>

        <div class="ffMainAttribute4">
		    <span class="ffMainAttribute"><spring:message code="page.ff15.attribute.pepPill" /></span>
		    <div data-items="true">
		        <c:set var="isFirst" value="true" />
		        <c:forEach var="item" items="${data.provisions}"><c:if test="${!isFirst}">,</c:if>
		            <c:set var="isFirst" value="false" />
		            <span data-item-id="${item.id}" data-item-provision>${item.amount} x ${item.name}</span>
	            </c:forEach>
		    </div>
        </div>

        <tiles:insertTemplate template="../charpage/eq.jsp" />
        <tiles:insertTemplate template="../charpage/notes.jsp" />
    </div>
</c:if>
