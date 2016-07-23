package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.content.command.fight.roundresolver.service.SorDamageReducingArmourService;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleSor4FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleSor4FightRoundResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private SingleSor4FightRoundResolver underTest;
    @Mock private FfFightCommand command;
    private FightRoundResult[] result;
    private int enemyIdx;
    private FightDataDto dto;
    @Mock private FfEnemy enemy;
    @Mock private FightCommandMessageList message;
    @Mock private FfItem weapon;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private FfCharacter character;
    @Instance private FfCharacterHandler characterHandler;
    @Instance private ResolvationData resolvationData;
    @Mock private FfCharacterItemHandler itemHandler;
    @Inject private SorDamageReducingArmourService damageReducingArmourService;

    @BeforeClass
    public void setUpClass() {
        resolvationData.setCharacter(character);
        final BookInformations info = new BookInformations(3);
        info.setCharacterHandler(characterHandler);
        resolvationData.setInfo(info);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setItemHandler(itemHandler);

        dto = new FightDataDto(enemy, message, resolvationData, null);

        enemyIdx = 0;
        result = new FightRoundResult[1];
    }

    public void testDoWinFightWhenGenericEnemyShouldExecuteDefault() {
        // GIVEN
        expectDefaultWinFight();
        expect(enemy.getId()).andReturn("9");
        mockControl.replay();
        // WHEN
        underTest.doWinFight(command, result, enemyIdx, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.WIN);
    }

    public void testDoWinFightWhenSkunkEnemyShouldExecuteDefaultAndAllowFleeing() {
        // GIVEN
        expectDefaultWinFight();
        expect(enemy.getId()).andReturn("1");
        command.setFleeAllowed(true);
        final Capture<FightFleeData> fleeData = newCapture();
        command.setFleeData(capture(fleeData));
        mockControl.replay();
        // WHEN
        underTest.doWinFight(command, result, enemyIdx, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.WIN);
        Assert.assertTrue(fleeData.getValue().isSufferDamage());
    }

    public void testDoLoseFightWhenGenericEnemyShouldExecuteDefault() {
        // GIVEN
        expectDefaultLoseFight();
        expect(enemy.getId()).andReturn("9");
        mockControl.replay();
        // WHEN
        underTest.doLoseFight(command, result, enemyIdx, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.LOSE);
    }

    public void testDoLoseFightWhenSkunkEnemyShouldExecuteDefaultAndDisallowFleeing() {
        // GIVEN
        expectDefaultLoseFight();
        expect(enemy.getId()).andReturn("1");
        command.setFleeAllowed(false);
        mockControl.replay();
        // WHEN
        underTest.doLoseFight(command, result, enemyIdx, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.LOSE);
    }

    public void testDoTieWhenGenericEnemyShouldExecuteDefault() {
        // GIVEN
        expectDefaultTieFight();
        expect(enemy.getId()).andReturn("9");
        mockControl.replay();
        // WHEN
        underTest.doTieFight(command, result, enemyIdx, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.TIE);
    }

    public void testDoTieWhenSkunkEnemyShouldExecuteDefaultAndDisallowFleeing() {
        // GIVEN
        expectDefaultTieFight();
        expect(enemy.getId()).andReturn("1");
        command.setFleeAllowed(false);
        mockControl.replay();
        // WHEN
        underTest.doTieFight(command, result, enemyIdx, dto);
        // THEN
        Assert.assertEquals(result[0], FightRoundResult.TIE);
    }

    private void expectDefaultTieFight() {
        expect(enemy.getName()).andReturn("Orc");
        expect(message.addKey("page.ff.label.fight.single.tied", "Orc")).andReturn(true);
    }

    private void expectDefaultLoseFight() {
        expect(character.getStoneSkin()).andReturn(0);
        damageReducingArmourService.setUpDamageProtection(dto);
        expect(enemy.getStaminaDamage()).andReturn(2);
        expect(attributeHandler.resolveValue(character, "damageProtection")).andReturn(0);
        character.changeStamina(-2);
        expect(enemy.getSkillDamage()).andReturn(0);
        character.changeSkill(0);
        expect(enemy.getName()).andReturn("Orc");
        expect(message.addKey("page.ff.label.fight.single.failedDefense", "Orc")).andReturn(true);
        expect(command.isLuckOnDefense()).andReturn(false);
    }

    private void expectDefaultWinFight() {
        expect(enemy.isKillableByNormal()).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expect(enemy.getDamageAbsorption()).andReturn(0);
        expect(weapon.getSubType()).andReturn(WeaponSubType.blunt);
        expect(enemy.getStamina()).andReturn(9);
        enemy.setStamina(7);
        expect(enemy.getSkill()).andReturn(7);
        expect(weapon.getSkillDamage()).andReturn(0);
        enemy.setSkill(7);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getName()).andReturn("Orc");
        expect(message.addKey("page.ff.label.fight.single.successfulAttack", "Orc")).andReturn(true);
        expect(command.isLuckOnHit()).andReturn(false);
    }

}
