<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${hasMap}">
	<h2>
		<span data-map="mapDialog">
			<spring:message code="page.menu.book.inventory.map" />
		</span>
	</h2>
	<div id="mapDialog" class="popupMap" title="${bookInfo.title}">
		<img src="http://zagor.hu/gamebooks/img.php?book=${bookInfo.resourceDir}&img=${not empty customMapId ? customMapId : 'map'}&loc=${bookInfo.locale}" />
	</div>
</c:if>
