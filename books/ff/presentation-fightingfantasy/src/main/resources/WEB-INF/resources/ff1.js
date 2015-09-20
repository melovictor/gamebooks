var ff1 = (function() {
	function init() {
		if ($("#choiceWrapper li").length) {
			$("#failedWithKeys").show();
		}
	}
	
	return {
		init : init
	};
})();

$(function() {
	ff2.init();
});
