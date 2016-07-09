<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:tiles="http://tiles.apache.org/tags-tiles" xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags">

	<div class="ff36Fighters" id="ffEnemyList">
		<div class="ff36GroupDesignation">
			<spring:message code="page.ff36.fight.army.label.ownArmy" />
		</div>
		<ul>
			<c:forEach var="ally" items="${fightCommand.resolvedAllies}">
				<c:if test="${ally.stamina > 0}">
					<li>
						<spring:message code="page.ff36.attribute.army.${ally.name}" arguments="${ally.stamina}" />
					</li>
				</c:if>
				<c:if test="${0 > ally.stamina}">
					<li>
						<spring:message code="page.ff36.attribute.army.${ally.name}" arguments="${-ally.stamina}" />
					</li>
				</c:if>
			</c:forEach>
		</ul>
	</div>

	<div class="ff36Fighters">
	   <div class="ff36GroupDesignation">
			<spring:message code="page.ff36.fight.army.label.enemyArmy" />
		</div>
	   <ul>
		   <c:forEach var="enemy" items="${fightCommand.resolvedEnemies}">
			   <li>${enemy.name }: ${enemy.stamina }</li>
		   </c:forEach>
	   </ul>
	</div>
	
<div id="ffAttackButton">
	<button data-attack="ff">
		<spring:message code="page.ff.label.fight.attack" />
	</button>
</div>


</jsp:root>
