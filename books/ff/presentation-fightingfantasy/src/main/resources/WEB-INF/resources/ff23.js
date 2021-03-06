var ff23HuntTrigger = (function() {
	
	function init() {
		if ($("#ff23HuntFinished").length > 0) {
			$("#choice li a:not([href^='s-" + $("#ff23HuntFinished").val() + "'])").parent().remove();
			$("#ff23HuntTrigger").remove();
		} else if ($("#ff23HuntTrigger").length > 0) {
			$("#choice").hide();
		}
	}
	
	function roll() {
		var button = $("#ff23HuntTrigger");
		button.prop("disabled", true);
		$.ajax({
			url : "hunt",
			type : "POST",
			accept : "application/json; charset=utf-8",
			data : {
				"nothing" : "nothing"
			},
			success : function(data) {
				button.prop("disabled", false);
				$("#ff23Tiger").attr("class", convertPosition(data.tigerPosition));
				$("#ff23Dog").attr("class", convertPosition(data.dogPosition));
				if (data.huntFinished) {
					$("#choice li a:not([href$='" + data.nextSectionPos + "'])").parent().remove();
					$("#choice").show();
					$("#ff23HuntTrigger").remove();
				}
				report(data.roundMessage);
			}
		});
	}
	
	function convertPosition(pos) {
		var posX = pos.substring(0,1);
		var posY = parseInt(pos.substring(1));
		if (posX == "@" || posX == "I" || posY == 0 || posY == 13) {
			return "gX";
		}
		return "g" + pos.substring(0,1) + " g" + pos.substring(1);
		
	}
	
	function report(message) {
		var $container = $("#ff23HuntReport");
		$container.html($container.html() + message);
	}
	
	return {
		roll : roll,
		init : init
	};
})();

$(function() {
	$("#ff23HuntTrigger").on("click", ff23HuntTrigger.roll);
	ff23HuntTrigger.init();
});
