<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@taglib tagdir="/WEB-INF/tags" prefix="l" %>

<form method="post" action="<c:url value="/settings" />">
	<c:if test="${not empty memoryUsageList}">
		<div id="memoryUsage">
			<table>
				<tr>
					<th></th>
					<th>Current</th>
					<th>Max</th>
				</tr>
				<c:forEach var="memUsage" items="${memoryUsageList}">
					<tr>
						<td>
							${memUsage.name} (${memUsage.type})
						</td>
						<td>
							<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" type="number" value="${memUsage.usage.used / 1024 / 1024}" /> M
						</td>
						<td>
							<fmt:formatNumber maxFractionDigits="0" type="number" value="${memUsage.usage.max / 1024 / 1024}" /> M
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div id="authTokens">
		  <c:forEach var="authTokenEntry" items="${authorizationCodeContainer}">
		      ${authTokenEntry}<br />
		  </c:forEach>
		</div>
	</c:if>
	<c:if test="${player.admin && environment.development}">
		<div id="settings-recording">
			<a href="<c:url value="/settings/recording/1" />">Start recording</a> <c:if test="${environment.recordState}">*</c:if><br />
			<a href="<c:url value="/settings/recording/0" />">Stop recording</a>	<c:if test="${!environment.recordState}">*</c:if><br />
			<div>
				D6: setUpRandomRolls(Arrays.asList(<c:forEach items="${nums6}" var="next" varStatus="status">
					${next}<c:if test="${!status.last}">,</c:if>
				</c:forEach>));
				<br />
				D10: setUpRandomRolls(Arrays.asList(<c:forEach items="${nums10}" var="next" varStatus="status">
					${next}<c:if test="${!status.last}">,</c:if>
				</c:forEach>));
			</div>
		</div>
	</c:if>
	<div id="settings-container">
		<c:forEach items="${settings}" var="settingGroup">
			<c:if test="${!settingGroup.adminOnly || player.admin}">
				<h3><spring:message code="${settingGroup.groupNameKey}" /></h3>
				<c:forEach items="${settingGroup.settingEntries}" var="settingItem">
					<c:choose>
						<c:when test="${settingItem.settingType == 'checkbox'}">
							<div class="settings-checkbox">
								<input type="checkbox" name="${settingItem.settingKey}" data-settings-checkbox id="${settingItem.settingKey}" value="true" ${userSettings[settingItem.settingKey] == 'true' ? "checked='checked'": ""} />
								<label for="${settingItem.settingKey}">
									<spring:message code="${settingItem.settingNameKey}" />
								</label>
							</div>
						</c:when>
						<c:when test="${settingItem.settingType == 'radio'}">
							<div class="settings-radio">
								<spring:message code="${settingItem.settingNameKey}" />
								<div class="settings-radio-options">
									<c:forEach items="${settingItem.subEntries}" var="subEntry">
										<div class="settings-radio-option">
											<input type="radio" name="${settingItem.settingKey}"
												id="${settingItem.settingKey}.${subEntry.optionKey}"
												value="${subEntry.optionKey}"
												${userSettings[settingItem.settingKey] == subEntry.optionKey ? "checked='checked'" : ""} />
											<label for="${settingItem.settingKey}.${subEntry.optionKey}">
												<spring:message code="${subEntry.titleKey}" />
											</label>
										</div>
									</c:forEach>
								</div>
							</div>
						</c:when>
						<c:when test="${settingItem.settingType == 'checkboxlist'}">
							<div class="settings-checkboxlist">
								<spring:message code="${settingItem.settingNameKey}" />
								<div class="settings-checkboxlist-options">
									<c:forEach items="${settingItem.subEntries}" var="subEntry">
										<div class="settings-checkboxlist-option">
											<input type="checkbox" name="${subEntry.optionKey}" data-settings-checkbox
												id="${subEntry.optionKey}"
												value="true"
												${userSettings[subEntry.optionKey] == "true" ? "checked='checked'" : ""} />
											<label for="${subEntry.optionKey}">
												<spring:message code="${subEntry.titleKey}" />
											</label>
										</div>
									</c:forEach>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							I don't know how to render this settings element: ${settingItem.settingNameKey}, ${settingItem.settingType }.
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:if>
		</c:forEach>
		<div class="settings-buttons">
			<input type="submit" value="<spring:message code='page.settings.button.save' text='Save settings' />" />
		</div>
	</div>
	<input type="hidden" name="global.defaultLanguage" value="${userSettings['global.defaultLanguage']}" />
</form>
