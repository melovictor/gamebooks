<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>


<tiles:insertTemplate template="../basic/section-sectionNumber.jsp" />

<tiles:insertTemplate template="../basic/section-mainContent.jsp" />

<c:if test="${not empty paragraph.data.hint}">
    <p class="inlineImage hoverInfo">
        <img src="resources/${bookInfo.resourceDir}/logo.jpg?${imgTypeOrder}" />
        <span class="info">
           ${paragraph.data.hint}
        </span>
    </p>

</c:if>

<div class="hr"></div>

<tiles:insertTemplate template="../basic/section-choices.jsp" />

<tiles:insertAttribute name="gamebook.command" />
