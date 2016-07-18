<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="l" %>
<%@tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@attribute name="item" required="true" type="hu.zagor.gamebooks.mvc.bookselection.domain.SeriesData" %>

<div>
	<h2 data-series-id="${item.id}"><c:out value="${item.name}"/></h2>
	<c:forEach var="bookItem" items="${item.books}" varStatus="status">
	    <l:book item="${bookItem}"/>
	</c:forEach>
</div>
