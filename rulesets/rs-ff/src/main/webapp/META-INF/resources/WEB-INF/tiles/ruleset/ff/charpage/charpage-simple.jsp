<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${hasInventory || hasMap}">
    <h1>
        <spring:message code="page.menu.ff.adventureSheet" />
    </h1>
    <div id="inventory">
       <tiles:insertTemplate template="../charpage/ssl3.jsp" />
       <tiles:insertTemplate template="../charpage/eq.jsp" />
       <tiles:insertTemplate template="../charpage/shadow.jsp" />
       <tiles:insertTemplate template="../charpage/map.jsp" />
       <tiles:insertTemplate template="../charpage/notes.jsp" />
    </div>
</c:if>
