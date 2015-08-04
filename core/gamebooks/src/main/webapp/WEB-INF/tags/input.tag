<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true" %>
<%@attribute name="label" required="true" %>
<%@attribute name="id" required="false" %>
<%@attribute name="type" required="false" %>

<label><spring:message code="${label}" /></label>
<form:input path="${name}" type="${type}" id="${id}" />
