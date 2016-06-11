var ff20 = (function() {
	function sendGenerationRequest() {
		var baseData = {
			specialSkill : $("[name='specialSkill']:checked").val()
		};
		$("[name='specialSkill']").prop("disabled", true);
		ff.sendGenerationRequest(null, baseData);
	}
	
	return {
		sendGenerationRequest : sendGenerationRequest
	}
})();

$(function() {
	$("[data-generator-button='ff20']").on("click", ff20.sendGenerationRequest);
});