<div id="userInputSection" class="ffUserInputSection">
    <form action="userInputResponse" method="post" id="userInputForm" autocomplete="off">
	    <label for="responseText">${userInputLabel}</label>
	    <input type="${responseType}" id="responseText" name="responseText" autocomplete="off" />
        <input type="hidden" name="elapsedTime" id="elapsedTime" />
    </form>
</div>