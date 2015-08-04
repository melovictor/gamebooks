<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zagor.hu/utilfunctions" prefix="util" %>
<%@ tag body-content="scriptless" language="java" pageEncoding="UTF-8"%>
<%@ attribute name="className" required="false" %>


<div class='TemakSmallTop'></div>
<div class='TemakMiddleSmall'>
	<div class='TemakMiddleSmallText ${className}'>
	   <jsp:doBody />
	</div>
</div>
<div class='TemakSmallBottom'></div>