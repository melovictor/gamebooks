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
			$("[data-generator-button='ff2']").prop("disabled", false);
		}
	}
	function finalizeCharacter() {
		$("#electricLash,#assaultBlaster,#gravityBomb,#grenade,#armour").prop("disabled", true);
		var data = {
			lash : $("#electricLash:checked").length,
			blaster : $("#assaultBlaster:checked").length,
			bomb : $("#gravityBomb:checked").length,
			grenade : parseInt($("#grenade").val()),
			armour : parseInt($("#armour").val())
		};
		$.ajax({
			url : "new/generate2",
			data : data,
			type : "get",
			complete : function() {
				$("#choiceWrapper").show();
				$("[data-generator-button='ff2']").hide();
				inventory.loadInventory();
			}
		});
		$("[data-generator-button='ff2']").prop("disabled", true).show();
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
			genButton.prop("disabled", false);
		} else {
			genButton.prop("disabled", true);
		}

		
		if (currentlySelected > maxWeapons) {
			resetLastChange($(this), currentlySelected);
		}
	}
	function resetLastChange($t, currentCount) {
		if ($t.attr("type") == "checkbox") {
			$t.prop("checked", false);
		} else {
			var diff = currentCount - maxWeapons;
			var curVal = parseInt($t.val());
			$t.val(curVal - diff);
		}
		verifyEquipments();
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
