var ff2 = (function() {
	var maxSpells;
	var selectedSpells = 0;
	var autoTriggerTaking = false;
	
	function initCharacterGenerationStep2(event, data) {
		$("[data-generator-button='ff']").hide();
		$("[data-step='2']").show();
		$("#choiceWrapper").hide();
		maxSpells = data.ffSpellNumeric;
		$("[data-generator-button='ff2']").prop("disabled", false).show();
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
		$("[data-generator-button='ff2']").prop("disabled", true).show();
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
	
	var sixPick = (function() {
		var totalGold;
		var $inputs;
		function init() {
			totalGold = parseInt($("[data-attribute-gold]").text());
			$inputs = $("#sixPick input[type=number]");
			
			if ($inputs.length > 0) {
				$inputs.each(function(idx, elem) {
					var $elem = $(elem);
					$elem.attr("max", totalGold);
				});
				if ($("#sixPick[data-first-round='true']").length > 0) {
					$("#choiceWrapper").hide();
				}
			}
		}
		function validate() {
			var remainingGold = totalGold;
			$inputs.each(function(idx, elem) {
				remainingGold -= parseInt($(elem).val());
			});
			if (remainingGold < 0) {
				$t = $(this);
				$t.val($t.attr("max"));
				remainingGold = 0;
			} 
			$inputs.each(function(idx, elem) {
				var $elem = $(elem);
				var currentBet = parseInt($elem.val());
				$elem.attr("max", currentBet + remainingGold);
			});
		}
		function roll() {
			var payload = {
				"n1" : $("#num1").val(),
				"n2" : $("#num2").val(),
				"n3" : $("#num3").val(),
				"n4" : $("#num4").val(),
				"n5" : $("#num5").val(),
				"n6" : $("#num6").val()
			};
			$.ajax({
				url : "sixPick",
				type : "POST",
				contentType : "application/json",
				accept : "application/json",
				dataType : 'json',
				data : JSON.stringify(payload),
				success : function(response) {
					if (response.status == 0) {
						totalGold = response.newGold;
						$("#rollResult").html(response.responseText);
						$inputs.each(function(idx, elem) {
							$(elem).val(0);
						});
						$("#choiceWrapper").show();
						inventory.loadInventory();
						if (totalGold == 0) {
							$("#sixPick button").remove();
						}
					}
				}
			});
		}
		return {
			initialize : init,
			validate : validate,
			roll : roll
		}
	})();
	
	return {
		initCharacterGenerationStep2 : initCharacterGenerationStep2,
		initPickNewSpells : initPickNewSpells,
		finalizeCharacter : finalizeCharacter,
		selectSpell : selectSpell,
		sixPick : sixPick
	};
})();

$(function() {
	$("[data-step='1']").on("stepFinished", ff2.initCharacterGenerationStep2);
	$("[data-generator-button='ff2']").on("click", ff2.finalizeCharacter);
	$("[data-spell] span").on("click", ff2.selectSpell);
	$("[data-pick-spells]").on("click", ff2.initPickNewSpells);
	$("#sixPick input").on("change", ff2.sixPick.validate);
	$("#sixPick button").on("click", ff2.sixPick.roll);
	ff2.sixPick.initialize();
});
