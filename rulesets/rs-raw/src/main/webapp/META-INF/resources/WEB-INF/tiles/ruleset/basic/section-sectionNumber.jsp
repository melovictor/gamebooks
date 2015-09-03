<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:if test="${!hideTopSection}">
    <div id="sectionNumber" data-id="${paragraph.id}">${paragraph.displayId}</div>
</c:if>

<c:if test="${!user.admin}">
	<script type="text/javascript">
		preventBack("<spring:message code="page.book.warning.dontgoback" />");
	</script>
</c:if>
