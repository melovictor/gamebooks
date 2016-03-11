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

var rewards = (function() {
	var rewardPoints;
	var distributedRewards = {};

	function init() {
		rewardPoints = parseInt($("#rewardPoints").val());
	}
	function allowDistribution() {
		if (rewardPoints > 0) {
			generateButtons();
		}
	}

	function generateButtons() {
		var $affectedEntries = $("[data-reward-price]");
		var renderedButtons = 0;
		$affectedEntries.each(function(idx, elem) {
			var $elem = $(elem);
			var price = parseInt($elem.data("reward-price"));
			var attribName = $elem.data("attrib-name");
			if (rewardPoints >= price) {
				distributedRewards[attribName] = 0;
				renderIncreaseButton($elem, price, attribName);
				renderedButtons++;
			}
		});
		if (renderedButtons > 0) {
			$("#rewardTotal").show();
		}
	}

	function verifyButtons() {
		$(".increaseAttribute").each(function(idx, elem) {
			var $elem = $(elem);
			if ($elem.find("div").data("price") > rewardPoints) {
				$elem.remove();
			}
		});
	}

	function markIncrease() {
		$t = $(this);
		var attribs = $t.data("for");
		var price = $t.data("price");
		if (rewardPoints >= price) {
			distributedRewards[attribs] += 1;
			rewardPoints -= price;
			var $displaySpan = $("[data-attrib-name='" + attribs + "']");
			$displaySpan.addClass("rewardAltered");
			var $bonusDiceSpan = $displaySpan.find("span.bonusDice");
			if ($bonusDiceSpan.length > 0) {
				$bonusDiceSpan.removeClass("diced10" + (distributedRewards[attribs] - 1)).addClass("diced10" + distributedRewards[attribs]);
			} else {
				$("<span>").attr("class", "bonusDice diced101").insertAfter($displaySpan.find("span:not(.increaseAttribute):last"));
			}
			var curValueText = $displaySpan.text();
			var curValue = parseInt(curValueText);
			var nextValue = curValue + 1;
			$displaySpan.contents().first()[0].textContent = nextValue;
		}
		$("#remainingRewardDisplay").text(rewardPoints);
		verifyButtons();
	}
	function sendIncreaseRequest() {
		$("#rewardTotal button").prop("disabled", true);
		$(".increaseAttribute").remove();
		$.ajax({
			url : "new/distributeRewarPoints",
			data : distributedRewards,
			accept : "application/json; charset=utf-8",
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function() {
				inventory.loadInventory();
				$("#rewardTotal").remove();
				$(".rewardAltered").removeClass("rewardAltered");
			}
		});
	}

	function renderIncreaseButton($elem, price, attrib) {
		var $span = $("<span>").addClass("increaseAttribute").appendTo($elem);
		$("<div>").data("for", attrib).data("price", price).appendTo($span);
		$span.append("(" + price + ")");
	}

	return {
		init : init,
		allowDistribution : allowDistribution,
		markIncrease : markIncrease,
		sendIncreaseRequest : sendIncreaseRequest
	};
})();

var ff = (function() {
	function sendGenerationRequest(event, baseData) {
		var $defPotion = $("#ffDefaultPotion");
		var data = baseData ? baseData : {};
		if ($defPotion) {
			$defPotion.prop("disabled", true);
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
				$("[data-generator-button], .ffDice").hide();
				inventory.loadInventory();
				rewards.allowDistribution();
				$("[data-step='1']").trigger("stepFinished", data);
			}
		});
		$("[data-generator-button]").prop("disabled", true);
	}

	function attributeTest() {
		var action = $(this).data("action");
		form.submit("post", "attributeTest-" + action, "actionEnd");
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
		var enemyId = $("[data-enemy-selected=true]").data("enemyId");
		form.submit("post", "preFight?id=" + itemId + "&enemyId=" + enemyId, "ffEnemyList");
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

var ffInventory = (function() {
	function purchaseItem(event) {
		var gold = parseInt($("[data-attribute-gold]").text());
		var price = parseInt($(this).data("price"));
		if (gold < price) {
			// TODO: give feedback to user
		} else {
			inventory.takeItem(event, "purchase", { price : price });
		}
	}

	return {
		purchaseItem : purchaseItem
	};
})();


var market = (function() {
	var totalPurchases;
	var requiredPurchases;
	var currentGold;
	var requiredGold;
	var requiredSalesExactly;
	var totalSales;

	function init() {
		var $content = $("#marketContent");
		if ($content.length > 0) {
			var $placeholder = $("[data-market]");
			$placeholder.append($content);
			$("#ffGamebookContent").addClass("marketAvailable");
		}

		totalPurchases = 0;
		totalSales = 0;
		requiredPurchases = parseInt($("#mustBuy").val());
		currentGold = parseInt($("#currentGold").val());
		requiredGold = parseInt($("#mustHaveGold").val());
		requiredSalesExactly = parseInt($("#mustSellExactly").val());

		if (requiredPurchases > totalPurchases || requiredGold > currentGold || (requiredSalesExactly > 0 && requiredSalesExactly > totalSales)) {
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
				totalSales++;
				if (data.text) {
					$("<p>").html(data.text).insertBefore($("#marketCommandFinish"));
				}
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
		} else if (marketingForceFinished()) {
			close();
		}
		inventory.loadInventory();
	}
	
	function close() {
		if (marketingFinished() || marketingForceFinished()) {
			form.submit("post", "marketClose", "actionEnd");
		}
	}
	
	function marketingFinished() {
		return currentGold >= requiredGold && totalPurchases >= requiredPurchases && requiredSalesExactly == 0;
	}
	function marketingForceFinished() {
		return requiredSalesExactly > 0 && requiredSalesExactly == totalSales;
	}
	
	return {
		init : init,
		buy : buyItem,
		sell : sellItem,
		close : close
	};
})();


$(function() {
	$("#ffGamebookContent")
		.on("click", ".increaseAttribute div", rewards.markIncrease);
	$("#rewardTotal button").on("click", rewards.sendIncreaseRequest);
	rewards.init();
	$("[data-generator-button='ff']").on("click", ff.sendGenerationRequest);
	$("[data-attribute-test='ff']").on("click", ff.attributeTest);
	$("[data-random='ff']").on("click", ff.random);
	$("[data-attack='ff']").on("click", ff.attack);
	$("[data-flee='ff']").on("click", ff.flee);
	$("#ffEnemyList").on("click", "[data-enemy-selected='false']", battle.changeSelection);
	$("#ffMenu")
		.on("click", "#gamebookCharacterPageWrapper:not(.dropping) [data-items]:not([data-offer-for-replacement]) [data-item-equippable='true']", inventory.changeEquip)
		.on("click", "#gamebookCharacterPageWrapper:not(.dropping) [data-items]:not([data-offer-for-replacement]) [data-item-provision],[data-items]:not([data-offer-for-replacement]) [data-item-potion]", inventory.consume)

		.on("click", "[data-items][data-offer-for-replacement] [data-item-id]:not([data-item-removable='false'])", inventory.replaceItemWith);
	$("#preFightItems")
		.on("click", "[data-item-id]", ff.useBeforeFight);
	market.init();
	$("[data-market]")
		.on("click", "#marketForSale [data-id]:not([data-stock='0'])", market.buy)
		.on("click", "#marketForPurchase [data-id]:not([data-stock='0'])", market.sell)
		.on("click", "[data-market-close]", market.close);
	$(".purchaseItem").on("click", ffInventory.purchaseItem);
	battle.init();
});
