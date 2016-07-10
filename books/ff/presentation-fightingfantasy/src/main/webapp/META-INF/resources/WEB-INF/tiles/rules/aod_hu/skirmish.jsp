<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page">
	<h2>Csaták</h2>

	<p>
		A Kaland, Játék, Kockázat sorozat többi kötetétől eltérően ebben a kalandban nem küzdesz mindig
		egyedül. Egy hadsereget is irányítasz, mely különböző fajú lények egységeiből áll. Sereged
		kezdetben Törpékből, Elfekből, harcosokból és lovagokból áll, de később másféle lények is fognak
		majd csatlakozni hozzád. A harcban elesett katonákat mindig 5 fős csoportokban veszíted el, és ha
		több egység is harcba keveredett, te döntheted el, mely csoportokat húzod le magadtól. Például, ha
		Törpék és lovagok rohamot indítottak az ellenség Goblinjai ellen, és 15-en meghaltak eközben,
		eldöntheted, hogy 15 Törpét veszíts, vagy 15 lovagot, esetleg 10 Törpét és 5 lovagot, vagy 10
		lovagot és 5 Törpét. Mindig jegyezd fel a változásokat a <em>Kalandlapodra</em>. A harcon kívül
		elvesztett katonákra is a fenti szabályok vonatkoznak.
	</p>

	<h3>Csata eredmények</h3>

	<table class="throwResultCasing">
		<tr>
			<td></td>
			<td colspan="2" class="righted">Helyzet</td>
			<td class="centerToggling"></td>
		</tr>
		<tr>
			<td class="throwResultCell"><div class="throwResult">A dobás eredménye</div></td>
			<td colspan="2">
				<table class="battleResults">
					<tr>
						<th></th>
						<th>Saját csapatok túlerőben</th>
						<th>Kiegyenlített erőviszonyok</th>
						<th>Ellenséges csapatok túlerőben</th>
					</tr>
					<tr>
						<td>1</td>
						<td>5S</td>
						<td>10S</td>
						<td>15S</td>
					</tr>
					<tr>
						<td>2</td>
						<td>5E</td>
						<td>5S</td>
						<td>10S</td>
					</tr>
					<tr>
						<td>3</td>
						<td>5E</td>
						<td>5S</td>
						<td>5S</td>
					</tr>
					<tr>
						<td>4</td>
						<td>5E</td>
						<td>5E</td>
						<td>5S</td>
					</tr>
					<tr>
						<td>5</td>
						<td>10E</td>
						<td>5E</td>
						<td>5E</td>
					</tr>
					<tr>
						<td>6</td>
						<td>15E</td>
						<td>10E</td>
						<td>5E</td>
					</tr>
				</table>
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td class="legend">S = saját csapatok</td>
			<td class="legend">E = ellenséges csapatok</td>
			<td></td>
		</tr>
	</table>


	<p>Kalandod során csatákra is sor fog kerülni. Ennek lebonyolítása az alábbiak szerint
		történik:</p>

	<ol>
		<li>Jegyezd fel, rajtad kívül hányan harcolnak a saját csapatodban.</li>
		<li>Jegyezd fel, hányan harcolnak az ellenséges csapatban.</li>
		<li>Hasonlítsd össze a két csapat méretét. Ha például 10 Törpe küzd 10 Goblinnal, az
			erőviszonyok kiegyenlítettek. Ha 10 Törpe 15 ellenséges Hobgoblinnal kerülne szembe, az ellenfél
			lenne túlerőben, míg ha a Törpék 15-en lennének, és a Hobgoblinok csak 10-en, úgy a saját
			csapatok lennének túlerőben.</li>
		<li>Dobj egy kockával, és nézd meg a <em>Csata eredmények</em> táblázatban a helyzetnek
			megfelelő oszlop dobásnak megfelelő rubrikáját!
		</li>
		<li>Vond le a saját vagy az ellenséges sereg létszámából az elesett katonákat.</li>
		<li>Ha egyik csapat sem pusztult el még teljesen, kezdd elölről a kört, az 1-es lépésnél
			(most már a csökkentett létszámok az aktuálisak).</li>
		<li>Ha a saját csapataidat teljesen elpusztították, te is elestél a harcban.</li>
	</ol>
</jsp:root>
