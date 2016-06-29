<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="cover">
	<c:url value="http://zagor.hu/gamebooks/img.php?book=${bookInfo.resourceDir}&img=${bookInfo.cover}&loc=${bookInfo.locale}" var="coverUrl" />
	<img class="bordered" src="${coverUrl}" alt="${bookInfo.title}" />
</div>

<div id="backCoverText">${paragraph.data.text}</div>

<c:url var="newGameUrl" value="new" />

<div id="choice">
	<ul>
		<li><a href="${newGameUrl}"><spring:message code="page.book.welcome.new" /></a></li>
		<c:if test="${haveSavedGame}">
			<c:url var="loadGameUrl" value="load" />
			<li><a href="${loadGameUrl}"><spring:message code="page.book.welcome.load" /></a></li>
		</c:if>
		<c:if test="${canContinuePrevious}">
		    <c:url var="continueGameUrl" value="loadPrevious" />
		    <li><a href="${continueGameUrl}"><spring:message code="page.book.welcome.continue" /></a></li>
		</c:if>
		<c:if test="${haveRules}">
            <c:url var="rulesUrl" value="../../rules/${bookInfo.id}" />
			<li><a href="${rulesUrl}" target="_blank" data-no-multi-navigation><spring:message code="page.book.welcome.readRules" /></a></li>
		</c:if>
	</ul>
</div>
