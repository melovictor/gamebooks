<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div id="errorMessage">
    <p>
        <spring:message code="${problemCode}" />
    </p>
</div>
