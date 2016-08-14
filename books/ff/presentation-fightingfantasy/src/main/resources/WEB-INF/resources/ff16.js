var ff16 = (function() {
	function rest() {
		$.ajax({
			url : "rest",
			method : "post",
			success : function() {
				inventory.loadInventory();
			}
		});
	}
	return {
		rest : rest
	}
})();

$(function() {
	$("#ffMenu").on("click", "#ff16Resting", ff16.rest)
});