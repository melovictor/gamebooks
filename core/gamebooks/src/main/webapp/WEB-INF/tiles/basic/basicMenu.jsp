<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="logoutUrl" value="/logout" />
<div>
	<spring:message code="page.menu.logged.in.as"
		arguments="${user.username},<a href='${logoutUrl}' id='logout'>,</a>" />
</div>

<div id="languageSelection">
	<div id="languageSelectionMain">
		<div id="languageSelectionSelected"></div>
		<div id="languageSelectionOptions">
			<ul>
				<c:forEach items="${availableLanguages}" var="language">
					<li class="<c:if test="${language.localeFormat eq selectedLanguage}">languageSelected</c:if>">
						<img src="<c:url value="/resources/img/flag/${language.flagCode}.png" />"
						alt="${language.localeCode}<c:if test="${not empty language.countryCode }">_${language.countryCode}</c:if>" />
						${language.selfName}
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>
