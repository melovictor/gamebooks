<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<h2>
    <span data-notes="notesDialog">
        <spring:message code="page.menu.book.inventory.notes" />
    </span>
</h2>
<div id="notesDialog" class="popupMap" title="${bookInfo.title}">
  <textarea id="notes" spellcheck="false">${data.notes}</textarea>
  <input type="hidden" value="<spring:message code="page.menu.book.inventory.notes.save" />" id="saveNotesLabel" />
</div>
