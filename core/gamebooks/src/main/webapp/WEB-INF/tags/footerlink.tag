<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zagor.hu/utilfunctions" prefix="util"%>
<%@ tag body-content="scriptless" language="java" pageEncoding="UTF-8"%>
<%@ attribute name="code" required="true"%>
<%@ attribute name="name" required="true"%>

<a class="pointer" onclick="javascript:window.location.href = doFullDecrypt('${code}'); return false;">${name}</a>