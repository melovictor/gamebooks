<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${not ffChoiceHidden && charEquipments.alive}">
    <div id="choiceWrapper" class="${ffChoiceClass}">
        <c:if test="${not empty paragraph.data.spellChoices}">
            <div id="sorSpellContainer">
                <c:forEach items="${paragraph.data.spellChoices}" var="spell" varStatus="stat">
	                <div class="sorSpell">
		                <c:set var="linkPrefix"><c:if test="${informativeSections}">${spell.id}|</c:if></c:set>
		                <a href="spl-${linkPrefix}${stat.index}">
			                ${spell.text}
			                <c:if test="${!hideChoiceSection}">
			                    (${spell.display})
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
