<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="inventory">
    <c:if test="${hasInventory || hasMap}">
		<h1>
			<spring:message code="page.menu.book.inventory" />
		</h1>

		<c:if test="${not empty data.equipment}">
			<h2>
				<spring:message code="page.menu.book.inventory.equipments" />
			</h2>

			<ul>
				<c:forEach var="item" items="${data.equipment}">
					<li>${item}</li>
				</c:forEach>
			</ul>
		</c:if>

		<c:if test="${not empty data.info}">
			<h2>
				<spring:message code="page.menu.book.inventory.information" />
			</h2>
	
			<ul>
				<c:forEach var="item" items="${data.info}">
					<li>${item}</li>
				</c:forEach>
			</ul>
		</c:if>

	   <c:if test="${hasMap}">
            <h2>
                <span data-map="mapDialog">
                    <spring:message code="page.menu.book.inventory.map" />
                </span>
            </h2>
            <div id="mapDialog" class="popupMap" title="${bookInfo.title}">
			  <img src="resources/${bookInfo.resourceDir}/map.jpg" />
			</div>
	   </c:if>
    </c:if>
	<h2>
	    <span data-notes="notesDialog">
	        <spring:message code="page.menu.book.inventory.notes" />
	    </span>
	</h2>
	<div id="notesDialog" class="popupMap" title="${bookInfo.title}">
	  <textarea id="notes" spellcheck="false">${data.notes}</textarea>
	  <input type="hidden" value="<spring:message code="page.menu.book.inventory.notes.save" />" id="saveNotesLabel" />
	</div>
</div>
