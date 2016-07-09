var ff36 = (function() {
	var totalToLose;
	
	function init() {
		totalToLose = parseInt($("#totalLosses").val());
		if (totalToLose > 0) {
			$(".ff36FightersToLose:eq(0)").focus();
		}
	}
	
	function verifyEnteredAmounts(last) {
		verifyAllHasMultipleOfFive();
		verifyEnoughFightersSelected(last);
	}
	
	function verifyEnoughFightersSelected(last) {
		var selected = 0;
		$(".ff36FightersToLose").each(function(idx, elem) {
			selected += parseInt($(elem).val());
		});
		var enoughSelected = true;
		if (selected > totalToLose && last) {
			$(last.target).val($(last.target).val() - selected + totalToLose);
		} else if (selected < totalToLose) {
			enoughSelected = false;
		}
		$("#ffAttackButton button").prop("disabled", !enoughSelected);
	}
	
	function verifyAllHasMultipleOfFive() {
		$(".ff36FightersToLose").each(function(idx, elem) {
			var entered = parseInt($(elem).val());
			if (entered % 5 != 0) {
				$(elem).val(entered - entered % 5);
			}
		});
	}
	
	function sendSelection(button) {
		verifyEnoughFightersSelected();
		if (!$(button).prop("disabled")) {
			allowNavigation();
			$("#ff36FinishAttack").submit();
		}
	}
	
	return {
		verifyEnteredAmounts : verifyEnteredAmounts,
		sendSelection : sendSelection,
		init : init
	};
})();

$(function() {
	ff36.init();
	$(".ff36FightersToLose")
		.on("change", ff36.verifyEnteredAmounts)
		.on("blur", ff36.verifyEnteredAmounts);
	$("#ffAttackButton button").on("click", ff36.sendSelection);
})