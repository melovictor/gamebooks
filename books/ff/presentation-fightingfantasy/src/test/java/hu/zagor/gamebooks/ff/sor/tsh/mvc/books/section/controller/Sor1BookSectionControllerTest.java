package hu.zagor.gamebooks.ff.sor.tsh.mvc.books.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import java.util.Set;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Sor1BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Sor1BookSectionControllerTest {
    private static final String WEAPON_ID = "1001";
    private static final String RAGNARS_BRACELET = "3020";
    private static final String NEUTRAL_ENEMY_ID = "1";
    private static final String ASSASSIN_ID = "17";
    private static final String TROLL_ID = "16";
    private Sor1BookSectionController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private SectionHandlingService sectionHandler;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private SorCharacter character;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfItem bracelet;
    @Mock private EquipInfo equipInfo;
    @Mock private FfItem weapon;
    @Inject private Set<String> swordItemIds;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private Map<String, Enemy> enemies;
    @Mock private FfEnemy enemy;
    @Mock private Paragraph paragraph;
    @Mock private ParagraphData data;
    @Mock private CommandList commandList;
    @Mock private FightCommand fightCommand;
    @Mock private FightRoundBoundingCommand bounding;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(3L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        Whitebox.setInternalState(underTest, "info", info);
    }

    @UnderTest
    public Sor1BookSectionController underTest() {
        return new Sor1BookSectionController(sectionHandler);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenSectionHandlerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Sor1BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testHandleBeforeFightWhenWeDoNotHaveTheBraceletShouldDoNothing() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, RAGNARS_BRACELET)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.handleBeforeFight(wrapper, NEUTRAL_ENEMY_ID);
        // THEN
    }

    public void testHandleBeforeFightWhenWeHaveTheBraceletButItIsNotEquippedShouldDoNothing() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, RAGNARS_BRACELET)).andReturn(bracelet);
        expect(bracelet.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isEquipped()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.handleBeforeFight(wrapper, NEUTRAL_ENEMY_ID);
        // THEN
    }

    public void testHandleBeforeFightWhenWeHaveTheBraceletAndEquippedWeaponIsNotSwordShouldSetBraceletAttackStrengthToZero() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, RAGNARS_BRACELET)).andReturn(bracelet);
        expect(bracelet.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isEquipped()).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn(WEAPON_ID);
        expect(swordItemIds.contains(WEAPON_ID)).andReturn(false);
        bracelet.setAttackStrength(0);
        mockControl.replay();
        // WHEN
        underTest.handleBeforeFight(wrapper, NEUTRAL_ENEMY_ID);
        // THEN
    }

    public void testHandleBeforeFightWhenWeHaveTheBraceletAndEquippedWeaponIsSwordShouldSetBraceletAttackStrengthToTwo() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, RAGNARS_BRACELET)).andReturn(bracelet);
        expect(bracelet.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isEquipped()).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn(WEAPON_ID);
        expect(swordItemIds.contains(WEAPON_ID)).andReturn(true);
        bracelet.setAttackStrength(2);
        mockControl.replay();
        // WHEN
        underTest.handleBeforeFight(wrapper, NEUTRAL_ENEMY_ID);
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsNeitherTrollNorAssassinShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, NEUTRAL_ENEMY_ID);
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsAssassinAndWeHaveLotsOfStaminaShouldDoNothing() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(15);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, ASSASSIN_ID);
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsAssassinAndWeHaveFewOfStaminaShouldSetAssassinNotToFlee() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(5);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(enemies.get(ASSASSIN_ID)).andReturn(enemy);
        enemy.setFleeAtStamina(0);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, ASSASSIN_ID);
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsTrollAndFightIsOverShouldDoNothing() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, TROLL_ID);
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsTrollAndBeforeRoundFiveShouldDoNothing() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(false);
        expect(commandList.get(0)).andReturn(fightCommand);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(enemies.get(TROLL_ID)).andReturn(enemy);
        expect(enemy.getSkill()).andReturn(4);
        expect(fightCommand.getRoundNumber()).andReturn(4);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, TROLL_ID);
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsTrollAndAtRoundFiveTrollHasntPickedUpWeaponShouldAttemptPickupEveryRound() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(false);
        expect(commandList.get(0)).andReturn(fightCommand);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(enemies.get(TROLL_ID)).andReturn(enemy);
        expect(enemy.getSkill()).andReturn(4);
        expect(fightCommand.getRoundNumber()).andReturn(5);
        expect(fightCommand.getBeforeBounding()).andReturn(bounding);
        bounding.setNth(1);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, TROLL_ID);
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsTrollAndPickedUpWeaponShouldStopPickipAttempt() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commandList);
        expect(commandList.isEmpty()).andReturn(false);
        expect(commandList.get(0)).andReturn(fightCommand);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(enemies.get(TROLL_ID)).andReturn(enemy);
        expect(enemy.getSkill()).andReturn(8);
        fightCommand.setBeforeBounding(null);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, TROLL_ID);
        // THEN
    }

}
