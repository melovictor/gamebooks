var lwCharGen = (function() {
	var maxKaiDisciplines;
	
	function init() {
		var $genButton = $("[data-generator-button='lw']");
		if ($genButton.length > 0) {
			$genButton.on("click", sendGenerationRequest);
			maxKaiDisciplines = parseInt($(".lwKaiDisciplines").data("total"));
			$(".lwKaiDisciplines input").on("change", calculateDisciplines);
		}
	}
	
	function sendGenerationRequest() {
		calculateDisciplines();
		if ($("button[data-generator-button='lw']").prop("disabled")) {
			return;
		}
		var $kaiDisciplines = $(".lwKaiDisciplines input");
		$kaiDisciplines.prop("disabled", true);
		var data = {
		};
		$kaiDisciplines.each(function(idx, elem) {
			var $elem = $(elem);
			data[$elem.attr("id")] = $elem.is(":checked");
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
	
	function calculateDisciplines() {
		var totalSelected = $(".lwKaiDisciplines input:checked").length;
		if (totalSelected > maxKaiDisciplines) {
			$(".lwKaiDisciplines input").prop("checked", false);
		} else if (totalSelected == maxKaiDisciplines) {
			$(".lwKaiDisciplines input:not(:checked)").prop("disabled", true);
			genButton(true);
		} else {
			$(".lwKaiDisciplines input:not(:checked)").prop("disabled", false);
			genButton(false);
		}
	}
	
	function genButton(enabled) {
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
	
	return {
		attack : attack,
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
		.on("click", ".lwSlot[data-item-type='provision'] span, .lwSlot[data-item-type='potion'] span", backspan, inventory.consume);

	$("[data-attack='lw']").on("click", lw.attack);
	$("[data-flee='lw']").on("click", lw.flee);
	$("[data-random='lw']").on("click", lw.random);
	market.init("lwGamebookContent");
	$("[data-market]")
		.on("click", "#marketForSale [data-id]:not([data-stock='0'])", market.buy)
		.on("click", "#marketForPurchase [data-id]:not([data-stock='0'])", market.sell)
		.on("click", "[data-market-close]", market.close);
});