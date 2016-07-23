package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.SorDamageReducingArmourService;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Set;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleSor2FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleSor2FightRoundResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private SingleSor2FightRoundResolver underTest;
    @Mock private FfFightCommand command;
    private FightDataDto dto;
    @Mock private SorCharacter character;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfEnemy enemy;
    @Mock private FightCommandMessageList messages;
    @Mock private FfItem weapon;
    @Mock private FfAttributeHandler attributeHandler;
    @Instance private Set<String> weedSmokers;
    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Inject private SorDamageReducingArmourService damageReducingArmourService;
    private FightRoundResult[] result;
    @Instance private ResolvationData resolvationData;
    private BookInformations info;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        weedSmokers.add("2");
        weedSmokers.add("3");
        weedSmokers.add("4");
        underTest.setWeedSmokers(weedSmokers);
        result = new FightRoundResult[1];
        resolvationData.setCharacter(character);
        info = new BookInformations(3L);
        info.setCharacterHandler(characterHandler);
        resolvationData.setInfo(info);

        dto = new FightDataDto(enemy, messages, resolvationData, null);
    }

    public void testDamageEnemyWhenWeDidNotSmokeWeedShouldDamageAsUsual() {
        // GIVEN
        expect(itemHandler.hasItem(character, "4017")).andReturn(false);
        expectNormalDamageEnemy();
        mockControl.replay();
        // WHEN
        underTest.damageEnemy(command, dto);
        // THEN
    }

    @Test(dataProvider = "hitMiss246")
    public void testDamageEnemyWhenWeDidSmokeWeedAndRolledEvenShouldDamageAsUsual(final int roll) {
        // GIVEN
        expect(itemHandler.hasItem(character, "4017")).andReturn(true);
        final int[] rollResult = new int[]{roll, roll};
        expect(generator.getRandomNumber(1)).andReturn(rollResult);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rollResult)).andReturn("[" + roll + "]");
        expect(messages.addKey("page.ff.label.random.after", "[" + roll + "]", roll)).andReturn(true);
        expectNormalDamageEnemy();
        mockControl.replay();
        // WHEN
        underTest.damageEnemy(command, dto);
        // THEN
    }

    public void testDamageEnemyWhenWeDidSmokeWeedAndRolled1ShouldDamageSelf() {
        // GIVEN
        expect(itemHandler.hasItem(character, "4017")).andReturn(true);
        final int[] rollResult = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(rollResult);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rollResult)).andReturn("[1]");
        expect(messages.addKey("page.ff.label.random.after", "[1]", 1)).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        character.changeStamina(-2);
        expect(messages.addKey("page.sor2.weeders.selfHitSelf", 2)).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.damageEnemy(command, dto);
        // THEN
    }

    @Test(dataProvider = "hitMiss35")
    public void testDamageEnemyWhenWeDidSmokeWeedAndRolled3Or5ShouldDamageNothing(final int roll) {
        // GIVEN
        expect(itemHandler.hasItem(character, "4017")).andReturn(true);
        final int[] rollResult = new int[]{roll, roll};
        expect(generator.getRandomNumber(1)).andReturn(rollResult);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rollResult)).andReturn("[" + roll + "]");
        expect(messages.addKey("page.ff.label.random.after", "[" + roll + "]", roll)).andReturn(true);
        expect(enemy.getName()).andReturn("Orc");
        expect(messages.addKey("page.sor2.weeders.selfMissedHit", "Orc")).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.damageEnemy(command, dto);
        // THEN
    }

    public void testDamageSelfWhenEnemyDidNotSmokeWeedShouldDamageAsUsual() {
        // GIVEN
        expect(enemy.getId()).andReturn("1");
        expectNormalDamageSelf();
        mockControl.replay();
        // WHEN
        underTest.damageSelf(dto);
        // THEN
    }

    @Test(dataProvider = "hitMiss246")
    public void testDamageSelfWhenEnemyDidSmokeWeedAndRollsEvenShouldDamageAsUsual(final int roll) {
        // GIVEN
        expect(enemy.getId()).andReturn("2");
        final int[] rollResult = new int[]{roll, roll};
        expect(generator.getRandomNumber(1)).andReturn(rollResult);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rollResult)).andReturn("[" + roll + "]");
        expect(messages.addKey("page.ff.label.random.after", "[" + roll + "]", roll)).andReturn(true);
        expectNormalDamageSelf();
        mockControl.replay();
        // WHEN
        underTest.damageSelf(dto);
        // THEN
    }

    @Test(dataProvider = "hitMiss35")
    public void testDamageSelfWhenEnemyDidSmokeWeedAndRolls3Or5ShouldDamageNothing(final int roll) {
        // GIVEN
        expect(enemy.getId()).andReturn("2");
        final int[] rollResult = new int[]{roll, roll};
        expect(generator.getRandomNumber(1)).andReturn(rollResult);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rollResult)).andReturn("[" + roll + "]");
        expect(messages.addKey("page.ff.label.random.after", "[" + roll + "]", roll)).andReturn(true);
        expect(enemy.getName()).andReturn("Orc");
        expect(messages.addKey("page.sor2.weeders.enemyMissedHit", "Orc")).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.damageSelf(dto);
        // THEN
    }

    public void testDamageSelfWhenEnemyDidSmokeWeedAndRolls1ShouldDamageSelf() {
        // GIVEN
        expect(enemy.getId()).andReturn("2");
        final int[] rollResult = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(rollResult);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rollResult)).andReturn("[1]");
        expect(messages.addKey("page.ff.label.random.after", "[1]", 1)).andReturn(true);
        expect(enemy.getStamina()).andReturn(9);
        enemy.setStamina(7);
        expect(enemy.getName()).andReturn("Orc");
        expect(messages.addKey("page.sor2.weeders.enemyHitSelf", "Orc")).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.damageSelf(dto);
        // THEN
    }

    public void testDoTieFightWhenNormalEnemyShouldExecuteDefault() {
        // GIVEN
        expect(enemy.getId()).andReturn("1");
        expect(enemy.getName()).andReturn("Orc");
        expect(messages.addKey("page.ff.label.fight.single.tied", "Orc")).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.doTieFight(command, result, 0, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.TIE);
    }

    public void testDoTieFightWhenChainMakerEnemyAndWeAreStrongTieShouldExecuteAsTie() {
        // GIVEN
        expect(enemy.getId()).andReturn("19");
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(6);
        expect(enemy.getName()).andReturn("Chainmaker");
        expect(messages.addKey("page.ff.label.fight.single.tied", "Chainmaker")).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.doTieFight(command, result, 0, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.TIE);
    }

    public void testDoTieFightWhenChainMakerEnemyAndWeAreOnLimitTieShouldExecuteAsLose() {
        // GIVEN
        expect(enemy.getId()).andReturn("19");
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(5);
        expect(character.getStoneSkin()).andReturn(0);
        expect(enemy.getId()).andReturn("19");
        expectNormalDamageSelf();
        expect(command.isLuckOnDefense()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.doTieFight(command, result, 0, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.LOSE);
    }

    public void testDoTieFightWhenChainMakerEnemyAndWeAreUnderLimitTieShouldExecuteAsLose() {
        // GIVEN
        expect(enemy.getId()).andReturn("19");
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(2);
        expect(character.getStoneSkin()).andReturn(0);
        expect(enemy.getId()).andReturn("19");
        expectNormalDamageSelf();
        expect(command.isLuckOnDefense()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.doTieFight(command, result, 0, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.LOSE);
    }

    @DataProvider(name = "hitMiss35")
    public Object[][] hitMiss35() {
        return new Object[][]{{3}, {5}};
    }

    @DataProvider(name = "hitMiss246")
    public Object[][] hitMiss246() {
        return new Object[][]{{2}, {4}, {6}};
    }

    private void expectNormalDamageSelf() {
        damageReducingArmourService.setUpDamageProtection(dto);
        expect(enemy.getStaminaDamage()).andReturn(2);
        expect(attributeHandler.resolveValue(character, "damageProtection")).andReturn(0);
        character.changeStamina(-2);
        expect(enemy.getSkillDamage()).andReturn(0);
        character.changeSkill(0);
        expect(enemy.getName()).andReturn("Orc");
        expect(messages.addKey("page.ff.label.fight.single.failedDefense", "Orc")).andReturn(true);
    }

    private void expectNormalDamageEnemy() {
        expect(enemy.isKillableByNormal()).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expect(enemy.getDamageAbsorption()).andReturn(0);
        expect(weapon.getSubType()).andReturn(WeaponSubType.edged);
        expect(enemy.getDamageAbsorptionEdged()).andReturn(0);
        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);
        expect(enemy.getSkill()).andReturn(11);
        expect(weapon.getSkillDamage()).andReturn(0);
        enemy.setSkill(11);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getName()).andReturn("Orc");
        expect(messages.addKey("page.ff.label.fight.single.successfulAttack", "Orc")).andReturn(true);
        expect(command.isLuckOnHit()).andReturn(false);
    }

}
