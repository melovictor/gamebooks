<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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
</div>
<div id="gamebookContent">
    <tiles:insertAttribute name="gamebook.content" />
</div>
