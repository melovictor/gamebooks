<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="l" %>

<c:if test="${hasInventory || hasMap}">
	<h1>
		<spring:message code="page.menu.lw.adventureSheet" />
	</h1>
	<div id="inventory">
		<div class="lwMainAttribute4">
			<span class="lwMainAttribute"><spring:message code="page.lw.attribute.combatSkill" /></span>
			<div class="lwMainAttributeValue" data-attribute-skill>${data.combatSkill}</div>
		</div>
		<div class="lwMainAttribute4">
			<span class="lwMainAttribute"><spring:message code="page.lw.attribute.endurance" /></span>
			<span class="lwInitialMainAttribute"><spring:message code="page.lw.attribute.endurance.initial" /> ${data.initialEndurance}</span>
			<div class="lwMainAttributeValue" data-attribute-stamina>${data.endurance}</div>
		</div>
		<div class="lwMainAttribute4">
			<span class="lwMainAttribute"><spring:message code="page.lw.money" /></span>
			<spring:message code="page.lw.money.goldCrowns" arguments="${data.character.money.goldCrowns }" />
		</div>
		<div class="lwMainAttribute4" id="lwDisciplinesList">
			<span class="lwMainAttribute"><spring:message code="page.lw.attribute.kaiDisciplines" /></span>
			<c:if test="${data.character.initialized}">
                <l:kaiDisc disc="camouflage"/>
                <l:kaiDisc disc="hunting"/>
                <l:kaiDisc disc="sixthSense"/>
                <l:kaiDisc disc="tracking"/>
                <l:kaiDisc disc="healing"/>
				<c:if test="${not empty data.weaponskillWeapon}">
					<c:set var="weaponskillName">
						<spring:message code="page.lw.attribute.kai.weaponskill.title" />
					</c:set>
					<c:set var="weaponskillWeapon">
						<spring:message code="page.lw.attribute.kai.weaponskill.${data.weaponskillWeapon}" />
					</c:set>
				</c:if>
				<span data-id="weaponskill" data-available="${data.character.kaiDisciplines.weaponskill.weaponskillObtained}"><spring:message code="page.lw.attribute.kai.weaponskill" arguments="${weaponskillName},${weaponskillWeapon}" /></span>
                <l:kaiDisc disc="mindshield"/>
                <l:kaiDisc disc="mindblast"/>
                <l:kaiDisc disc="animalKinship"/>
                <l:kaiDisc disc="mindOverMatter"/>
			</c:if>
		</div>


		<div class="lwMainAttribute3">
			<span class="lwMainAttribute"><spring:message code="page.lw.equipment.weapons" /></span>
			<c:forEach var="item" items="${data.weapons}">
				<div class="lwSlot" data-item-equipped="${item.equipInfo.equipped}" data-item-id="${item.id}"><span>${item.name}</span>
					<c:if test="${item.id != 'defWpn'}">
						<span class="remove">&#xf00d;</span>
					</c:if>
				</div>
			</c:forEach>
		</div>
		<div class="lwMainAttribute3">
			<span class="lwMainAttribute"><spring:message code="page.lw.equipment.normalEquipments" /></span>
			<c:forEach var="item" items="${data.normalEquipment}">
				<div class="lwSlot" title="${item.description}" data-item-id="${item.id}" data-item-type="${item.itemType}">
					<span>${item.name}</span>
					<span class="remove">&#xf00d;</span>
				</div>
			</c:forEach>
		</div>
		<div class="lwMainAttribute3">
			<span class="lwMainAttribute"><spring:message code="page.lw.equipment.specialEquipments" /></span>
			<c:forEach var="item" items="${data.specialEquipment}">
				<div class="lwSlot" title="${item.description}" data-item-id="${item.id}">
					${item.name}<span class="remove">&#xf00d;</span>
				</div>
			</c:forEach>
		</div>
		


		<tiles:insertTemplate template="shadow.jsp" />
		<tiles:insertTemplate template="map.jsp" />
		<tiles:insertTemplate template="notes.jsp" />
	</div>
</c:if>
