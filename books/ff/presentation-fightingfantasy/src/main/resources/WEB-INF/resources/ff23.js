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
			data : {},
			type : "POST",
			accept : "application/json; charset=utf-8",
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			complete : function(data) {
				button.prop("disabled", false);
				var json = data.responseJSON;
				$("#ff23Tiger").attr("class", convertPosition(json.tigerPosition));
				$("#ff23Dog").attr("class", convertPosition(json.dogPosition));
				if (json.huntFinished) {
					$("#choice li a:not([href$='" + json.nextSectionPos + "'])").parent().remove();
					$("#choice").show();
					$("#ff23HuntTrigger").remove();
				}
				report(json.roundMessage);
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
