<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<tiles:insertTemplate template="../basic/section-sectionNumber.jsp" />

<tiles:insertTemplate template="../basic/section-mainContent.jsp" />

<div class="hr"></div>

<tiles:insertTemplate template="section-choices.jsp" />

<tiles:insertAttribute name="gamebook.command" />

<c:if test="${paragraph.id == 'generate'}">
    <script type="text/javascript">allowNavigation();</script>
</c:if>
