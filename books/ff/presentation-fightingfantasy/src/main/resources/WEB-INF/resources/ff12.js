var ff12 = (function() {
	var maxWeapons;
	
	function initCharacterGenerationStep2(event, data) {
		$("[data-generator-button='ff']").hide();
		$("[data-step='2']").show();
		$("#choiceWrapper").hide();
		maxWeapons = data.ffWeaponsNumeric;
		$("[data-generator-button='ff2']").show();
		if (maxWeapons < 3) {
			$("[data-price=3]").prop("disabled", true);
			$("#electricLash").prop("checked", true);
			uncheckableLash();
			$("[data-generator-button='ff2']").removeAttr("disabled");
		}
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
			complete : function(data) {
				$("[data-spell] span").unbind("click");
				$("#choiceWrapper").show();
				$("[data-generator-button='ff2']").hide();
				$("#availableSpells").hide();
				inventory.loadInventory();
			}
		});
		$("[data-generator-button='ff2']").attr("disabled", "disabled").show();
	}
	function uncheckableLash() {
		$("#electricLash").on("change", function() {
			$("#electricLash").prop("checked", true);
			verifyEquipments();
		});
	}
	function verifyEquipments() {
		var lash = $("#electricLash:checked").length;
		var blaster = $("#assaultBlaster:checked").length * 3;
		var bomb = $("#gravityBomb:checked").length * 3;
		var grenades = parseInt($("#grenade").val());
		var armour = parseInt($("#armour").val());
		var currentlySelected = lash + blaster + bomb + grenades + armour;

		var selectedRequiredItems = $("[data-required]:checked");
		var genButton = $("[data-generator-button='ff2']");
		if (selectedRequiredItems.length > 0) {
			genButton.removeAttr("disabled");
		} else {
			genButton.attr("disabled", "disabled");
		}

		
		if (currentlySelected > maxWeapons) {
			resetLastChange($(this), currentlySelected);
		}
	}
	function resetLastChange($t, currentCount) {
		if ($t.attr("type") == "checkbox") {
			$t.removeAttr("checked");
		} else {
			var diff = currentCount - maxWeapons;
			var curVal = parseInt($t.val());
			$t.val(curVal - diff);
		}
	}

	
	return {
		initCharacterGenerationStep2 : initCharacterGenerationStep2,
		finalizeCharacter : finalizeCharacter,
		verifyEquipments : verifyEquipments
	};
})();

$(function() {
	$("[data-step='1']").on("stepFinished", ff12.initCharacterGenerationStep2);
	$("[data-generator-button='ff2']").on("click", ff12.finalizeCharacter);
	$("#ff12Weapons input").on("change", ff12.verifyEquipments);
});
