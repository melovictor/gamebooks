<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:set var="maxEnemiesToDisplay" value="${fightCommand.maxEnemiesToDisplay}" scope="request" />
<c:if test="${fightCommand.maxEnemiesToDisplay > 2}">
    <c:set var="maxEnemiesToDisplay" value="2" scope="request" />
</c:if>
<tiles:insertTemplate template="single.jsp" />
