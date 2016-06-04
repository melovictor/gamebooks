var ff8 = (function() {
	function displayMap() {
		$("#mapDialog img").attr("src", "map");

		$("#mapDialog").dialog({
			width : $(window).height() / 2384 * 1393,
			height : $(window).height(),
			position : {
				my : "center top",
				at : "center top",
				of : window
			}
		});
	}
	
	return {
		displayMap : displayMap
	}
})();

$(function() {
	$("#gamebookCharacterPageWrapper")
		.on("click", "span[data-map-ff8]", ff8.displayMap)
});
