<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<h1>
    <spring:message code="page.menu.ff.adventureSheet" />
</h1>
<div id="inventory">
    <tiles:insertTemplate template="../charpage/ssl4.jsp" />

	<div class="ffMainAttribute4">
	    <span class="ffMainAttribute"><spring:message code="page.pt14.attribute.wisdom" /></span>
	    <div class="ffMainAttributeValue" data-attribute-skill>${charEquipments.wisdom}</div>
	</div>

    <tiles:insertTemplate template="../charpage/gpp.jsp" />
    <tiles:insertTemplate template="../charpage/eq.jsp" />
    <tiles:insertTemplate template="../charpage/map.jsp" />
    <tiles:insertTemplate template="../charpage/notes.jsp" />
</div>
