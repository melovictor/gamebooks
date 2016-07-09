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

var bookRewards = (function() {
	function init() {
		setTimeout(showNextBox, 250);
		loadExistingRewards();
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
	function loadExistingRewards() {
		var $container = $("#gamebookRewards");
		var bookId = $("#bookId").val();
		var userId = $("#userId").val();
		if ($container.length > 0) {
			$.ajax({
				url : "http://zagor.hu/getrewards.php?bookId=" + bookId + "&userId=" + userId,
				data : {
					bookId : bookId,
					userId : userId
				},
				type : "post",
				success : function(response) {
					var $earned = $("<div>");
					var $unearned = $("<div>");
					$(response).each(function(idx, elem) {
						if (elem.rewardCode) {
							$earned.append($("<img src='http://zagor.hu/img/reward/" + elem.rewardImage + "' />"));
						} else {
							$unearned.append($("<img src='http://zagor.hu/img/reward/placeholder/" + elem.placeholder + "' />"));
						}
					});
					$container.append($earned);
					$container.append($unearned);
				}
			});
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
	bookRewards.init();
	images.init();
});
