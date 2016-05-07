<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:if test="${hasInventory || hasMap}">
	<h1>
		<spring:message code="page.menu.ff.adventureSheet" />
	</h1>
	<div id="inventory">
		<tiles:insertTemplate template="ssl.jsp" />
		<tiles:insertTemplate template="../charpage/gpp.jsp">
			<tiles:putAttribute name="width" value="4" type="string" />
		</tiles:insertTemplate>

		<div class="ffMainAttribute4">
			<span class="ffMainAttribute"><spring:message code="page.ff60.attribute.time" /></span>
			<div class="ffMainAttributeValue" data-attribute-time>
				<fmt:formatNumber value="${charEquipments.time}" groupingUsed="true" />
			</div>
		</div>

		<tiles:insertTemplate template="../charpage/eq.jsp" />
		<tiles:insertTemplate template="../charpage/shadow.jsp" />
		<tiles:insertTemplate template="../charpage/map.jsp" />
		<tiles:insertTemplate template="../charpage/notes.jsp" />
	</div>
</c:if>
