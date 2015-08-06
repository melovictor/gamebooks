<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<div id="ffMenu" data-hidden-character-page="#inventory">
	<div id="gamebookCharacterPageWrapper">
		<tiles:insertAttribute name="gamebook.charpagebar" />
	</div>
</div>
<div id="ffGamebookContent">
    <tiles:insertAttribute name="gamebook.content" />
</div>
