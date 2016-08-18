<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="l" %>

<div id="accessLogs">

	<h1><spring:message code="page.logs.baseLog" /></h1>
	<c:forEach var="log" items="${logFiles.base}">
		<c:url var="logUrl" value="/logs/log-base${log.timestamp}.log/log/view" />
		<a href="${logUrl}">${log.loginDateTime}</a> &ndash; <fmt:formatNumber value="${log.size / 1024}" maxFractionDigits="1" /> kB<br />
	</c:forEach>

	<c:forEach items="${logFiles.byDate}" var="byDate">
		<h1>${byDate.date}</h1>
		<c:forEach items="${byDate.byUser}" var="byUser">
			<h2>${byUser.userId}</h2>
			<c:forEach items="${byUser}" var="log">
				<c:url var="logUrl" value="logs/log-${log.userId}-${log.timestamp}.log/log" />
				<c:if test="${not empty log.bookId}">
					<c:url var="logUrl" value="logs/log-${log.userId}-${log.timestamp}-${log.bookId}.log/log" />
				</c:if>

				<c:if test="${not empty log.bookId}">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ndash;
				</c:if>
				<a href="${logUrl}/view">
					<c:if test="${empty log.bookId}">
						${log.loginDateTime}
					</c:if>
					<c:if test="${not empty log.bookId}">
						${log.bookName}
					</c:if>
				</a> &ndash; <fmt:formatNumber value="${log.size / 1024}" maxFractionDigits="1" /> kB<br />
			</c:forEach>
		</c:forEach>
	</c:forEach>

</div>
<div id="saveGameFiles">
    <c:forEach var="userSavedGames" items="${savedGameFiles}">
        <h2>${userSavedGames.userName}</h2>
        <c:forEach var="savedGame" items="${userSavedGames.savedFiles}" varStatus="stat">
            <c:if test="${stat.index > 0}">
                <br />
            </c:if>
            <a href="savegame/${userSavedGames.userId}/${savedGame.bookId}/view">${savedGame.bookTitle}</a>
        </c:forEach>
    </c:forEach>
</div>


<h1><spring:message code="page.logs.archivedLogs" /></h1>
<div id="logArchives">
    <c:forEach items="${archivedLogFiles}" var="log">
        <c:url var="logUrl" value="/logs/archive.${log}.zip/zip" />
        <a href="${logUrl}">${log}</a>
    </c:forEach>
</div>
