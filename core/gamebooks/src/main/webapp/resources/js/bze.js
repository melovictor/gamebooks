function preventBack(warning) {
	window.onbeforeunload = function(e) {
		return warning;
	};
}

var menu = (function() {
	function updateLanguage(newElement) {
		changeLanguage(newElement.find("img").attr("alt"));
	}

	function changeLanguage(newLang) {
		window.location.href = "?lang=" + newLang;
	}

	function updateSelection() {
		$("#languageSelectionSelected").html($("#languageSelectionOptions .languageSelected").html());
	}
	
	function logout() {
		$("#logoutForm").submit();
	}

	return {
		updateLanguage : updateLanguage,
		updateSelection : updateSelection,
		logout : logout
	};
})();

$(function() {
	$("#languageSelectionOptions li").on("click", function() {
		menu.updateLanguage($(this));
	});
	menu.updateSelection();
	$("#adventurerName").focus();
	$("#logout").on("click", menu.logout);
});
