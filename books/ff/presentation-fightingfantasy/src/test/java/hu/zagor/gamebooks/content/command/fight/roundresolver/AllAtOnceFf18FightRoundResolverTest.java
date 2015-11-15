package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AllAtOnceFf18FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class AllAtOnceFf18FightRoundResolverTest {

    private AllAtOnceFf18FightRoundResolver underTest;
    private IMocksControl mockControl;
    private FightCommand command;
    private FightDataDto dto;
    private FfEnemy enemy;
    private FightCommandMessageList messages;
    private List<ItemType> usableWeaponTypes;
    private ResolvationData resolvationData;
    private ParagraphData rootData;
    private FfCharacter character;
    private Map<String, Enemy> enemies;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    private FfItem weapon;
    private FightRoundBoundingCommand afterBounding;
    private List<Command> commandsList;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        setUpClassUnchecked();
        underTest = new AllAtOnceFf18FightRoundResolver();
        command = mockControl.createMock(FightCommand.class);
        enemy = mockControl.createMock(FfEnemy.class);
        messages = mockControl.createMock(FightCommandMessageList.class);
        character = mockControl.createMock(FfCharacter.class);
        info = new FfBookInformations(1L);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).withEnemies(enemies).build();
        dto = new FightDataDto(enemy, messages, resolvationData, usableWeaponTypes);
        weapon = mockControl.createMock(FfItem.class);
        afterBounding = mockControl.createMock(FightRoundBoundingCommand.class);
    }

    @SuppressWarnings("unchecked")
    private void setUpClassUnchecked() {
        commandsList = mockControl.createMock(List.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDamageEnemyIfNotFightingAgainstNorthernArcadianShouldDamageNormally() {
        // GIVEN
        expect(enemy.isKillableByNormal()).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expect(enemy.getDamageAbsorption()).andReturn(0);
        expect(weapon.getSubType()).andReturn(WeaponSubType.edged);
        expect(enemy.getDamageAbsorptionEdged()).andReturn(0);
        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);
        expect(enemy.getSkill()).andReturn(5);
        expect(weapon.getSkillDamage()).andReturn(0);
        enemy.setSkill(5);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getName()).andReturn("Arcadian");
        expect(messages.addKey("page.ff.label.fight.single.successfulAttack", "Arcadian")).andReturn(true);
        expect(command.isLuckOnHit()).andReturn(false);
        expect(enemy.getId()).andReturn("35");
        mockControl.replay();
        // WHEN
        underTest.damageEnemy(command, dto);
        // THEN
    }

    public void testDamageEnemyIfFightingAgainstNorthernArcadianAndItIsStillAliveShouldDamageNormallyAndContinueBattle() {
        // GIVEN
        expect(enemy.isKillableByNormal()).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expect(enemy.getDamageAbsorption()).andReturn(0);
        expect(weapon.getSubType()).andReturn(WeaponSubType.edged);
        expect(enemy.getDamageAbsorptionEdged()).andReturn(0);
        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);
        expect(enemy.getSkill()).andReturn(5);
        expect(weapon.getSkillDamage()).andReturn(0);
        enemy.setSkill(5);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getName()).andReturn("Arcadian");
        expect(messages.addKey("page.ff.label.fight.single.successfulAttack", "Arcadian")).andReturn(true);
        expect(command.isLuckOnHit()).andReturn(false);
        expect(enemy.getId()).andReturn("50");
        expect(enemy.getStamina()).andReturn(7);
        mockControl.replay();
        // WHEN
        underTest.damageEnemy(command, dto);
        // THEN
    }

    public void testDamageEnemyIfFightingAgainstNorthernArcadianAndItIsDeadShouldDamageNormallyButRemoveAfterBoundingRandomCommand() {
        // GIVEN
        expect(enemy.isKillableByNormal()).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expect(enemy.getDamageAbsorption()).andReturn(0);
        expect(weapon.getSubType()).andReturn(WeaponSubType.edged);
        expect(enemy.getDamageAbsorptionEdged()).andReturn(0);
        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);
        expect(enemy.getSkill()).andReturn(5);
        expect(weapon.getSkillDamage()).andReturn(0);
        enemy.setSkill(5);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getStaminaDamageWhenHit()).andReturn(0);
        expect(enemy.getName()).andReturn("Arcadian");
        expect(messages.addKey("page.ff.label.fight.single.successfulAttack", "Arcadian")).andReturn(true);
        expect(command.isLuckOnHit()).andReturn(false);
        expect(enemy.getId()).andReturn("50");
        expect(enemy.getStamina()).andReturn(-1);
        expect(command.getAfterBounding()).andReturn(afterBounding);
        expect(afterBounding.getCommands()).andReturn(commandsList);
        commandsList.clear();
        mockControl.replay();
        // WHEN
        underTest.damageEnemy(command, dto);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
