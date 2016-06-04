<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<h1>
    <spring:message code="page.menu.ff.adventureSheet" />
</h1>
<div id="inventory">
    <tiles:insertTemplate template="../charpage/ssl3.jsp" />
    <div class="ffMainAttribute3">
        <span class="ffMainAttribute"><spring:message code="page.ff.attribute.gold" /></span>
        <div class="ffMainAttributeValue" data-attribute-gold><fmt:formatNumber value="${data.gold}" groupingUsed="true" /></div>
    </div>
    <div class="ffMainAttribute32" data-items="true">
        <span class="ffMainAttribute"><spring:message code="page.ff8.attribute.spells" /></span>
        <c:forEach var="item" varStatus="status" items="${data.spells}">
            <c:if test="${item.itemType.consumable}">
                <span data-item-id="${item.id}" data-item-provision>${item.name}</span>${!status.last ? ',' : ''}
            </c:if>
            <c:if test="${!item.itemType.consumable}">
                ${item.name}${!status.last ? ',' : ''}
            </c:if>
        </c:forEach>
    </div>

    <tiles:insertTemplate template="../charpage/eq.jsp" />
    <tiles:insertTemplate template="../charpage/shadow.jsp" />
     <h2>
         <span data-map-ff8="mapDialog">
             <spring:message code="page.menu.book.inventory.map" />
         </span>
     </h2>
     <div id="mapDialog" class="popupMap" title="${bookInfo.title}">
       <img src="" />
     </div>
    <tiles:insertTemplate template="../charpage/notes.jsp" />
</div>
