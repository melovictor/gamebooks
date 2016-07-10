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
		$("#unearnedGamebookRewards").on("click", "img:not(.noDescription)", showDescription);
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
				url : "http://zagor.hu/getrewards.php",
				data : {
					bookId : bookId,
					userId : userId
				},
				type : "post",
				success : function(response) {
					var $earned = $("<div>");
					var $unearned = $("<div>");
					var hasEarned = false;
					var hasUnearned = false;
					$(response).each(function(idx, elem) {
						if (elem.rewardImage) {
							$earned.append($("<img src='http://zagor.hu/img/reward/" + elem.rewardImage + "' />"));
							hasEarned = true;
						} else {
							var $img = $("<img src='http://zagor.hu/img/reward/placeholder/" + elem.placeholder + "' />");
							if (elem.desc1 == "") {
								$img.addClass("noDescription");
							} else {
								$img.data("desc1", elem.desc1);
								$img.data("desc2", elem.desc2);
							}
							$unearned.append($img);
							hasUnearned = true;
						}
					});
					if (hasEarned) {
						$("#earnedGamebookRewards").append($earned).show();
					} else {
						$("#earnedGamebookRewards").remove();
					}
					if (hasUnearned) {
						$("#unearnedGamebookRewards").append($unearned).show();
					} else {
						$("#unearnedGamebookRewards").remove();
					}
				}
			});
		}
	}
	function showDescription(img) {
		var $img = $(img.target);
		var $container = fetchContainer();
		var desc1 = $img.data("desc1");
		var desc2 = $img.data("desc2");
		var contText = $container.text();
		if (desc1 == contText || desc2 == contText) {
			$container.text(desc2);
		} else {
			$("img.selected").removeClass();
			$container.text(desc1);
			$img.addClass("selected");
		}
		
	}
	function fetchContainer() {
		var $container = $("#unearnedDescription");
		if ($container.length == 0) {
			$container = $("<div id='unearnedDescription'>");
			$("#unearnedGamebookRewards .title").after($container);
		}
		return $container;
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
