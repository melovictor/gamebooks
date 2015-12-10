<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${hasInventory || hasMap}">
	<h1>
		<spring:message code="page.menu.ff.adventureSheet" />
	</h1>
	<div id="inventory">

		<c:set var="width">
			<tiles:insertAttribute defaultValue="3" name="width" />
		</c:set>
		<div class="ffMainAttribute${width}">
			<span class="ffMainAttribute"><spring:message code="page.ff.attribute.skill" /></span>
			<span class="ffInitialMainAttribute"><spring:message code="page.ff.attribute.skill.initial" /> ${charEquipments.initialSkill}</span>
			<div class="ffMainAttributeValue" data-attribute-skill>${charEquipments.skill}
				<c:if test="${!charEquipments.usedLibra}">
                    <button data-libra-reset="skill">
                        <spring:message code="page.ff.attribute.skill.reset" />
                    </button>
				</c:if>
			</div>
		</div>
		<div class="ffMainAttribute${width}">
			<span class="ffMainAttribute"><spring:message code="page.ff.attribute.stamina" /></span>
			<span class="ffInitialMainAttribute"><spring:message code="page.ff.attribute.stamina.initial" /> ${charEquipments.initialStamina}</span>
			<div class="ffMainAttributeValue" data-attribute-stamina>${charEquipments.stamina}
				<c:if test="${!charEquipments.usedLibra}">
                    <button data-libra-reset="stamina">
                        <spring:message code="page.ff.attribute.stamina.reset" />
                    </button>
				</c:if>
			</div>
		</div>
		<div class="ffMainAttribute${width}">
			<span class="ffMainAttribute"><spring:message code="page.ff.attribute.luck" /></span>
			<span class="ffInitialMainAttribute"><spring:message code="page.ff.attribute.luck.initial" /> ${charEquipments.initialLuck}</span>
			<div class="ffMainAttributeValue" data-attribute-luck>${charEquipments.luck}
				<c:if test="${!charEquipments.usedLibra}">
					<button data-libra-reset="luck">
                        <spring:message code="page.ff.attribute.luck.reset" />
                    </button>
				</c:if>
			</div>
		</div>



		<tiles:insertTemplate template="../charpage/gpp.jsp" />
		<tiles:insertTemplate template="../charpage/eq.jsp" />
		<tiles:insertTemplate template="../charpage/map.jsp" />
		<tiles:insertTemplate template="../charpage/notes.jsp" />
	</div>
</c:if>
