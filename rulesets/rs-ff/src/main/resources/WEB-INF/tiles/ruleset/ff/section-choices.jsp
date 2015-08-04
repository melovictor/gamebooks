<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not ffChoiceHidden && charEquipments.alive}">
    <div id="choiceWrapper" class="${ffChoiceClass}">
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
