<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<h1>Felszerelés</h1>

<p>D’Val kapitány elvezet a Királyi fegyvertárba, ahol a zöld tunikádat és Kai köpenyedet
	megvarrják és kitisztítják. Amíg ez megtörténik, az Őrkapitány átad neked egy zacskónyi
	aranykoronát az útra. Hogy megtudd, mennyi pénzt kaptál, dobj egy tízoldalú kockával, és ehhez a
	számhoz adj hozzá 10-et! Ezt az értéket jegyezd fel az Akciótáblázat Deréktarsoly rovatába. Ha
	sikeresen teljesítetted az első könyvet, akkor vagyonod az ott megszerzett Aranykoronákkal is
	bővül, de ne feledd, maximum 50 érme lehet nálad!</p>

<p>Egy nagy asztal található a fegyvertár közepén, melyen az alábbi felszerelések találhatóak:</p>

<ul>
	<li>Kard (Fegyverek)</li>
	<li>Rövid kard (Fegyverek)</li>
	<li>Két étkezésre elegendő élelem</li>
	<li>Páncéling (Különleges tárgyak). Ez 4 ponttal növeli <span class="attribute">szívóssági</span>
		pontjaid számát
	</li>
	<li>Buzogány (Fegyverek)</li>
	<li>Gyógyital (a Hátizsákba kerül). Ez 4 pontot adhat hozzá a <span class="attribute">szívóssági</span>
		pontjaid összegéhez, ha harc után elfogyasztod. A palackban csupán egyetlen adag van.
	</li>
	<li>Harci bot (Fegyverek)</li>
	<li>Lándzsa (Fegyverek)</li>
	<li>Pajzs (Különleges tárgyak). Ha harcolsz, hozzáadhatsz <span class="attribute">Harci
			Ügyesség</span> pontjaidhoz 2-t!
	</li>
	<li>Pallos (Fegyverek)</li>
</ul>

<p>Az alábbiak közül bármely két tárgyat magadhoz veheted! (Ha végigjátszottad az első könyvet
	lehetőséged van lecserélni a fegyvereidet.) Ha szükséges, akkor végezd el a módosításokat Harci
	Ügyesség és Szívósság pontjaid értékeinél.</p>

<h2>Hogyan vidd a felszerelést?</h2>

<p>Immár megvan a felszerelésed, és az alábbi felsorolásból megtudod, mit hogyan kell vinned.
	Nem szükséges jegyzeteket készítened, bármikor visszalapozhatsz ehhez a listához a kaland során.</p>

<ol start="0">
	<li>Kard - kézben kell vinni.</li>
	<li>Rövid kard - kézben kell vinni.</li>
	<li>Élelem - a Hátizsákban viszed.</li>
	<li>Páncéling - a testeden viseled.</li>
	<li>Buzogány - a kezedben viszed.</li>
	<li>Gyógyital - a Hátizsákba kerül.</li>
	<li>Harci bot - kézben kell vinni.</li>
	<li>Lándzsa - kézben viszed.</li>
	<li>Pajzs - a vállad fölött lóg egy szíjon, amikor nem harcolsz, egyébként kézben tartod.</li>
	<li>Pallos - kézben a helye.</li>
</ol>

<tiles:insertTemplate template="../lw_hu/equipmentAmount.jsp" />
<tiles:insertTemplate template="../lw_hu/equipmentUsage.jsp" />
