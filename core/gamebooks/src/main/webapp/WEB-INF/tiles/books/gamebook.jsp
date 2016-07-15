<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:tiles="http://tiles.apache.org/tags-tiles">
	<div id="right-out">
		<div id="right-in">
			<div id="gamebookCharacterPageWrapper">
				<tiles:insertAttribute name="gamebook.charpagebar" />
			</div>
		</div>
	</div>
	<tiles:insertAttribute name="gamebook.rewards" />
	<div id="gamebookContent">
		<tiles:insertAttribute name="gamebook.content" />
	</div>
</jsp:root>
