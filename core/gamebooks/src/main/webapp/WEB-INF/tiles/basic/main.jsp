<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="${cookie.lang.value}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="shortcut icon" href="http://zagor.hu/favicon_j.ico" />

<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/reset.css" />" />
<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/jquery-ui.css" />" />
<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/zagor.css" />" />
<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/bze.css" />" />
<c:forEach var="cssResource" items="${resources.css}">
	<link type="text/css" rel="stylesheet" href="<c:url value="../resources/${cssResource}" />" />
</c:forEach>
<c:forEach var="cssResource" items="${helpDescriptor.resourceInformation.cssResources}">
	<link type="text/css" rel="stylesheet" href="<c:url value="../book/resources/${cssResource}.css" />" />
</c:forEach>

<script type="text/javascript" src="<c:url value="/resources/js/jquery.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/rijndael.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/bze.js" />"></script>
<c:forEach var="jsResource" items="${resources.js}">
	<script type="text/javascript" src="<c:url value="../resources/${jsResource}" />"></script>
</c:forEach>

<title><spring:message code="${pageTitle}" text="${pageTitle}" /></title>
</head>
<body>
	<div class="feedbackBox">
	</div>
	<c:forEach items="${paragraph.rewards}" var="rewardUrl">
		<div class="mainRewardBox">
			<img alt="new reward" src="${rewardUrl}" /><br />
			<spring:message code="page.book.reward.new" />
		</div>
	</c:forEach>

	<table id="BodyTable">
		<col width="auto" />
		<col width="29px" />
		<col width="1002px" />
		<col width="29px" />
		<col width="auto" />
		<tr>
			<td id="LeftSideTable"></td>
			<td id="TableHeader" colspan="3">
				<div id="ForumMenu">
					<tiles:insertAttribute name="page.menu" />
				</div>
			</td>
			<td id="RightSideTable"></td>
		</tr>
		<tr>
			<td id="BalKozepso"></td>
			<td id="BalCsik"></td>
			<td id="MainContent">
				<div id="main">
					<tiles:insertAttribute name="page.content" />
				</div>
			</td>
			<td id="JobbCsik"></td>
			<td id="JobbKozepso"></td>
		</tr>
		<tr>
			<td></td>
			<td colspan="3" id="BottomCloser"><tiles:insertTemplate template="footer.jsp" /></td>
			<td></td>
		</tr>
	</table>
</body>
</html>
