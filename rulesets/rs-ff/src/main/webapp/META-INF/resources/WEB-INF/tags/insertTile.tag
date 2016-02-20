<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@attribute name="templateName" required="true" type="java.lang.String" %>
<%@attribute name="defaultTemplateName" required="true" type="java.lang.String" %>

<c:if test="${empty templateName}">
	<tiles:insertTemplate template="${defaultTemplateName}" />
</c:if>
<c:if test="${not empty templateName}">
	<tiles:insertTemplate template="${templateName}" />
</c:if>
