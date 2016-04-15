<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@attribute name="item" required="true" type="hu.zagor.gamebooks.mvc.bookselection.domain.BookData" %>

<div class="bookItem">
	<c:url value="book/${item.id}/welcome" var="bookUrl" />
	<c:url value="book/${item.id}/${item.coverPath}" var="coverUrl" />

	<c:if test="${!item.disabled || user.admin}">
		<a href="${bookUrl}" class="booksmallcover" data-bookId="${item.id}">
			<img class="bordered${item.disabled ? ' disabled' : ''}" src="${coverUrl}" alt="${item.title}" />
		</a>
	</c:if>
	<c:if test="${item.disabled && !user.admin}">
		<img class="bordered disabled" src="${coverUrl}" alt="${item.title}" />
	</c:if>
	<c:if test="${item.position != null}">
		${item.position}.
	</c:if>
	${item.title}
	<c:if test="${item.unfinished}">
		<em>
			<spring:message code="page.menu.book.unfinished" />
		</em>
	</c:if>
</div>
