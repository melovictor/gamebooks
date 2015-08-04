<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="l"%>

<c:url var="loginPage" value="/login" />
<form:form commandName="loginData" method="post" action="${loginPage}" id="regform" name="regform">
	<c:if test="${not empty loginError}">
        <l:smallbox className="errorMessage">
            <spring:message code="${loginError}" />
        </l:smallbox>
    </c:if>

	<l:smallbox>
		<l:input label="page.login.username" name="username" id="adventurerName" />
		<l:input label="page.login.password" name="password" id="adventurerPassphrase" type="password" />
		<input type="submit" id="loginSubmitButton"
			value="<spring:message code="page.login.submit" />" />
	</l:smallbox>
</form:form>