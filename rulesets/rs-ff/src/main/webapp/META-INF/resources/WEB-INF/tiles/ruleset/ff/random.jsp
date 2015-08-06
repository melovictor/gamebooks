<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<p class="ffRandomLabel">
    ${random.label}
    <img src="resources/ff/dice1.gif" class="ffDice" />
</p>
<p>
    <button data-random="ff"><spring:message code="page.ff.label.random" /></button>
</p>