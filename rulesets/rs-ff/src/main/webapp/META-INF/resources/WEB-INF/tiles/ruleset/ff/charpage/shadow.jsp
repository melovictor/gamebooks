<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${not empty charEquipments.shadows && user.tester}">
<div class="ffShadowEquipment">
	<span class="ffMainAttribute"><spring:message code="page.ff.shadowItems" /></span>
	<c:set var="isFirst" value="true" />
	<c:forEach var="item" items="${charEquipments.shadows}"><c:if test="${!isFirst}">,</c:if>
		<c:set var="isFirst" value="false" />
		{<span <c:if test="${not empty item.description}"> title="${item.description}"</c:if>>${item.name}</span>}<!--
	--></c:forEach>
</div>
</c:if>
