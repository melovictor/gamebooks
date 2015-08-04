<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:url var="dice" value="/resources/img/dice/dice1.jpg" />
<p class="rawRandomLabel">
    ${random.label}
    <img src="${dice}" class="rawDice" />
</p>
<p>
    <button data-random="raw"><spring:message code="page.raw.label.random" /></button>
</p>