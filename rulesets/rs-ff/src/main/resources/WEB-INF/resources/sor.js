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
	
	function resetAttribute() {
		$("[data-libra-reset]").attr("disabled", "disabled");
		$.ajax({
			url : "libraReset",
			type : "get",
			success : function(data) {
				inventory.loadInventory();
			}
		});

	}

	return {
		generateCharacter : generateCharacter,
		changeCaste : changeCaste,
		resetAttribute : resetAttribute
	};
})();


$(function() {
	$("[data-generator-button='sor']").on("click", sor.generateCharacter);
	$("[name='caste']").on("change", sor.changeCaste);
	$("[data-libra-reset]").on("click", sor.resetAttribute);
});

