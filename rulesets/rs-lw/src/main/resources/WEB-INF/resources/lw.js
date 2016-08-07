var lwCharGen = (function() {
	var maxSelectableValues = {};
	var selectionVerificationStatus = {};
	
	function init() {
		var $genButton = $("[data-generator-button='lw']");
		if ($genButton.length > 0) {
			$genButton.on("click", sendGenerationRequest);
			$("[data-total]").each(function(idx, elem) {
				$e = $(elem);
				var className = $e.attr("class");
				var maxSelectable = parseInt($e.data("total"));
				maxSelectableValues[className] = maxSelectable;
				selectionVerificationStatus[className] = false;
				$("." + className + " input").on("change", calculateMaxSelectables);
			});
		}
	}
	
	function sendGenerationRequest() {
		calculateAllMaxSelectables();
		if ($("button[data-generator-button='lw']").prop("disabled")) {
			return;
		}
		var data = {
		};

		var $kaiDisciplines = $(".lwKaiDisciplines input");
		$kaiDisciplines.prop("disabled", true);
		$kaiDisciplines.each(function(idx, elem) {
			var $elem = $(elem);
			data[$elem.attr("id")] = $elem.is(":checked");
		});

		var $startingEquipment = $(".lwStartingEquipment input");
		$startingEquipment.prop("disabled", true);
		var itemArray = {};
		var count = 0;
		$startingEquipment.each(function(idx, elem) {
			var $elem = $(elem);
			if ($elem.is(":checked")) {
				data["equipments[" + count + "].id"] = $elem.data("id");
				data["equipments[" + count + "].amount"] = $elem.data("amount");
				count++;
			}
		});
		
		$.ajax({
			url : "new/generate",
			data : data,
			type : "POST",
			success : function(data) {
				$.each(data, function(key, value) {
					$("#" + key).html(value);
				});
				if (data.weaponskill) {
					$(".lwKaiDisciplines").append($("<p>").html(data.weaponskill));
				}
				$("#choiceWrapper").show();
				$("[data-generator-button], .lwDice").hide();
				inventory.loadInventory();
				$("[data-step='1']").trigger("stepFinished", data);
			}
		});
		$("[data-generator-button]").prop("disabled", true);
	}
	
	function calculateAllMaxSelectables() {
		$("[data-total]").each(function(idx, elem) {
			calculateMaxSelectables({ "target" : $(elem) });
		});
	}
	
	function calculateMaxSelectables(event) {
		var group = $(event.target).closest("p").attr("class");
		var maxSelectable = maxSelectableValues[group];
		
		var totalSelected = $("." + group + " input:checked").length;
		if (totalSelected > maxSelectable) {
			$("." + group + " input").prop("checked", false);
		} else if (totalSelected == maxSelectable) {
			$("." + group + " input:not(:checked)").prop("disabled", true);
			selectionVerificationStatus[group] = true;
			genButton();
		} else {
			$("." + group + " input:not(:checked)").prop("disabled", false);
			selectionVerificationStatus[group] = false;
			genButton();
		}
	}
	
	function genButton() {
		var enabled = true;
		for (var key in selectionVerificationStatus) {
			enabled &= selectionVerificationStatus[key];
		};
		$("button[data-generator-button='lw']").prop("disabled", !enabled);
	}


	return {
		init : init
	};
})();

var lw = (function() {
	function attack() {
		var enemyId = $("[data-enemy-selected='true']").data("enemy-id");
		form.submit("post", "attack?id=" + enemyId, "lwEnemyList");
	}
	function flee() {
		form.submit("post", "flee", "lwEnemyList");
	}
	function random() {
		form.submit("post", "random", "actionEnd");
	}
	function useBeforeFight() {
		var itemId = $(this).data("item-id");
		form.submit("post", "preFight?id=" + itemId, "ffEnemyList");
	}
	
	return {
		attack : attack,
		useBeforeFight : useBeforeFight,
		flee : flee,
		random : random
	};
})();

$(function() {
	function backspan($elem) {
		return $elem.parent();
	}
	
	lwCharGen.init();
	$("#lwMenu")
		.on("click", "#gamebookCharacterPageWrapper [data-item-equipped] span:not(.remove)", backspan, inventory.changeEquip)
		.on("click", "#gamebookCharacterPageWrapper span.remove", backspan, inventory.dropItem)
		.on("click", ".lwSlot[data-item-type='provision'] span:not(.remove), .lwSlot[data-item-type='potion'] span", backspan, inventory.consume);

	$("[data-attack='lw']").on("click", lw.attack);
	$("[data-flee='lw']").on("click", lw.flee);
	$("[data-random='lw']").on("click", lw.random);
	market.init("lwGamebookContent");
	$("[data-market]")
		.on("click", "#marketForSale [data-id]:not([data-stock='0'])", market.buy)
		.on("click", "#marketForPurchase [data-id]:not([data-stock='0'])", market.sell)
		.on("click", "[data-market-close]", market.close);
	$("#preFightItems")
		.on("click", "[data-item-id]", lw.useBeforeFight);
});