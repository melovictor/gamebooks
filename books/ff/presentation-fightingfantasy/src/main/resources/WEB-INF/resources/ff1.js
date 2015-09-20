var ff1 = (function() {
	function init() {
		if ($("#choiceWrapper li").length == 0) {
			$(".failedWithKeys").show();
		}
	}
	
	return {
		init : init
	};
})();

$(function() {
	ff1.init();
});
