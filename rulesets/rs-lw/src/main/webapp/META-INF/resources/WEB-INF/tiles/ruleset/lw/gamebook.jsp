<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<div id="lwMenu" data-hidden-character-page="#inventory">
	<div id="gamebookCharacterPageWrapper">
		<tiles:insertAttribute name="gamebook.charpagebar" />
	</div>
</div>
<div id="lwGamebookContent">
    <tiles:insertAttribute name="gamebook.content" />
</div>
