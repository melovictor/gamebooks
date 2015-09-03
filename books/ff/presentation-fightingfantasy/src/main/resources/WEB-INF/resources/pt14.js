var pt14 = (function() {
	function dropFenris() {
		var choices = $("#choice a");
		if (choices.length > 0) {
			var currentSection = $("#sectionNumber").data("id");
			var $form = $("<form method='post' action='dropFenris'>");
			$form.append($("<input type='hidden' name='source' value='" + currentSection + "' />"));
			$form.submit();
		}
	}
	
	return {
		dropFenris : dropFenris
	};
})();



$(function() {
	$("[data-item-id='3015']").on("click", pt14.dropFenris);
});

