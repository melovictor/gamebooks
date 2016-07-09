<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:tiles="http://tiles.apache.org/tags-tiles" xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags">

	<form method="post" action="finishAttack" id="ff36FinishAttack">
		<div class="ff36Fighters" id="ffEnemyList">
			<div class="ff36GroupDesignation">
				<spring:message code="page.ff36.fight.army.label.lostMen" />
			</div>
			<ul>
				<c:forEach var="ally" items="${fightCommand.resolvedAllies}">
					<c:if test="${ally.stamina > 0}">
						<li>
							<input name="${ally.name}" id="${ally.name}" type="number" min="0" max="${ally.stamina}" value="0" step="5" class="ff36FightersToLose" />
							<label for="${ally.name}"><spring:message code="page.ff36.attribute.army.${ally.name}" arguments="${ally.stamina}" /></label>
						</li>
					</c:if>
					<c:if test="${0 > ally.stamina}">
						<input id="totalLosses" value="${-ally.stamina}" type="hidden" />
						<spring:message code="page.ff36.fight.army.label.totalLost" arguments="${-ally.stamina}" />
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</form>

	<div id="ffAttackButton">
		<button data-lose="ff36" disabled="disabled">
			<spring:message code="page.ff36.label.fight.loseFighters" />
		</button>
	</div>

</jsp:root>
