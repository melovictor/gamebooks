<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2><span class="attribute">ÉLETERŐ</span> és <span class="attribute">ÉLELEM</span></h2>

<p><span class="attribute">ÉLETERŐ</span> pontjaid sokszor fognak változni kalandjaid során, amint megküzdesz a szörnyekkel és elvállalsz lelkesítő feladatokat. Ahogy célodhoz közeledsz, <span class="attribute">ÉLETERŐ</span> pontjaidnak száma veszélyesen lecsökkenhet, és a csaták különösen kockázatossá válnak, ezért légy óvatos!</p>

<c:if test="${stdHelp_hasFood}">
    <p>Hátizsákodban ${stdHelp_foodAmount} étkezésre elegendő élelmiszer van.
    <c:if test="${stdHelp_canEatAlways}">
        Bármikor megállhatsz pihenni és enni, kivéve csata közben.
    </c:if>
    <c:if test="${!stdHelp_canEatAlways}">
        Csak olyankor állhatsz meg pihenni és enni, amikor a szöveg erre kifejezetten lehetőséget ad.
    </c:if>
    Minden étkezés 4 pontot ad <span class="attribute">ÉLETERŐ</span> pontjaidhoz, és 1 ponttal csökkenti Élelmiszer-tartalékodat. A <em>Kalandlapon</em> külön Élelmiszerkészlet négyzet van, hogy feljegyezd, mennyit fogyasztottál. Ne feledd, hogy hosszú utat kell megtenned, ezért bölcsen használd fel élelmiszeredet!</p>
</c:if>
<c:if test="${!stdHelp_hasFood}">
    <p>Ellentétben a többi Kaland, Játék, Kockázat könyvvel, most Élelmiszerkészlet nélkül kezded meg kalandjaidat, azonban a játék során lehetőséged lesz rá, hogy különböző módon növeld <span class="attribute">ÉLETERŐDET</span>.</p>
</c:if>

<p>Azt se feledd, hogy <span class="attribute">ÉLETERŐ</span> pontjaid száma sohasem lépheti túl <em>Kezdeti</em> értékét, kivéve, ha egy adott oldalon ezt az utasítást kaptad.<c:if test="${stdHelp_hasPotions}"> Az <span class="attribute">ERŐ</span> Italának megivásával (lásd odébb) bármikor kezdeti értékére állíthatod vissza <span class="attribute">ÉLETERŐDET</span>.</c:if></p>
