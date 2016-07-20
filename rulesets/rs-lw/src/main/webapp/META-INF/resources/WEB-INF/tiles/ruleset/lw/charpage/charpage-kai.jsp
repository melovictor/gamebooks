<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${hasInventory || hasMap}">
	<h1>
		<spring:message code="page.menu.lw.adventureSheet" />
	</h1>
	<div id="inventory">
		<div class="lwMainAttribute2">
			<span class="lwMainAttribute"><spring:message code="page.lw.attribute.combatSkill" /></span>
			<div class="lwMainAttributeValue" data-attribute-skill>${data.combatSkill}</div>
		</div>
		<div class="lwMainAttribute2">
			<span class="lwMainAttribute"><spring:message code="page.lw.attribute.endurance" /></span>
			<span class="lwInitialMainAttribute"><spring:message code="page.lw.attribute.endurance.initial" /> ${data.initialEndurance}</span>
			<div class="lwMainAttributeValue" data-attribute-stamina>${data.endurance}</div>
		</div>



	   <tiles:insertTemplate template="shadow.jsp" />
	   <tiles:insertTemplate template="map.jsp" />
	   <tiles:insertTemplate template="notes.jsp" />
	</div>
</c:if>
