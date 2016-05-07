var ff60 = (function() {
	function init() {
		$("#gamebookCharacterPageWrapper")
			.on("click", "[data-spell-luck]", triggerLuckSpell)
			.on("click", "[data-spell-skill]", triggerSkillSpell);
	}
	
	function triggerLuckSpell() {
		$.ajax({
			url : "luckSpell",
			complete : function() {
				inventory.loadInventory();
			}
		});
	}
	
	function triggerSkillSpell() {
		$.ajax({
			url : "skillSpell",
			complete : function() {
				inventory.loadInventory();
			}
		});
	}
	
	return {
		init : init
	};
})();

$(function() {
	ff60.init();
});