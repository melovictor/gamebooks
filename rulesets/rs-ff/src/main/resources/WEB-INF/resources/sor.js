var sor = (function() {
	function generateCharacter() {
		$("[name='caste']").prop("disabled", true);
		ff.sendGenerationRequest(null, {
			caste : $("[name='caste']:checked").val()
		});
	}
	function changeCaste(event) {
		var $skillAddition = $("#skillAddition");
		$skillAddition.text($("#skillValue" + $(event.target).val()).val());
	}
	
	function resetAttribute() {
		$("[data-libra-reset]").prop("disabled", true);
		$.ajax({
			url : "libraReset",
			type : "get",
			success : function(data) {
				inventory.loadInventory();
			}
		});
	}
	
	var doingRemoval = false;
	function startRemoveCurseSickness() {
		doingRemoval = true;
		$(".sorCursesFullWidth").addClass("removing");
		$("[data-libra-curseremoval]").prop("disabled", true);
	}
	function removeSpecificCurseSickness() {
		doingRemoval = false;
		$(".sorCursesFullWidth").removeClass("removing");
		var curseId = $(this).data("itemId");
		$.ajax({
			url : "libraRemoveCurse/" + curseId,
			type : "get",
			success : function(data) {
				inventory.loadInventory();
			}
		});
	}

	return {
		generateCharacter : generateCharacter,
		changeCaste : changeCaste,
		resetAttribute : resetAttribute,
		startRemoveCurseSickness : startRemoveCurseSickness,
		removeSpecificCurseSickness : removeSpecificCurseSickness
	};
})();


$(function() {
	$("[data-generator-button='sor']").on("click", sor.generateCharacter);
	$("[name='caste']").on("change", sor.changeCaste);
	$("#gamebookCharacterPageWrapper")
		.on("click", "[data-libra-reset]:not([disabled])", sor.resetAttribute)
		.on("click", "[data-libra-curseremoval]:not([disabled])", sor.startRemoveCurseSickness)
		.on("click", ".sorCursesFullWidth.removing [data-item-id]", sor.removeSpecificCurseSickness);
});
