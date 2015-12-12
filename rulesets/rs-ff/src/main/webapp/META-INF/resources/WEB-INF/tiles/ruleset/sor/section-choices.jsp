<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${not ffChoiceHidden && charEquipments.alive}">
    <div id="choiceWrapper" class="${ffChoiceClass}">
        <c:if test="${not empty paragraph.data.spellChoices}">
            <div id="sorSpellContainer">
                <c:forEach items="${paragraph.data.spellChoices}" var="spell">
	                <div class="sorSpell">
		                <c:set var="linkPrefix"><c:if test="${informativeSections}">s-${spell.id}|</c:if></c:set>
		                <a href="${linkPrefix}${choice.position}">
			                ${spell.text}
			                <c:if test="${!hideChoiceSection}">
			                    (${spell.id})
			                </c:if>
		                </a>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        <tiles:insertTemplate template="../basic/section-choices.jsp" />
    </div>
</c:if>
<c:if test="${ffChoiceHidden}">
    <div id="choiceWrapper" class="${ffChoiceClass}">
        <div id="choice">
            <ul></ul>
        </div>
    </div>
</c:if>