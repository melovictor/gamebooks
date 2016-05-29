<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<h1>
	<spring:message code="page.menu.ff.adventureSheet" />
</h1>
<div id="inventory">
	<tiles:insertTemplate template="../charpage/ssl3.jsp" />
	<tiles:insertTemplate template="../charpage/gpp.jsp">
	<tiles:putAttribute name="width" value="4" type="string" />
	</tiles:insertTemplate>
	<div class="ffMainAttribute4">
		<span class="ffMainAttribute"><spring:message code="page.ff14.attribute.spells" /></span>
		<div data-items="true">
		    <c:forEach items="${charEquipments.spells}" var="spell" varStatus="status">
		        <c:if test="${!status.first }">, </c:if>
		        ${spell.name}
		    </c:forEach>
		</div>
	</div>
	<tiles:insertTemplate template="../charpage/eq.jsp" />
	<tiles:insertTemplate template="../charpage/map.jsp" />
	<tiles:insertTemplate template="../charpage/notes.jsp" />
</div>
