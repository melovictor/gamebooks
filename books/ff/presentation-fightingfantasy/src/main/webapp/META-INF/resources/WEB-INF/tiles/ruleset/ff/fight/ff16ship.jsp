<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:set var="firstEnemy" value="${empty param.id}" />

<div>
	<span class="skillTitle">
		<spring:message code="page.ff16.attribute.crewStrike" />
	</span>
	<span class="staminaTitle">
		<spring:message code="page.ff16.attribute.crewStrength" />
	</span>

	<div>
		<span data-enemy-id="0"><spring:message code="page.ff16.label.fight.ship.crewName" /></span>
		<span data-enemy-skill>${data.crewStrike}</span>
		<span data-enemy-stamina>${data.crewStrength}</span>
	</div>
</div>
<div id="ffEnemyList">
	<c:forEach var="enemy" items="${fightCommand.resolvedEnemies}" varStatus="status">
		<br />
		<c:if test="${enemy.stamina > 0}">
			 <span data-enemy-id="${enemy.id}" data-enemy-name data-enemy-selected="${param.id == enemy.id || firstEnemy}" data-enemy-forced="false">${enemy.name}</span>
		</c:if>
		<c:if test="${enemy.stamina <= 0}">
			<span data-enemy-id="${enemy.id}" data-enemy-name>${enemy.name}</span>
		</c:if>
		<span data-enemy-id="${enemy.id}" data-enemy-skill>${enemy.skill}</span>
		<span data-enemy-id="${enemy.id}" data-enemy-stamina>${enemy.stamina}</span>

		<c:set var="firstEnemy" value="${firstEnemy && enemy.stamina < 1}" />
	</c:forEach>
</div>

<div id="ffAttackButton">
	<button data-attack="ff">
		<spring:message code="page.ff.label.fight.attack" />
	</button>
	<c:if test="${fightCommand.fleeAllowed}">
		<button data-flee="ff">
			<c:if test="${not empty fightCommand.fleeData.text}">
				${fightCommand.fleeData.text}
			</c:if>
			<c:if test="${empty fightCommand.fleeData.text}">
				<spring:message code="page.ff.label.fight.flee" />
			</c:if>
		</button>
	</c:if>
</div>
