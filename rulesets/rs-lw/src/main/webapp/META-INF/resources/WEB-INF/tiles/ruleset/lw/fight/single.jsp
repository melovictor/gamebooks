<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div>
	<span class="combatSkillTitle">
		<spring:message code="page.lw.attribute.combatSkill" />
	</span>
	<span class="enduranceTitle">
		<spring:message code="page.lw.attribute.endurance" />
	</span>

	<div>
		<span data-enemy-id="0">${user.principal}</span>
		<span data-enemy-combatSkill>${data.combatSkill}</span>
		<span data-enemy-endurance>${data.endurance}</span>
	</div>
</div>

<div id="lwEnemyList">
	<c:forEach var="enemy" items="${fightCommand.resolvedEnemies}" varStatus="status">
		<br />
		<c:if test="${enemy.endurance > 0}">
			 <span data-enemy-id="${enemy.id}" data-enemy-name data-enemy-selected="${status.first ? 'true' : 'disabled'}">${enemy.name}</span>
		</c:if>
		<c:if test="${enemy.endurance <= 0}">
			<span data-enemy-id="${enemy.id}" data-enemy-name>${enemy.name}</span>
		</c:if>
		<span data-enemy-id="${enemy.id}" data-enemy-combatSkill>${enemy.combatSkill}</span>
		<span data-enemy-id="${enemy.id}" data-enemy-endurance>${enemy.endurance}</span>
	</c:forEach>
</div>

<c:if test="${not empty data.preFightItems && fightCommand.roundNumber == 0}">
	<div id="preFightItems">
		<spring:message code="page.ff.label.fight.preFightItems" />
		<c:forEach items="${data.preFightItems}" var="item" varStatus="loop">
			<span data-item-id="${item.id}">${item.name}</span><c:if test="${!loop.last}">, </c:if>
		</c:forEach>
	</div>
</c:if>


<div id="lwAttackButton">
	<button data-attack="lw">
		<spring:message code="page.lw.label.fight.attack" />
	</button>
	<c:if test="${fightCommand.fleeAllowed}">
		<button data-flee="lw">
			<c:if test="${not empty fightCommand.fleeData.text}">
				${fightCommand.fleeData.text}
			</c:if>
			<c:if test="${empty fightCommand.fleeData.text}">
				<spring:message code="page.lw.label.fight.flee" />
			</c:if>
		</button>
	</c:if>
</div>
