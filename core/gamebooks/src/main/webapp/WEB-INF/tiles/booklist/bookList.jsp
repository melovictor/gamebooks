<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="l"%>

<div class="sortToggle"></div>
<div id="booklist-container">
	<c:forEach var="seriesItem" items="${bookSeriesData}">
		<l:series item="${seriesItem}" />
	</c:forEach>
	<br class="clear" />
</div>
