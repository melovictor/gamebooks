package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.AbstractCommandTest;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class FightCommandTest extends AbstractCommandTest {

    private FightCommand underTest;
    private IMocksControl mockControl;
    private FfParagraphData win;
    private FfParagraphData lose;
    private FfParagraphData flee;
    private FfParagraphData fleeCloned;
    private FfParagraphData loseCloned;
    private FfParagraphData winCloned;
    private FightFleeData fleeData;
    private FightFleeData fleeDataCloned;
    private RoundEvent roundEvent;
    private RoundEvent roundEventCloned;
    private BattleStatistics battleStatisticsCloned;
    private BattleStatistics battleStatistics;
    private FightRoundBoundingCommand afterBounding;
    private FightRoundBoundingCommand beforeBounding;
    private FightRoundBoundingCommand afterBoundingCloned;
    private FightRoundBoundingCommand beforeBoundingCloned;
    private Map<String, BattleStatistics> battleStatisticsMap;
    private FightOutcome outcome;
    private FfSpecialAttack specialAttack;
    private FfSpecialAttack specialAttackCloned;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        win = mockControl.createMock(FfParagraphData.class);
        lose = mockControl.createMock(FfParagraphData.class);
        flee = mockControl.createMock(FfParagraphData.class);
        fleeData = mockControl.createMock(FightFleeData.class);
        fleeDataCloned = mockControl.createMock(FightFleeData.class);
        fleeCloned = mockControl.createMock(FfParagraphData.class);
        winCloned = mockControl.createMock(FfParagraphData.class);
        loseCloned = mockControl.createMock(FfParagraphData.class);
        roundEvent = mockControl.createMock(RoundEvent.class);
        roundEventCloned = mockControl.createMock(RoundEvent.class);
        battleStatistics = mockControl.createMock(BattleStatistics.class);
        battleStatisticsCloned = mockControl.createMock(BattleStatistics.class);
        afterBounding = mockControl.createMock(FightRoundBoundingCommand.class);
        beforeBounding = mockControl.createMock(FightRoundBoundingCommand.class);
        afterBoundingCloned = mockControl.createMock(FightRoundBoundingCommand.class);
        beforeBoundingCloned = mockControl.createMock(FightRoundBoundingCommand.class);
        battleStatisticsMap = new HashMap<String, BattleStatistics>();
        battleStatisticsMap.put("29", battleStatistics);
        specialAttack = mockControl.createMock(FfSpecialAttack.class);
        specialAttackCloned = mockControl.createMock(FfSpecialAttack.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new FightCommand();
        underTest.setUsableWeaponTypes(Arrays.asList(new ItemType[]{ItemType.weapon1, ItemType.weapon2}));
        mockControl.reset();
    }

    public void testGetValidMoveShouldReturnFight() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getValidMove();
        // THEN
        Assert.assertEquals(returned, "fight");
    }

    public void testGetBattleStatisticsWhenIdIsRequestedTheFirstTimeShouldCreateAndReturnNewBattleStatistics() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final BattleStatistics returned = underTest.getBattleStatistics("9");
        // THEN
        Assert.assertNotNull(returned);
    }

    public void testGetBattleStatisticsWhenIdIsRequestedTheSecondTimeShouldReturnTheFirstBattleStatistics() {
        // GIVEN
        final BattleStatistics first = underTest.getBattleStatistics("9");
        mockControl.replay();
        // WHEN
        final BattleStatistics returned = underTest.getBattleStatistics("9");
        // THEN
        Assert.assertSame(returned, first);
    }

    public void testGetCommandViewWhenBattleIsStillOngoingShouldReturnViewWithTheChoicesHidden() {
        // GIVEN
        underTest.setBattleType("Single");
        underTest.setOngoing(true);
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("ff");
        // THEN
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(returned.getViewName(), "ffFightSingle");
        Assert.assertSame(model.get("fightCommand"), underTest);
        Assert.assertEquals(model.get("ffChoiceHidden"), true);
    }

    public void testGetCommandViewWhenBattleIsFinishedShouldReturnViewWithoutTheChoicesHidden() {
        // GIVEN
        underTest.setOngoing(false);
        underTest.setBattleType("Single");
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("ff");
        // THEN
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(returned.getViewName(), "ffFightSingle");
        Assert.assertSame(model.get("fightCommand"), underTest);
        Assert.assertFalse(model.containsKey("ffChoiceHidden"));
    }

    public void testCloneWhenAllComplexFieldsAreSetShouldReturnClone() throws CloneNotSupportedException {
        // GIVEN
        underTest.setAfterBounding(afterBounding);
        underTest.setBeforeBounding(beforeBounding);
        outcome = new FightOutcome();
        outcome.setParagraphData(win);
        underTest.getWin().add(outcome);
        underTest.setFlee(flee);
        underTest.setFleeData(fleeData);
        underTest.setLose(lose);
        underTest.getRoundEvents().add(roundEvent);
        underTest.getSpecialAttacks().add(specialAttack);
        Whitebox.setInternalState(underTest, "battleStatistics", battleStatisticsMap);

        expectTc(win, winCloned);
        expectTc(flee, fleeCloned);
        expectTc(fleeData, fleeDataCloned);
        expectTc(lose, loseCloned);
        expectTc(roundEvent, roundEventCloned);
        expectTc(battleStatistics, battleStatisticsCloned);
        expectTc(afterBounding, afterBoundingCloned);
        expectTc(beforeBounding, beforeBoundingCloned);
        expectTc(specialAttack, specialAttackCloned);
        mockControl.replay();
        // WHEN
        final FightCommand returned = underTest.clone();
        // THEN
        Assert.assertSame(returned.getWin().get(0).getParagraphData(), winCloned);
        Assert.assertSame(returned.getLose(), loseCloned);
        Assert.assertSame(returned.getRoundEvents().get(0), roundEventCloned);
        Assert.assertSame(returned.getAfterBounding(), afterBoundingCloned);
        Assert.assertSame(returned.getBeforeBounding(), beforeBoundingCloned);
        Assert.assertSame(returned.getBattleStatistics("29"), battleStatisticsCloned);
        Assert.assertSame(returned.getSpecialAttacks().get(0), specialAttackCloned);
        Assert.assertNotSame(returned, underTest);
    }

    public void testCloneWhenNoneOfTheComplexFieldsAreSetShouldReturnClone() throws CloneNotSupportedException {
        // GIVEN
        mockControl.replay();
        // WHEN
        final FightCommand returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
