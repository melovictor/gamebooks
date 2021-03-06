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
		<span class="ffMainAttribute"><spring:message code="page.ff20.attribute.honor" /></span>
		<div class="ffMainAttributeValue" data-attribute-skill>${data.honor}</div>
	</div>
	<div class="ffMainAttribute3">
		<span class="ffMainAttribute"><spring:message code="page.ff.attribute.provision" /></span>
		<div data-items="true">
			<c:set var="isFirst" value="true" />
			<c:forEach var="item" items="${data.provisions}">${isFirst ? '' : ','}
				<c:set var="isFirst" value="false" />
				<span data-item-id="${item.id}" data-item-provision>${item.amount} x ${item.name}</span></c:forEach><c:forEach var="item" items="${data.potions}">${isFirst ? '' : ','}
				<c:set var="isFirst" value="false" />
				<span data-item-id="${item.id}" data-item-potion>${item.amount} x ${item.name}</span></c:forEach>
		</div>
	</div>
	<div class="ffMainAttribute3">
		<span class="ffMainAttribute"><spring:message code="page.ff20.attribute.specialSkill" /></span>
		${data.specialSkill.description}
		<c:if test="${data.specialSkill == 'KJUDZSUTSZU'}">
			<c:if test="${data.arrows.willowLeaf > 0}">
				<br /><spring:message code="page.ff20.attribute.arrow.willowLeaf" arguments="${data.arrows.willowLeaf}" />
			</c:if>
			<c:if test="${data.arrows.bowelRaker > 0}">
				<br /><spring:message code="page.ff20.attribute.arrow.bowelRaker" arguments="${data.arrows.bowelRaker}" />
			</c:if>
			<c:if test="${data.arrows.armourPiercer > 0}">
				<br /><spring:message code="page.ff20.attribute.arrow.armourPiercer" arguments="${data.arrows.armourPiercer}" />
			</c:if>
			<c:if test="${data.arrows.hummingBulb > 0}">
				<br /><spring:message code="page.ff20.attribute.arrow.hummingBulb" arguments="${data.arrows.hummingBulb}" />
			</c:if>
			<c:if test="${data.arrows.tsunevara > 0}">
				<br /><spring:message code="page.ff20.attribute.arrow.tsunevara" arguments="${data.arrows.tsunevara}" />
			</c:if>
		</c:if>
	</div>

	<tiles:insertTemplate template="../charpage/eq.jsp" />
	<tiles:insertTemplate template="../charpage/shadow.jsp" />
	<tiles:insertTemplate template="../charpage/map.jsp" />
	<tiles:insertTemplate template="../charpage/notes.jsp" />
</div>
