<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<p class="ffAttributeTestLabel">
	${attribTest.label}
	<img src="resources/ff/dice2.gif" class="ffDice" />
</p>
<p>
	<button data-attribute-test="ff" data-action="test"><spring:message code="page.ff.label.test" /></button>
	<c:if test="${attribTest.canSkip}">
		<button data-attribute-test="ff" data-action="skip">${attribTest.skipText}</button>
	</c:if>
</p>
