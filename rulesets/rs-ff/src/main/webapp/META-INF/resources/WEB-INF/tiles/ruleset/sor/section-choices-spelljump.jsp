<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="choice">
	<ul>
		<c:forEach var="choice" items="${paragraph.data.choices}">
			<li>
				<a href="${choice.id}">
					<spring:message code="book.choice.next" />
				</a>
			</li>
		</c:forEach>
	</ul>
</div>
