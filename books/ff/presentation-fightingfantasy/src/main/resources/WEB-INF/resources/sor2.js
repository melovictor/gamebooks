var sor2 = (function() {
	function visitFlanker() {
		location.href = "visitFlanker";
	}
	return {
		visitFlanker : visitFlanker
	};
})();


$(function() {
	$("[data-assassin]").on("click", sor2.visitFlanker);
});
