<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="width">
    <tiles:insertAttribute defaultValue="3" name="width" />
</c:set>
<div class="ffMainAttribute${width}">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.skill" /></span>
    <span class="ffInitialMainAttribute"><spring:message code="page.ff.attribute.skill.initial" /> ${charEquipments.initialSkill}</span>
    <div class="ffMainAttributeValue" data-attribute-skill>${charEquipments.skill}</div>
</div>
<div class="ffMainAttribute${width}">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.stamina" /></span>
    <span class="ffInitialMainAttribute"><spring:message code="page.ff.attribute.stamina.initial" /> ${charEquipments.initialStamina}</span>
    <div class="ffMainAttributeValue" data-attribute-stamina>${charEquipments.stamina}</div>
</div>
<div class="ffMainAttribute${width}">
    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.luck" /></span>
    <span class="ffInitialMainAttribute"><spring:message code="page.ff.attribute.luck.initial" /> ${charEquipments.initialLuck}</span>
    <div class="ffMainAttributeValue" data-attribute-luck>${charEquipments.luck}</div>
</div>
