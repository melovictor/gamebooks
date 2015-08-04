<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="rules">

	<c:forEach var="serie" items="${series}">
		<c:if test="${serie.locale == locale}">
			<h1>
				<spring:message code="page.rules.seriesTitle" arguments="${serie.seriesTitle}" argumentSeparator="@" />
			</h1>
			<p>
				<c:forEach var="bookInfo" items="${serie.entries}" varStatus="status">
					<c:url var="url" value="/rules/${bookInfo.id}" />
					<a href="${url}">${bookInfo.title}</a>
					<c:if test="${!status.last}">
						<br />
					</c:if>
				</c:forEach>
			</p>
		</c:if>
	</c:forEach>
</div>
