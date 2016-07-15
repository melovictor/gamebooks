<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags">
	<div id="gamebookRewards">
		<input type="hidden" id="bookId" value="${bookInfo.id}" /> <input type="hidden" id="userId"
			value="${user.id}" />
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
</jsp:root>