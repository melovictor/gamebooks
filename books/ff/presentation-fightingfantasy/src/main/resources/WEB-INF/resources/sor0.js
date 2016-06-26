var sor0 = (function() {
	function init() {
		var $images = $(".sorSpellImage[data-img]");
		var locale = $("html").attr("lang");
		$images.each(function(idx, elem) {
			var $e = $(elem);
			var book = "sor0";
			var img = $e.data("img");
			var $img = $("<img>").attr("src", "http://zagor.hu/gamebooks/img.php?book=" + book + "&img=" + img + "&loc=" + locale);
			$e.append($img);
		});
	}

	return {
		init : init
	};
})();

$(function() {
	$("#choiceWrapper").removeClass('ffChoiceHidden');
	sor0.init();
});