<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<tiles:insertTemplate template="../basic/section-sectionNumber.jsp" />

<c:if test="${earnedRewards > 0}">
    <input type="hidden" id="rewardPoints" value="${earnedRewards}" />
    <div id="rewardTotal">
        <spring:message code="page.ff.attribute.rewards.toDistribute" />
        <span id="remainingRewardDisplay">${earnedRewards}</span>
        <button><spring:message code="page.ff.attribute.rewards.save" /></button>
    </div>
</c:if>
<tiles:insertTemplate template="../basic/section-mainContent.jsp" />

<p class="ffSmallImage" id="actionEnd">
    <img src="resources/${bookInfo.resourceDir}/image.small" alt="<spring:message code="page.ff.alt.smallImage" />" />
</p>

<div class="hr"></div>

<tiles:insertTemplate template="../${bookInfo.paragraphResolver.rulesetPrefix}/section-choices.jsp" />

<tiles:insertAttribute name="gamebook.command" />

<c:if test="${paragraph.id == 'generate'}">
    <script type="text/javascript">allowNavigation();</script>
</c:if>

<c:if test="${environmentDetector.seleniumTesting}">
   <script type="text/javascript">
     $(function() {
       inventory.stickInventory("#inventory");
     });
   </script>
</c:if>