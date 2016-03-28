var sor4 = (function() {
	var nextDart = 0;
	var player = 0;
	
	function isPlayer() {
		return player == 0;
	}
	function getName() {
		return player == 0 ? "hero" : "guard" + player;
	}
	
	function init() {
		// TODO: save and reload both on FE and BE side
		if ($("#sor4TenUpTrigger").length > 0) {
			$("#choiceWrapper").hide();
		}
	}
	function throwDart() {
		$.ajax({
			url : "throwNextDart"
		}).done(function(result) {
			var $dart = $("#sor4Dart" + nextDart);
			$dart.show();
			$(".sor4TenUpReport").append(result.message);
			$("#currentScore").text(result.newTotal);
			$dart.attr("class", "").addClass("w" + result.x).addClass("h" + result.y);
			nextDart = (nextDart + 1) % 2;
			if (result.winner) {
				if (isPlayer()) {
					inventory.loadInventory();
				}
				$("#sor4TenUpTrigger").hide();
				$("." + result.winnerName + "Won").show();
				if (result.gold < 2) {
					$("[href='s-481|1'],[href='s-397|2'],[href='1'],[href='2']").remove();
				} else {
					$("[href='s-397|3'],[href='3']").remove();
				}
				$("#choiceWrapper").show();
			} else {
				player = (player + 1) % 4;
				$(".nextThrower").hide();
				$("." + getName()).show();
			}
		});
	}
	
	return {
		init : init,
		next : throwDart
	};
})();

$(function() {
	$("#sor4TenUpTrigger").click(sor4.next);
	sor4.init();
});