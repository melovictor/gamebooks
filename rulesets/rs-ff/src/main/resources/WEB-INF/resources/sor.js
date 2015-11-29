var sor = (function() {
	function generateCharacter() {
		$("[name='caste']").attr("disabled", "disabled");
		ff.sendGenerationRequest(null, {
			caste : $("[name='caste']:checked").val()
		});
	}
	function changeCaste(event) {
		var $skillAddition = $("#skillAddition");
		$skillAddition.text($("#skillValue" + $(event.target).val()).val());
	}

	return {
		generateCharacter : generateCharacter,
		changeCaste : changeCaste
	};
})();


$(function() {
	$("[data-generator-button='sor']").on("click", sor.generateCharacter);
	$("[name='caste']").on("change", sor.changeCaste);
});

