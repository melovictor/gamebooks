var ff15 = (function() {
	function missileAttack() {
		var enemyId = $("[data-enemy-selected='true']").data("enemy-id");
		var hit = $("#luckOnEnemyHit").is(":checked");
		var def = $("#luckOnSelfHit").is(":checked");
		var oth = $("#luckOnOther").is(":checked");
		form.submit("post", "attack?id=" + enemyId + "&missile=true&hit=" + hit + "&def=" + def + "&oth=" + oth, "ffEnemyList");
	}
	return {
		attack : missileAttack
	};
})();



$(function() {
	$("[data-attack='ff-m']").on("click", ff15.attack);
});

