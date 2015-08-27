<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="choice">
	<ul>
		<c:forEach var="choice" items="${paragraph.data.choices}">
			<li>
                <c:set var="linkPrefix"><c:if test="${informativeSections}">s-${choice.id}|</c:if></c:set>
			    <a href="${linkPrefix}${choice.position}">
			        <c:choose>
						<c:when test="${not empty choice.text}">
                            ${choice.text}
                                <c:if test="${!hideChoiceSection}">
                                    (${choice.display})
                                </c:if>
                        </c:when>
						<c:otherwise>
							<spring:message code="book.choice.next" />
							<c:if test="${!hideChoiceSection}">
		                        (${choice.display})
		                    </c:if>
						</c:otherwise>
					</c:choose>
			    </a>
			</li>
		</c:forEach>
	</ul>
</div>
