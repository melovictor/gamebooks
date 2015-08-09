var ff2 = (function() {
	var maxSpells;
	var selectedSpells = 0;
	var autoTriggerTaking = false;
	
	function initCharacterGenerationStep2(event, data) {
		$("[data-generator-button='ff']").hide();
		$("[data-step='2']").show();
		$("#choiceWrapper").hide();
		maxSpells = data.ffSpellNumeric;
	}
	function finalizeCharacter() {
		var data = {
			spells : $("#spellCodes").val().trim()
		};
		$.ajax({
			url : "new/generate2",
			data : data,
			type : "get",
			accept : "application/json; charset=utf-8",
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				$("[data-spell] span").unbind("click");
				$("#choiceWrapper").show();
				$("[data-generator-button='ff2']").hide();
				$("#availableSpells").hide();
				inventory.loadInventory();
			}
		});
	}
	function selectSpell() {
		if (maxSpells > selectedSpells) {
			$t = $(this);
			$container = $("#selectedSpells");
			var containerText = $container.html();
			if (selectedSpells > 0) {
				containerText += ", ";
			}
			containerText += $t.text();
			selectedSpells++;
			$container.html(containerText);
			var spellCode = $t.parent().data("spell");
			var $spellCodeInput = $("#spellCodes");
			$spellCodeInput.val($spellCodeInput.val() + spellCode + " ");

			if (autoTriggerTaking) {
				$.ajax({
					url : "acquireNewSpell/" + spellCode,
					type : "get",
					accept : "application/json; charset=utf-8",
					contentType : "application/json; charset=utf-8",
					dataType : "json",
					success : function(data) {
						if (maxSpells <= selectedSpells) {
							$("[data-spell] span").unbind("click");
							$("#availableSpells,#selectedSpells").hide();
						}
						inventory.loadInventory();
					}
				});
			}
		}
	}
	
	function initPickNewSpells() {
		$("[data-pick-spells]").unbind("click");
		maxSpells = parseInt($(this).data("pickSpells"));
		selectedSpells = 0;
		$("#availableSpells,#selectedSpells").show();
		autoTriggerTaking = true;
	}
	
	return {
		initCharacterGenerationStep2 : initCharacterGenerationStep2,
		initPickNewSpells : initPickNewSpells,
		finalizeCharacter : finalizeCharacter,
		selectSpell : selectSpell
	};
})();

$(function() {
	$("[data-step='1']").on("stepFinished", ff2.initCharacterGenerationStep2);
	$("[data-generator-button='ff2']").on("click", ff2.finalizeCharacter);
	$("[data-spell] span").on("click", ff2.selectSpell);
	$("[data-pick-spells]").on("click", ff2.initPickNewSpells);
});
