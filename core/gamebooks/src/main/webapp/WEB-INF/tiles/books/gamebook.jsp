<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:tiles="http://tiles.apache.org/tags-tiles" xmlns:spring="http://www.springframework.org/tags">
	<div id="right-out">
		<div id="right-in">
			<div id="gamebookCharacterPageWrapper">
				<tiles:insertAttribute name="gamebook.charpagebar" />
			</div>
		</div>
	</div>
	<div id="gamebookRewards">
		<input type="hidden" id="bookId" value="${bookInfo.id}" />
		<input type="hidden" id="userId" value="${user.id}" />
		<div id="unearnedGamebookRewards">
			<div class="title">
				<spring:message code="page.book.reward.unearned" />
			</div>
		</div>
		<div id="earnedGamebookRewards">
			<div class="title">
				<spring:message code="page.book.reward.earned" />
			</div>
		</div>
	</div>
	<div id="gamebookContent">
		<tiles:insertAttribute name="gamebook.content" />
	</div>
</jsp:root>
