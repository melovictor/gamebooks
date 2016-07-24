<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${not choiceHidden && data.alive}">
	<div id="choiceWrapper" class="${ffChoiceClass}">
		<c:if test="${not empty paragraph.data.spellChoices}">
			<div id="sorSpellContainer">
				<c:forEach items="${paragraph.data.spellChoices}" var="spell" varStatus="stat">
					<div class="sorSpell">
						<c:set var="linkPrefix"><c:if test="${informativeSections}">${spell.id}~</c:if></c:set>
						<c:if test="${spell.id != '-1' && data.wizard}">
							<a href="spl-${linkPrefix}${stat.index}">
								${spell.text}
								<c:if test="${!hideChoiceSection}">
									(${spell.display})
								</c:if>
							</a>
						</c:if>
						<c:if test="${spell.id == '-1' || !data.wizard}">
							<c:if test="${data.vikKnown}">
								<span data-spell-name="${spell.text}">${spell.text}</span>
							</c:if>
							<c:if test="${!data.vikKnown}">
								${spell.text}
							</c:if>
						</c:if>
					</div>
				</c:forEach>
			</div>
		</c:if>
		<c:if test="${paragraph.data.spellJump}">
			<tiles:insertTemplate template="section-choices-spelljump.jsp" />
		</c:if>
		<c:if test="${!paragraph.data.spellJump}">
			<tiles:insertTemplate template="../basic/section-choices.jsp" />
		</c:if>
	</div>
</c:if>
<c:if test="${choiceHidden}">
	<div id="choiceWrapper" class="${ffChoiceClass}">
		<div id="choice">
			<ul></ul>
		</div>
	</div>
</c:if>
