<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="firstEnemy" value="${empty param.id}" />

<div>
	<span class="skillTitle">
	    <spring:message code="page.ff15.attribute.weaponStrength" />
	</span>
    <span class="staminaTitle">
        <spring:message code="page.ff15.attribute.shield" />
    </span>
	
	<div>
	    <span data-enemy-id="0">${user.principal}</span>
	    <span data-enemy-skill>${data.ship.weaponStrength}</span>
	    <span data-enemy-stamina>${data.ship.shield}</span>
	</div>

</div>
<div id="ffEnemyList">
    <c:set var="forcedOrder" value="${fightCommand.forceOrder}" />
    <c:forEach var="enemy" items="${fightCommand.resolvedEnemies}" varStatus="status">
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
	</c:forEach>
</div>

<div id="ffAttackButton">
    <button data-attack="ff">
        <spring:message code="page.ff.label.fight.attack" />
    </button>
    <c:if test="${data.ship.smartMissile > 0}">
        <button data-attack="ff-m">
            <spring:message code="page.ff.label.fight.attack.withMissile" />
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
