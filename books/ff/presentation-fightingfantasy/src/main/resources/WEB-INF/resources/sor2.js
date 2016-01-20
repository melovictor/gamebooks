var sor2 = (function() {
	function visitFlanker() {
		location.href = "visitFlanker";
	}

	function enableBetValue1() {
		enableBetValue(1);
	}
	function enableBetValue3() {
		enableBetValue(3);
	}
	function enableBetValue(step) {
		var $bet = $("#bet");
		$bet.removeAttr("disabled");
		$bet.val(step);
		var totalGold = $("[data-attribute-gold]").text();
		$bet.attr("step", step);
		$bet.attr("max", totalGold);
		$("#doBet").removeAttr("disabled");
	}
	function disableBetValue() {
		var $bet = $("#bet");
		$bet.attr("disabled", "disabled");
		$bet.val(0);
		$("#doBet").attr("disabled", "disabled");
	}
	function initBetting() {
		$("#ogre").on("click", enableBetValue3);
		$("#barbarian").on("click", enableBetValue1);
		$("#noone").on("click", disableBetValue);
		$("#doBet").on("click", doBet);
	}
	function doBet() {
		$("#doBet,#ogre,#barbarian,#bet,#noone").attr("disabled", "disabled");
		var betValue = $("#bet").val();
		var participant = 4025 + $("#barbarian:checked").length;
		$.ajax({
			url : "makeBet",
			data : {
				value : betValue,
				participant : participant
			},
			type : "get",
			success : function(response) {
				$(".betting").html(response);
				inventory.loadInventory();
			}
		});
	}

	return {
		visitFlanker : visitFlanker,
		initBetting : initBetting
	};
})();


$(function() {
	$("[data-assassin]").on("click", sor2.visitFlanker);
	sor2.initBetting();
});
