function preventBack(warning) {
	window.onbeforeunload = function(e) {
		return warning;
	};
}

var images = (function() {
	function init() {
		var $images = $(".inlineImage[data-img]");
		var locale = $("html").attr("lang");
		$images.each(function(idx, elem) {
			var $e = $(elem);
			var book = $e.data("book");
			var img = $e.data("img");
			var type = $e.data("type");
			if (!type) {
				type = "";
			}
			var $img = $("<img>").attr("src", "http://zagor.hu/gamebooks/img.php?book=" + book + "&type=" + type + "&img=" + img + "&loc=" + locale);
			$e.append($img);
		});
	}

	return {
		init : init
	};
})();

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

function showFeedback(text) {
	var $feedback = $(".feedbackBox");
	$feedback.text(text);
	$feedback.fadeIn("slow");
	setTimeout(function() {
		$feedback.fadeOut("slow");
	}, 3000);
}

var newRewards = (function() {
	function init() {
		setTimeout(showNextBox, 250);
	}
	function showNextBox() {
		var $rewardBoxes = $(".mainRewardBox");
		if ($rewardBoxes.length > 0) {
			var $nextBox = $($rewardBoxes[0]);
			$nextBox.fadeIn("slow");
			setTimeout(function() {
				$nextBox.fadeOut("slow", function() {
					$nextBox.remove();
					showNextBox();
				});
			}, 3000);
		}
	}
	
	return {
		init : init
	};
})();

$(function() {
	$("#languageSelectionOptions li").on("click", function() {
		menu.updateLanguage($(this));
	});
	menu.updateSelection();
	$("#adventurerName").focus();
	$("#logout").on("click", menu.logout);
	newRewards.init();
	images.init();
});
