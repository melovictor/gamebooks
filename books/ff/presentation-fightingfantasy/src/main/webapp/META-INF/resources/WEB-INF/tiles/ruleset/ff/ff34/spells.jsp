<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="ffMainAttribute4">
	<span class="ffMainAttribute"><spring:message code="page.ff34.attribute.spells" /></span>
	<div data-items="true">
		<c:set var="isFirst" value="true" />
		<c:forEach var="item" items="${data.spells}"><c:if test="${!isFirst}">,</c:if>
			<c:set var="isFirst" value="false" />
			<c:if test="${item.itemType == 'provision'}"><span data-item-id="${item.id}" data-item-provision>${item.name}</span></c:if><!--
			--><c:if test="${item.itemType == 'common'}"><span data-item-id="${item.id}">${item.name}</span></c:if></c:forEach>
	</div>
</div>