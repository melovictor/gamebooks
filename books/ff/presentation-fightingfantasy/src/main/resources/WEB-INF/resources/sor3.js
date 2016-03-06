var sor3 = (function() {
	function useSnakeRing() {
		allowNavigation();
		location.href = "useSnakeRing";
	}

	return {
		useSnakeRing : useSnakeRing
	};
})();

$(function() {
	$("[data-serpent-ring]").on("click", sor3.useSnakeRing);
});
