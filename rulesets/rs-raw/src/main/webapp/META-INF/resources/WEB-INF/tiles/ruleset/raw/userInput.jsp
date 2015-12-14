<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="userInputSection"<c:if test="${not empty userInputClass}"> class="${userInputClass}"</c:if>>
	<form action="userInputResponse" method="post" id="userInputForm" autocomplete="off">
		<label for="responseText">${command.label}</label>
		<c:if test="${command.type == 'number'}">
			<input type="number" id="responseText" name="responseText" autocomplete="off" min="${command.min}" max="${command.max}" />
		</c:if>
		<c:if test="${command.type == 'text'}">
			<input type="text" id="responseText" name="responseText" autocomplete="off" />
		</c:if>
		<input type="hidden" name="elapsedTime" id="elapsedTime" />
	</form>
</div>
