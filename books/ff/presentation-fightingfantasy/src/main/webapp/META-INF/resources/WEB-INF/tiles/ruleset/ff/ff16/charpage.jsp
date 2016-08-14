<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<h1>
	 <spring:message code="page.menu.ff.adventureSheet" />
</h1>
<div id="inventory">
	<tiles:insertTemplate template="../charpage/ssl4.jsp" />
    <div class="ffMainAttribute4">
        <span class="ffMainAttribute"><spring:message code="page.ff16.attribute.time" /></span>
        <div class="ffMainAttributeValue" data-attribute-gold>
            <fmt:formatNumber value="${data.time}" groupingUsed="true" />
            <button id="ff16Resting"><spring:message code="page.ff16.attribute.time.resting" /></button>
        </div>
    </div>

    <div class="ffMainAttribute4">
    <span class="ffMainAttribute"><spring:message code="page.ff16.attribute.crewStrike" /></span>
    <span class="ffInitialMainAttribute"><spring:message code="page.ff16.attribute.crewStrike.initial" /> ${data.initialCrewStrike}</span>
    <div class="ffMainAttributeValue" data-attribute-skill>${data.crewStrike}</div>
    </div>
    <div class="ffMainAttribute4">
    <span class="ffMainAttribute"><spring:message code="page.ff16.attribute.crewStrength" /></span>
    <span class="ffInitialMainAttribute"><spring:message code="page.ff16.attribute.crewStrength.initial" /> ${data.initialCrewStrength}</span>
    <div class="ffMainAttributeValue" data-attribute-skill>${data.crewStrength}</div>
    </div>
	<div class="ffMainAttribute4">
	    <span class="ffMainAttribute"><spring:message code="page.ff.attribute.gold" /></span>
	    <div class="ffMainAttributeValue" data-attribute-gold><fmt:formatNumber value="${data.gold}" groupingUsed="true" /></div>
	</div>
    <div class="ffMainAttribute4">
        <span class="ffMainAttribute"><spring:message code="page.ff16.attribute.slave" /></span>
        <div class="ffMainAttributeValue" data-attribute-gold><fmt:formatNumber value="${data.slave}" groupingUsed="true" /></div>
    </div>

	<tiles:insertTemplate template="../charpage/eq.jsp" />
	<tiles:insertTemplate template="../charpage/shadow.jsp" />
	<tiles:insertTemplate template="../charpage/map.jsp" />
	<tiles:insertTemplate template="../charpage/notes.jsp" />
</div>
