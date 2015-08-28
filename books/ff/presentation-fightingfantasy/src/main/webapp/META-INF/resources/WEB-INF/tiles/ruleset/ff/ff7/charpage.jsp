<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="hasMap" value="${charEquipments.encounteredMap}" scope="request" />
<c:set var="customMapId" value="149" scope="request" />
<tiles:insertTemplate template="../charpage/charpage-basic.jsp" />
