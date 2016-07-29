function allowNavigation() {
	window.onbeforeunload = null;
}
function preventMultipleNavigation() {
	$("a").on("click", function(e) {
		e.preventDefault();
	});
}

var inventory = (function() {
	var dialogContent;
	function loadInventory(shouldShow) {
		$.ajax({
			url : "s/inventory"
		}).done(function(response) {
			var inventoryVisible = isInventoryVisible();
			$("#gamebookCharacterPageWrapper").html(response);
			if (shouldShow || inventoryVisible) {
				$("#inventory").show();
			}
			initTitles();
		});
	}
	
	function isInventoryVisible() {
		return $("#inventory").css("display") == "block";
	}
	
	function changeEquip($event) {
		var $item = $($event.currentTarget);
		if ($event.data) {
			$item = $event.data($item);
		}
		var itemId = $item.data("item-id");
		var currentState = $item.data("item-equipped");
		$.ajax({
			url : "changeEquipState/" + itemId + "/" + !currentState
		}).done(function() {
			loadInventory(true);
		});
	}

	function consume($event) {
		var $item = $($event.currentTarget);
		if ($event.data) {
			$item = $event.data($item);
		}
		var itemId = $item.data("item-id");
		$.ajax({
			url : "consume/" + itemId
		}).done(function(result) {
			if (result.message) {
				showFeedback(result.message);
			}
			if (result.onclick) {
				$(result.onclick).click();
			} else {
				loadInventory(true);
			}
		});
	}


	var $itemToGather;
	function replaceItem() {
		showInventory(null, null, true);
		$itemToGather = $(this);
		$("[data-items]").attr("data-offer-for-replacement", "");
		var $exemptions = $itemToGather.closest("[data-replace-excempt]");
		if ($exemptions.length > 0) {
			var noReplace = $exemptions.data("replace-excempt").split(",");
			$(noReplace).each(function(idx, itemId) {
				$("[data-item-id='" + itemId + "']").attr("data-not-replace-candidate", "");
			});
		}
	}
	function replaceItemWith() {
		$("[data-offer-for-replacement]").removeAttr("data-offer-for-replacement");
		var gatherId = $itemToGather.data("id");
		var amount = $itemToGather.data("amount");
		if (!amount)  {
			amount = 1;
		}
		var loseId = $(this).data("item-id");
		var data = {
			loseId : loseId,
			gatherId : gatherId,
			amount : amount
		}
		
		inventoryAnimationOngoing = false;
		takeItemCall("replace", $itemToGather, amount, data);
		$itemToGather = null;
		$("[data-not-replace-candidate]").removeAttr("data-not-replace-candidate");
	}
	
	function takeItem(event, url, baseData) {
		var that = $(event.target);
		var id = that.data("id");
		var amount = that.data("amount");
		if (!amount) {
			amount = 1;
		}
		var data = baseData ? baseData : {};
		data.itemId = id;
		data.amount = amount;

		takeItemCall(url ? url : "take", that, amount, data);
	}
	function takeItemCall(url, that, amount, data) {
		$.ajax({
			url : url,
			data : data,
			method : "POST"
		}).done(function(response) {
			var totalTaken = response.totalTaken;
			if (amount == totalTaken) {
				unbind(that);
			} else {
				that.data("amount", amount - totalTaken);
			}
			if (response.warningMessage) {
				showFeedback(response.warningMessage);
			}
			if (totalTaken > 0) {
				unbindGroup(that);
			}
			loadInventory();
		});
	}
	function unbindGroup(elem) {
		var groupName = elem.data("group");
		if (groupName) {
			var group = $("[data-item-choice=" + groupName + "]");
			var maxItems = group.data("max-choice");
			if (maxItems == 1) {
				var groupItems = $(".takeItem[data-group=" + groupName + "]")
				unbind(groupItems);
			} else {
				group.data("max-choice", maxItems - 1);
			}
		}
	}
	function unbind(elem) {
		elem.removeAttr("class");
		elem.unbind("click");
	}

	function displayMap() {
		var dialogId = "#" + $(this).data("map");
		$(dialogId).dialog({
			width : 600
		});
	}

	function displayNotes(button) {
		var dialogId = "#" + $(this).data("notes");
		var dialog = $(dialogId);
		var isOpen;
		try {
			isOpen = dialog.dialog("isOpen");
		} catch (exception) {
			isOpen = false;
		}

		if (!isOpen) {
			if (dialogContent == null) {
				dialogContent = dialog.find("textarea").val();
			} else {
				dialog.find("textarea").val(dialogContent);
			}
			dialog.dialog({
				width : 600,
				height : 600,
				maxWidth : 1002,
				buttons : [ {
					"data-notes-save" : "",
					type : "button",
					text : $("#saveNotesLabel").attr("value"),
					id : "saveNotes",
					click : inventory.saveNotes
				} ]
			});

		}
	}

	function saveNotes() {
		var dialog = $(this);
		var notesText = dialog.find("textarea").val();
		dialogContent = notesText;
		$.ajax({
			url : "s/inventory/notes",
			data : {
				notes : notesText
			},
			type : "post"
		});
		dialog.dialog("close");
	}

	var inventoryAnimationOngoing = false;
	function showInventory(event, providedSelector, disableClosing) {
		if (!inventoryAnimationOngoing) {
			inventoryAnimationOngoing = true;
			var selector = (providedSelector ? providedSelector : $("[data-hidden-character-page]").data("hidden-character-page"));
			var cp = $(selector);
			cp.show(300, function() {
				if (!disableClosing) {
					inventoryAnimationOngoing = false;
				}
			});
		}
	}

	function hideInventory() {
		if (!inventoryAnimationOngoing) {
			inventoryAnimationOngoing = true;
			var selector = $("[data-hidden-character-page]").data("hidden-character-page");
			var cp = $(selector);
			cp.hide(300, function() {
				inventoryAnimationOngoing = false;
			});
		}
	}

	function stickInventory(selector) {
		inventoryAnimationOngoing = true;
		$(selector).show();
	}

	var droppingInTotal;
	function prepareSingleDrop() {
		prepareDrop(1);
	}
	function prepareMultiDrop() {
		prepareDrop(999);
	}
	function prepareDrop(itemsToDrop) {
		droppingInTotal = itemsToDrop;
		$("#gamebookCharacterPageWrapper").addClass("dropping");
	}
	function dropItem($event) {
		droppingInTotal--;
		var $elem = $(this);
		if ($event.data) {
			$elem = $event.data($elem);
		}
		var itemId = $elem.data("itemId");
		$.ajax({
			url : "drop/" + itemId,
			type : "post",
			complete : function() {
				loadInventory();
			}
		});
		
		if (droppingInTotal <= 0) {
			finishDrop();
		}
	}
	function finishDrop() {
		$("#gamebookCharacterPageWrapper").removeClass("dropping");
	}

	function initTitles() {
		$("span[title], div[title]").tooltip({
			content: function() {
				return $(this).attr("title");
			},
			show: false,
			hide: false
		});
	}
	
	return {
		loadInventory : loadInventory,
		takeItem : takeItem,
		displayMap : displayMap,
		displayNotes : displayNotes,
		saveNotes : saveNotes,
		showInventory : showInventory,
		hideInventory : hideInventory,
		stickInventory : stickInventory,
		changeEquip : changeEquip,
		consume : consume,
		replaceItem : replaceItem,
		replaceItemWith : replaceItemWith,
		prepareSingleDrop : prepareSingleDrop,
		prepareMultiDrop : prepareMultiDrop,
		finishDrop : finishDrop,
		dropItem : dropItem,
		initTitles : initTitles
	};
})();

var form = (function() {
	function submitForm(method, url, location) {
		var form = $("<form action='" + url + "#" + location + "' method='" + method + "' />");
		$("body").append(form);
		allowNavigation();
		form.submit();
	}

	return {
		submit : submitForm
	}
})();

var userInput = (function() {
	function initialize() {
		if ($("#userInputSection").length > 0) {
			$("#elapsedTime").val(new Date().getTime());
			$("#userInputForm").on("submit", handleSubmit);
		}
	}
	
	function isValid() {
		var $input = $("#responseText");
		var isValid = true;
		if ($input.attr("type") == "number") {
			var value = parseInt($input.val());
			var min =  parseInt($input.attr("min"));
			var max = parseInt($input.attr("max"));
			isValid = !isNaN(value);
			isValid &= isNaN(min) || value >= min;
			isValid &= isNaN(max) || value <= max;
		}
		return isValid;
	}
	
	function handleSubmit(event) {
		if (!isValid()) {
			event.preventDefault();
		} else {
			$elapsedTimeField = $("#elapsedTime");
			$elapsedTimeField.val(new Date().getTime() - $elapsedTimeField.val());
			allowNavigation();
		}
	}
	
	return {
		init : initialize
	}
})();


var raw = (function() {
	function random() {
		form.submit("post", "random", "actionEnd");
	}
	return {
		random : random
	};
}());

var gameMenu = (function() {
	function saveGame() {
		$.ajax({
			url : "s/save",
			success : function(message) {
				showFeedback(message);
			}
		});
	}

	return {
		saveGame : saveGame
	};
})();

var market = (function() {
	var totalPurchases;
	var requiredPurchases;
	var currentGold;
	var requiredGold;
	var requiredSalesExactly;
	var totalSales;
	var singleCcy;
	var multiCcy;

	function init(contentId) {
		var $content = $("#marketContent");
		if ($content.length > 0) {
			var $placeholder = $("[data-market]");
			$placeholder.append($content);
			$("#" + contentId).addClass("marketAvailable");
		}

		totalPurchases = 0;
		totalSales = 0;
		requiredPurchases = parseInt($("#mustBuy").val());
		currentGold = parseInt($("#currentGold").val());
		requiredGold = parseInt($("#mustHaveGold").val());
		requiredSalesExactly = parseInt($("#mustSellExactly").val());
		singleCcy = $("#singleCcy").val();
		multiCcy = $("#multipleCcy").val();
		
		if ($content.length > 0) {
			updateCurrentBalance();
		}
		
		if (requiredPurchases > totalPurchases || requiredGold > currentGold || (requiredSalesExactly > 0 && requiredSalesExactly > totalSales)) {
			$("#marketCommandFinish").hide();
		}
	}
	
	function updateCurrentBalance() {
		var $balanceField = $("#marketBalanceActual");
		var balanceText;
		if (currentGold == 1) {
			balanceText = singleCcy.replace("{0}", currentGold);
		} else {
			balanceText = multiCcy.replace("{0}", currentGold);
		}
		$balanceField.text(balanceText);
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
					updateStock($elem);
				}
				updateElements(data);
			}
		});
	}
	
	function updateStock($elem) {
		var newStock =  $elem.attr("data-stock") - 1;
		var $marketStock = $elem.parent().find(".marketStock");
		if (newStock == 1) {
			$marketStock.remove();
		} else if (newStock > 1) {
			$marketStock.find(".amount").text(newStock);
		}
		$elem.attr("data-stock", $elem.attr("data-stock") - 1);
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
						updateStock($elem);
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
		updateCurrentBalance();
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
	$("#saveGameLink").on("click", gameMenu.saveGame);
	$("span.takeItem").click(inventory.takeItem);
	$("span.replaceItem").click(inventory.replaceItem);
	$("#gamebookCharacterPageWrapper")
		.on("click", "span[data-map]", inventory.displayMap)
		.on("click", "span[data-notes]", inventory.displayNotes)

		.on("click", "[data-drop-single-item]", inventory.prepareSingleDrop)
		.on("click", "[data-drop-multiple-items]", inventory.prepareMultiDrop)
		.on("click", "[data-end-drop]", inventory.finishDrop);
	$("#main").on("click", "#gamebookCharacterPageWrapper.dropping [data-item-removable='true']", inventory.dropItem);

	$("button[data-notes-save]").click(inventory.saveNotes);
	$("[data-hidden-character-page]")
		.on("mouseenter", inventory.showInventory)
		.on("mouseleave", inventory.hideInventory);
	$("[data-random='raw']").on("click", raw.random);
	userInput.init();
	
	$("a[href]").on("click", allowNavigation);
	$("#MainContent a[href]:not([data-no-multi-navigation])").on("click", preventMultipleNavigation);
	$("#responseText").focus();
	inventory.initTitles();
});
