var battle = (function() {
	function changeSelection($event) {
		var $newEnemy = $($event.currentTarget);
		$("[data-enemy-selected='true']").attr("data-enemy-selected", "false");
		$newEnemy.attr("data-enemy-selected", "true");
	}
	function init() {
		if ($("[data-enemy-selected='true']").length == 0) {
			$("[data-enemy-selected='false']:eq(0)").attr("data-enemy-selected", "true");
		}
	}
	
	return {
		changeSelection : changeSelection,
		init : init
	};
})();



var ff = (function() {
	function sendGenerationRequest() {
		var $defPotion = $("#ffDefaultPotion");
		var data = {};
		if ($defPotion) {
			$defPotion.attr("disabled", "disabled");
			data.potion = $defPotion.val();
		}
		data.other = "else";
		$.ajax({
			url : "new/generate",
			data : data,
			type : "get",
			accept : "application/json; charset=utf-8",
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				$.each(data, function(key, value) {
					$("#" + key).html(value);
				});
				$("#choiceWrapper").show();
				$("[data-generator-button='ff'], .ffDice").hide();
				inventory.loadInventory();
				$("[data-step='1']").trigger("stepFinished", data);
			}
		});
	}

	function attributeTest() {
		form.submit("post", "attributeTest", "actionEnd");
	}
	function random() {
		form.submit("post", "random", "actionEnd");
	}
	function flee() {
		form.submit("post", "flee", "ffEnemyList");
	}
	function attack() {
		var enemyId = $("[data-enemy-selected='true']").data("enemy-id");
		var hit = $("#luckOnEnemyHit").is(":checked");
		var def = $("#luckOnSelfHit").is(":checked");
		var oth = $("#luckOnOther").is(":checked");
		form.submit("post", "attack?id=" + enemyId + "&hit=" + hit + "&def=" + def + "&oth=" + oth, "ffEnemyList");
	}
	function useBeforeFight() {
		var itemId = $(this).data("item-id");
		form.submit("post", "preFight?id=" + itemId, "ffEnemyList");
	}

	return {
		sendGenerationRequest : sendGenerationRequest,
		attributeTest : attributeTest,
		random : random,
		attack : attack,
		useBeforeFight : useBeforeFight,
		flee : flee
	};
})();


var market = (function() {
	var totalPurchases;
	var requiredPurchases;
	var currentGold;
	var requiredGold;

	function init() {
		var $content = $("#marketContent");
		if ($content.length > 0) {
			var $placeholder = $("[data-market]");
			$placeholder.append($content);
			$("#ffGamebookContent").addClass("marketAvailable");
		}

		totalPurchases = 0;
		requiredPurchases = parseInt($("#mustBuy").val());
		currentGold = parseInt($("#currentGold").val());
		requiredGold = parseInt($("#mustHaveGold").val());
		
		if (requiredPurchases > totalPurchases || requiredGold > currentGold) {
			$("#marketCommandFinish").hide();
		}
	}
	
	function buyItem() {
		var $elem = $(this);
		$.ajax({
			url : "buyItem/" + $elem.data("id"),
			type : "get",
			accept : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				totalPurchases++;
				if (data.successfulTransaction) {
					$elem.attr("data-stock", $elem.attr("data-stock") - 1);
				}
				updateElements(data);
			}
		});
	}
	function sellItem() {
		var $elem = $(this);
		$.ajax({
			url : "sellItem/" + $elem.data("id"),
			type : "get",
			accept : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				if (data.giveUpMode && data.giveUpFinished) {
					close();
				} else {
					if (data.successfulTransaction) {
						$elem.attr("data-stock", $elem.attr("data-stock") - 1);
					}
					updateElements(data);
				}
			}
		});
	}
	
	function updateElements(data) {
		currentGold = data.gold;
		
		$("#marketForSale #row div:not([data-stock='0'])").each(function(idx, elem) {
			var $elem = $(elem);
			if ($elem.data("price") > currentGold) {
				$elem.attr("data-stock", "0");
			}
		});
		if (marketingFinished()) {
			$("#marketCommandFinish").show();
		}
		inventory.loadInventory();
	}
	
	function close() {
		if (marketingFinished()) {
			form.submit("post", "marketClose", "actionEnd");
		}
	}
	
	function marketingFinished() {
		return currentGold >= requiredGold && totalPurchases >= requiredPurchases;
	}
	
	return {
		init : init,
		buy : buyItem,
		sell : sellItem,
		close : close
	};
})();


$(function() {
	$("[data-generator-button='ff']").on("click", ff.sendGenerationRequest);
	$("[data-attribute-test='ff']").on("click", ff.attributeTest);
	$("[data-random='ff']").on("click", ff.random);
	$("[data-attack='ff']").on("click", ff.attack);
	$("[data-flee='ff']").on("click", ff.flee);
	$("#ffEnemyList").on("click", "[data-enemy-selected='false']", battle.changeSelection);
	$("#ffMenu")
		.on("click", "[data-items]:not([data-offer-for-replacement]) [data-item-equippable='true']", inventory.changeEquip)
		.on("click", "[data-items]:not([data-offer-for-replacement]) [data-item-provision],[data-items]:not([data-offer-for-replacement]) [data-item-potion]", inventory.consume)
		
		.on("click", "[data-items][data-offer-for-replacement] [data-item-id]:not([data-item-removable='false'])", inventory.replaceItemWith);
	$("#preFightItems")
		.on("click", "[data-item-id]", ff.useBeforeFight);
	market.init();
	$("[data-market]")
		.on("click", "#marketForSale [data-id]:not([data-stock='0'])", market.buy)
		.on("click", "#marketForPurchase [data-id]:not([data-stock='0'])", market.sell)
		.on("click", "[data-market-close]", market.close);
	
	battle.init();
});

