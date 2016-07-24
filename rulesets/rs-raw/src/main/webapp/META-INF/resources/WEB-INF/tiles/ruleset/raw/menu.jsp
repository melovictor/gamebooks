<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="bookListUrl" value="/booklist" />
<c:url var="loadGameUrl" value="load" />
<c:if test="${not empty nextUpdateTime}">
	<div id="serverRestart">
		<spring:message code="page.warning.serverRestart" arguments="${nextUpdateTime}" />
	</div>
</c:if>
<spring:message code="${pageTitle}" text="${pageTitle}" />
<c:if test="${empty bookList}">
	<ul>
		<li>
			<a href="${bookListUrl}" id="closeBook">
				<spring:message code="page.menu.book.close" />
			</a>
		</li>
		<c:if test="${empty isWelcomeScreen and empty isErrorPage}">
			<li>
				<a href="<c:url value="new" />" id="restartBook">
					<spring:message code="page.menu.book.restart" />
				</a>
			</li>
			<c:if test="${haveSavedGame}">
				<li>
					<a href="${loadGameUrl}">
						<spring:message code="page.menu.book.load" />
					</a>
				</li>
			</c:if>
			<c:if test="${empty isNewScreen}">
				<li>
					<span class="link" id="saveGameLink">
						<spring:message code="page.menu.book.save" />
					</span>
				</li>
			</c:if>
		</c:if>
	</ul>
</c:if>
