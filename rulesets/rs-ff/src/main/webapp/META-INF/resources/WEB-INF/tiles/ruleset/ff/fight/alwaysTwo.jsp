<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<c:set var="maxEnemiesToDisplay" value="2" scope="request" />
<tiles:insertTemplate template="single.jsp" />
