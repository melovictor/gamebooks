<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<tiles:insertTemplate template="../basic/section-sectionNumber.jsp" />

<tiles:insertTemplate template="../basic/section-mainContent.jsp" />

<div class="hr"></div>

<tiles:insertTemplate template="../basic/section-choices.jsp" />

<tiles:insertAttribute name="gamebook.command" />
