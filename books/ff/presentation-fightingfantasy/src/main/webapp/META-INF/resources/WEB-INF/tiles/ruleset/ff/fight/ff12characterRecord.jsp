<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<span class="skillTitle">
    <spring:message code="page.ff.attribute.skill" />
</span>
<span class="staminaTitle">
    <spring:message code="page.ff.attribute.stamina" />
</span>
<span class="luckTitle">
    <spring:message code="page.ff12.attribute.armour" />
</span>

<div>
    <span data-enemy-id="0">${user.principal}</span>
    <span data-enemy-skill>${data.skill}</span>
    <span data-enemy-stamina>${data.stamina}</span>
    <span data-enemy-armour>${data.armour}</span>
</div>
