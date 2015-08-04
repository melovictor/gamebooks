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

	return {
		updateLanguage : updateLanguage,
		updateSelection : updateSelection
	};
})();

var settings = (function() {
	function toggle() {
		var t = $(this);
		var id = t.attr("id");
		var other = $("#" + id.replace(/\./g, "\\.") + "-inverted");
		other.prop("checked", !t.prop("checked"));
	}

	function init(checkboxes) {
		checkboxes.click(settings.toggle);
		checkboxes.each(function(index, original) {
			var orig = $(original);
			var clone = orig.clone();
			clone.addClass("settings-inverted-checkbox");
			var id = clone.attr("id");
			clone.attr("id", id + "-inverted");
			var value = clone.attr("value");
			clone.attr("value", !value);
			var checked = clone.prop("checked");
			if (checked) {
				clone.removeAttr("checked");
			} else {
				clone.attr("checked", "checked");
			}
			orig.after(clone);
		});
	}

	return {
		toggle : toggle,
		initialize : init
	};
})();



$(function() {
	$("#languageSelectionOptions li").on("click", function() {
		menu.updateLanguage($(this));
	});
	menu.updateSelection();
	settings.initialize($("input[data-settings-checkbox]"));
	$("#adventurerName").focus();
});
