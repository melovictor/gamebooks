<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<p class="lwRandomLabel">
    ${random.label}
    <img src="resources/lw/dice10.jpg" class="lwDice" />
</p>
<p>
    <button data-random="lw"><spring:message code="page.lw.label.random" /></button>
</p>