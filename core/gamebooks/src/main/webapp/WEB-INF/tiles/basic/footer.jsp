<%@taglib tagdir="/WEB-INF/tags" prefix="l"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div id="Footer">
	<span id="FooterInfo">
		<spring:message code="page.footer.allRightsReserved" />
		<l:miniornament />
		<l:footerlink code="9a41e113b7ccb2e0415c6f32a63af7e29c32b837056c0f8bc3663db313a06256" name="runner" />
		<l:miniornament />
		<spring:message code="page.footer.contact" />
		<l:footerlink code="5e3d01bdbc0fe943ec88e3db028ab703262e5c1feddf2bfc5192a50019b72bda" name="info@zagor.hu" />
		<l:miniornament />
		<spring:message code="page.footer.creator" />
		<l:footerlink code="cd04d34468e9308360c6aa75715f9e5dd126f6f27951db91ec208b286c3b9363" name="FireFoX" />
        <l:miniornament />
        <spring:message code="page.footer.poweredBy" />
    </span>
    <span id="FooterCompatibility">
        <spring:message code="page.footer.browsers" />
		<spring:message code="page.footer.resolution" /> 1280x1024
	</span>
</div>