<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="firstEnemy" value="true" />
<c:set var="indifferentFight" value="${fightCommand.resolvedAllies[0].ally.indifferentAlly}" />

<div id="ffEnemyList" class="${indifferentFight ? 'ffAllyIndifferent' : ''}">
    <span class="skillTitle">
        <spring:message code="page.ff.attribute.skill" />
    </span>
    <span class="staminaTitle">
        <spring:message code="page.ff.attribute.stamina" />
    </span>
    
    <c:forEach items="${fightCommand.resolvedAllies}" var="ally">
	    <div>
	        <span data-enemy-id="0">${ally.name}</span>
	        <span data-enemy-skill>${ally.skill}</span>
	        <span data-enemy-stamina>${ally.stamina}</span>
	    </div>
    </c:forEach>
    <c:forEach var="enemy" items="${fightCommand.resolvedEnemies}" varStatus="status">
        <c:if test="${!status.first || !indifferentFight}">
            <br />
        </c:if>
        <c:if test="${enemy.stamina > 0}">
           <span data-enemy-id="${enemy.id}" data-enemy-name data-enemy-selected="${firstEnemy}">${enemy.name}</span>
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
