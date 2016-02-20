<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="l" %>
<c:set var="firstEnemy" value="${empty param.id}" />

<div>
    <l:insertTile templateName="${characterRecord}" defaultTemplateName="characterRecord.jsp"/>
	<c:forEach var="ally" items="${fightCommand.resolvedAllies}">
		<div>
			<span data-enemy-id="0">${ally.name}</span>
			<span data-enemy-skill>${ally.skill}</span>
			<c:if test="${showAllyStamina}">
			   <span data-enemy-stamina>${ally.stamina}</span>
			</c:if>
			<span data-enemy-stamina>&nbsp;</span>
		</div>
	</c:forEach>

</div>
<div id="ffEnemyList">
	<c:set var="forcedOrder" value="${fightCommand.forceOrder}" />
	<c:forEach var="enemy" items="${fightCommand.resolvedEnemies}" varStatus="status">
		<c:if test="${empty maxEnemiesToDisplay || status.index < maxEnemiesToDisplay}">
			<br />
			<c:if test="${enemy.stamina > 0}">
				 <span data-enemy-id="${enemy.id}" data-enemy-name data-enemy-selected="${forcedOrder ? (status.first ? 'true' : 'disabled') : (param.id == enemy.id || firstEnemy)}" data-enemy-forced="${forcedOrder && status.first}">${enemy.name}</span>
			</c:if>
			<c:if test="${enemy.stamina <= 0}">
				<span data-enemy-id="${enemy.id}" data-enemy-name>${enemy.name}</span>
			</c:if>
			<span data-enemy-id="${enemy.id}" data-enemy-skill>${enemy.skill}</span>
			<span data-enemy-id="${enemy.id}" data-enemy-stamina>${enemy.stamina}</span>
		
			<c:set var="firstEnemy" value="${firstEnemy && enemy.stamina < 1}" />
		</c:if>
	</c:forEach>
</div>

<c:if test="${fightCommand.luckTestAllowed}">
	<div id="ffLuckSettings">
		<input type="checkbox" id="luckOnEnemyHit" ${param.hit ? "checked='checked'" : ""}/> <label for="luckOnEnemyHit"><spring:message code="page.ff.label.fight.luck.attack" /></label><br />
		<input type="checkbox" id="luckOnSelfHit" ${param.def ? "checked='checked'" : ""}/> <label for="luckOnSelfHit"><spring:message code="page.ff.label.fight.luck.defense" /></label>
		<c:if test="${fightCommand.afterBounding.luckAllowed}">
			<br />
			<input type="checkbox" id="luckOnOther" ${param.oth ? "checked='checked'" : ""}/> <label for="luckOnOther"><spring:message code="page.ff.label.fight.luck.other" /></label>
		</c:if>
	</div>
</c:if>

<c:if test="${not empty charEquipments.preFightItems && fightCommand.roundNumber == 0 && fightCommand.preFightAvailable}">
    <div id="preFightItems">
        <spring:message code="page.ff.label.fight.preFightItems" />
        <c:forEach items="${charEquipments.preFightItems}" var="item" varStatus="loop">
            <c:if test="${!item.usedInPreFight}">
                <span data-item-id="${item.id}">${item.name}</span><c:if test="${!loop.last}">, </c:if>
            </c:if>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty charEquipments.atFightItems && fightCommand.roundNumber > 0}">
    <div id="preFightItems">
        <spring:message code="page.ff.label.fight.atFightItems" />
        <c:forEach items="${charEquipments.atFightItems}" var="item" varStatus="loop">
            <c:if test="${!item.usedInPreFight}">
                <span data-item-id="${item.id}">${item.name}</span><c:if test="${!loop.last}">, </c:if>
            </c:if>
        </c:forEach>
    </div>
</c:if>

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

<c:if test="${!environmentDetector.seleniumTesting}">
	<script type="text/javascript">
		$(function() {
			<c:if test="${fightCommand.keepOpen}">
				inventory.stickInventory("#inventory");
			</c:if>
			<c:if test="${!fightCommand.keepOpen}">
				inventory.showInventory(null, "#inventory", true);
			</c:if>
		});
	</script>
</c:if>
