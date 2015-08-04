<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:if test="${!bookSpecificRules}">
	<c:set var="selectedMenu" value="rules" scope="request" />
</c:if>
<tiles:insertTemplate template="../basic/basicMenu2.jsp" />
