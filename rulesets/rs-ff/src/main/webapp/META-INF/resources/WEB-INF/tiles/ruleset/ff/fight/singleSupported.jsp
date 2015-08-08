<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="firstEnemy" value="${empty param.id}" />

<div>
	<span class="skillTitle">
	    <spring:message code="page.ff.attribute.skill" />
	</span>
    <span class="staminaTitle">
        <spring:message code="page.ff.attribute.stamina" />
    </span>
    <span class="luckTitle">
        <spring:message code="page.ff.attribute.luck" />
    </span>
	
	<div>
	    <span data-enemy-id="0">${user.username}</span>
	    <span data-enemy-skill>${charEquipments.skill}</span>
	    <span data-enemy-stamina>${charEquipments.stamina}</span>
	    <span data-enemy-luck>${charEquipments.luck}</span>
	</div>
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
    <c:set var="hasForced" value="false" />
	<c:forEach var="enemy" items="${fightCommand.resolvedEnemies}">
	    <br />
        <c:if test="${enemy.stamina > 0}">
            <span data-enemy-id="${enemy.id}" data-enemy-name data-enemy-selected="${hasForced && !enemy.forced ? 'disabled' : (param.id == enemy.id || firstEnemy ? 'true' : 'false')}" data-enemy-forced="${enemy.forced}">${enemy.name}</span>
            <c:if test="${enemy.forced}">
                <c:set var="hasForced" value="true" />
            </c:if>
        </c:if>
        <c:if test="${enemy.stamina <= 0}">
           <span data-enemy-id="${enemy.id}" data-enemy-name>${enemy.name}</span>
        </c:if>
	    <span data-enemy-id="${enemy.id}" data-enemy-skill>${enemy.skill}</span>
	    <span data-enemy-id="${enemy.id}" data-enemy-stamina>${enemy.stamina}</span>
	    
	    <c:set var="firstEnemy" value="${firstEnemy && enemy.stamina < 1}" />
	</c:forEach>
</div>

<div id="ffLuckSettings">
	<input type="checkbox" id="luckOnEnemyHit" ${param.hit ? "checked='checked'" : ""}/> <label for="luckOnEnemyHit"><spring:message code="page.ff.label.fight.luck.attack" /></label><br />
	<input type="checkbox" id="luckOnSelfHit" ${param.def ? "checked='checked'" : ""}/> <label for="luckOnSelfHit"><spring:message code="page.ff.label.fight.luck.defense" /></label>
	<c:if test="${fightCommand.afterBounding.luckAllowed}">
	   <br />
	   <input type="checkbox" id="luckOnOther" ${param.oth ? "checked='checked'" : ""}/> <label for="luckOnOther"><spring:message code="page.ff.label.fight.luck.other" /></label>
	</c:if>
</div>

<div id="ffAttackButton">
    <button data-attack="ff">
        <spring:message code="page.ff.label.fight.attack" />
    </button>
    <c:if test="${ffFleeAllowed}">
        <button data-flee="ff">
	        <spring:message code="page.ff.label.fight.flee" />
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
