<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not lwChoiceHidden && data.alive}">
    <div id="choiceWrapper" class="${lwChoiceClass}">
        <tiles:insertTemplate template="../basic/section-choices.jsp" />
    </div>
</c:if>
<c:if test="${lwChoiceHidden}">
    <div id="choiceWrapper" class="${lwChoiceClass}">
        <div id="choice">
            <ul></ul>
        </div>
    </div>
</c:if>
