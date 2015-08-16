<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>FELSZERELÉS<c:if test="${stdHelp_hasPotions}"> ÉS ITALOK</c:if></h2>

<p>Minimális felszereléssel kezded kalandodat, de utad során találhatsz vagy vásárolhatsz holmikat. Karddal vagy felfegyverezve, és bőrpáncélt viselsz. Hátizsákodban étel, ital és azok a kincsek vannak, amelyeket megszerzel.<c:if test="${stdHelp_hasLantern}"> Lámpásod is van, amely megvilágítja az utadat.</c:if>

Tárgyaidat mindenkor jegyezd be, a <em>Kalandlapodon</em> lévő ,,Felszerelési tárgyak'' rovatba. Ha ezek közül bármelyiket használni fogod utad során, a történetből megtudod, vajon melyik semmisült meg vagy veszett el. Ilyenkor mindig jelöld a hiányt a <em>Kalandlapon</em>, és a kihúzott tárgyakat többé nem használhatod.</p>

<c:if test="${stdHelp_hasPotions}">
    <p>Emellett magaddal vihetsz egy üveggel a Varázsitalból, amely segít rajtad. Az alább felsorolt italok közül választhatsz:</p>

	<p>Az <span class="attribute">ÜGYESSÉG</span> Itala &ndash; kezdeti értékére állítja <span class="attribute">ÜGYESSÉG</span> pontjaidat.</p>
	<p>Az <span class="attribute">ERŐ</span> Itala &ndash; kezdeti értékére állítja <span class="attribute">ÉLETERŐ</span> pontjaidat.</p>
	<p>A <span class="attribute">SZERENCSE</span> Itala &ndash; kezdeti értékére állítja <span class="attribute">SZERENCSE</span> pontjaidat és 1 ponttal növeli <em>Kezdeti</em> <span class="attribute">SZERENCSÉDET</span>.</p>

	<p>Kalandjaid során bármikor kortyolhatsz a magaddal vitt italból (kivéve, amikor csatában állsz). Egyetlen adag ezen italok bármelyikéből visszaállítja <span class="attribute">ÜGYESSÉGEDET</span>, <span class="attribute">ÉLETERŐDET</span> vagy <span class="attribute">SZERENCSÉDET</span> <em>Kezdeti</em> értékére (és a <span class="attribute">SZERENCSE</span> Itala 1 ponttal növeli <em>Kezdeti</em> <span class="attribute">SZERENCSÉDET</span>).</p>
	<p>Minden üveg ${stdHelp_potionAmount} adagnyi varázsitalt tartalmaz, tehát ${stdHelp_potionAmountTimes} élhetsz vele kalandjaid során. Jegyezd fel a <em>Kalandlapon</em>, ha felhasználtad.</p>
	<p>Azt se feledd, hogy csak a három ital egyikét viheted magaddal utadra. Válassz tehát bölcsen!</p>
</c:if>
