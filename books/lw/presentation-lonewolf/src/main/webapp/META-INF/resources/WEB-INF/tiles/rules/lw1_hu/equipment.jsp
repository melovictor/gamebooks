<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<h1>Felszerelés</h1>

<p>A kai beavatottak zöld tunikáját és köpenyét viseled. Nem sok minden áll a rendelkezésedre az
	életben maradáshoz.</p>

<p>Minden vagyonod egy balta és egy Hátizsák egyetlen étkezésre elegendő Élelemmel. A derekadon
	bőrtarsoly függ Aranykoronákkal. Hogy megtudd, hány korona van benne, dobj egy tízoldalú kockával.
	Az eredménynek megfelelő számú Aranykorona lapul a tarsolyodban a kaland kezdetén.</p>

<p>A kolostor füstölgő romjai közt megleled Sommerlund térképét, amely föltünteti Holmgardot, a
	fővárost és Durenort, keleten. A biztonság kedvéért a térképet elrejted a tunikádba.</p>

<p>A következőket találod még:</p>

<ol start="0">
	<li>Pallos (Fegyverek)</li>
	<li>Kard (Fegyverek)</li>
	<li>Sisak (Különleges tárgyak). Ez 2 ponttal növeli <span class="attribute">szívóssági</span> pontjaid számát</li>
	<li>Két étkezésre elegendő élelem</li>
	<li>Páncéling (Különleges tárgyak). Ez 4 ponttal növeli <span class="attribute">szívóssági</span> pontjaid számát</li>
	<li>Buzogány (Fegyverek)</li>
	<li>Gyógyital (a Hátizsákba kerül). Ez 4 pontot adhat hozzá a <span class="attribute">szívóssági</span> pontjaid összegéhez,
		ha harc után elfogyasztod. A palackban csupán egyetlen adag van.</li>
	<li>Harci bot (Fegyverek)</li>
	<li>Lándzsa (Fegyverek)</li>
	<li>12 Aranykorona (Deréktarsoly)</li>
</ol>

<p>Hogy a fentiekből mit találsz, annak megállapításához ismét dobj egy tízoldalú kockával.</p>

<h2>Hogyan vidd a felszerelést?</h2>

<p>Immár megvan a felszerelésed, s az alábbi fölsorolásból megtudod, mit hogyan kell vinned.</p>

<ol start="0">
	<li>Pallos - kézben a helye.</li>
	<li>Kard - kézben kell vinni.</li>
	<li>Sisak - a fejeden viseled.</li>
	<li>Élelem - a Hátizsákban viszed.</li>
	<li>Páncéling - a testeden viseled.</li>
	<li>Buzogány - a kezedben viszed.</li>
	<li>Gyógyital - a Hátizsákba kerül.</li>
	<li>Harci bot - kézben kell vinni.</li>
	<li>Lándzsa - kézben viszed.</li>
	<li>Aranykoronák - a Deréktarsolyba kerülnek.</li>
</ol>

<tiles:insertTemplate template="../lw_hu/equipmentAmount.jsp" />
<tiles:insertTemplate template="../lw_hu/equipmentUsage.jsp" />
