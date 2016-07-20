<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty data.shadows && user.tester}">
<div class="lwShadowEquipment">
	<span class="lwMainAttribute"><spring:message code="page.lw.shadowItems" /></span>
	<c:set var="isFirst" value="true" />
	<c:forEach var="item" items="${data.shadows}"><c:if test="${!isFirst}">,</c:if>
		<c:set var="isFirst" value="false" />
		{<span <c:if test="${not empty item.description}"> title="${item.description}"</c:if>>${item.name}</span>}<!--
	--></c:forEach>
</div>
</c:if>
