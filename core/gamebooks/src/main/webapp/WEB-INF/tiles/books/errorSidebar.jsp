<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:url var="bookListUrl" value="/booklist" />
<c:url var="loadGameUrl" value="load" />
<c:if test="${empty bookList}">
    <ul>
	    <li><a href="${bookListUrl}">
	        <spring:message code="page.menu.book.close" />
	    </a></li>
	    
        <li><a href="<c:url value="new" />"><spring:message code="page.menu.book.restart" /></a></li>
        
        <c:if test="${haveSavedGame}">
           <li><a href="${loadGameUrl}"><spring:message code="page.menu.book.load" /></a></li>
        </c:if>
    </ul>
</c:if>
