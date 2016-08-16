<%@tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@attribute name="disc" required="true" type="java.lang.String" %>

<span data-id="${disc}" data-available="${data.character.kaiDisciplines[disc]}"><spring:message code="page.lw.attribute.kai.${disc}" /></span>