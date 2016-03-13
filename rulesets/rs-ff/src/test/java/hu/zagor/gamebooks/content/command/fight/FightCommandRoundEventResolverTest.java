package hu.zagor.gamebooks.content.command.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.content.command.fight.stat.StatisticsProvider;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightCommandRoundEventResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class FightCommandRoundEventResolverTest {

    private static final String ENEMY_ID = "9";
    private static final String TEXT = "This is the text in the round result.";
    @UnderTest private FightCommandRoundEventResolver underTest;
    @MockControl private IMocksControl mockControl;
    @Instance private List<ParagraphData> resolveList;
    private FightCommand command;
    @Mock private BattleStatistics stats;
    @Instance private Map<String, BattleStatistics> battleStatistics;
    @Mock private StatisticsProvider statProvider;
    @Mock private EventStatistics eventStatistics;
    @Mock private FfParagraphData data;
    @Mock private FfParagraphData clonedData;
    private RoundEvent roundEvent;
    @Instance(inject = true) private Map<FightRoundResult, StatisticsProvider> statProviders;
    @Mock private FightCommandMessageList messages;

    @BeforeClass
    public void setUpClass() {
        battleStatistics.put(ENEMY_ID, stats);
        statProviders.put(FightRoundResult.WIN, statProvider);
    }

    @BeforeMethod
    public void setUpMethod() {
        resolveList.clear();
        command = new FightCommand();
        command.setOngoing(true);
        Whitebox.setInternalState(command, "battleStatistics", battleStatistics);
        Whitebox.setInternalState(command, "messages", messages);
        roundEvent = new RoundEvent();
        roundEvent.setEnemyId(ENEMY_ID);
        roundEvent.setParagraphData(data);
        roundEvent.setRoundResult(FightRoundResult.WIN);
    }

    public void testResolveRoundEventWhenRoundEventsAreEmptyShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertTrue(resolveList.isEmpty());
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichNoActingIsRequiredShouldDoNothing() {
        // GIVEN
        roundEvent.setTotalCount(5);
        roundEvent.setSubsequentCount(-1);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(eventStatistics.getSubsequent()).andReturn(2);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertTrue(resolveList.isEmpty());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveRoundEventWhenWrongRoundEventAvailableShouldThrowException() {
        // GIVEN
        roundEvent.setRoundResult(FightRoundResult.LOSE);
        roundEvent.setTotalCount(5);
        roundEvent.setSubsequentCount(-1);
        command.getRoundEvents().add(roundEvent);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN throws exception
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichActingIsRequiredForTotalShouldAddRoundEventDataToResolveList()
        throws CloneNotSupportedException {
        // GIVEN
        roundEvent.setTotalCount(3);
        roundEvent.setSubsequentCount(-1);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(data.clone()).andReturn(clonedData);
        expect(clonedData.isInterrupt()).andReturn(false);
        messages.switchToPostRoundMessages();
        expect(clonedData.getText()).andReturn(TEXT);
        expect(messages.add(TEXT)).andReturn(true);
        clonedData.setText("");
        messages.switchToRoundMessages();
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), clonedData);
        Assert.assertTrue(command.isOngoing());
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichActingIsRequiredForSubsequentShouldAddRoundEventDataToResolveList()
        throws CloneNotSupportedException {
        // GIVEN
        roundEvent.setTotalCount(-1);
        roundEvent.setSubsequentCount(2);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(eventStatistics.getSubsequent()).andReturn(2);
        expect(data.clone()).andReturn(clonedData);
        expect(clonedData.isInterrupt()).andReturn(false);
        messages.switchToPostRoundMessages();
        expect(clonedData.getText()).andReturn(TEXT);
        expect(messages.add(TEXT)).andReturn(true);
        clonedData.setText("");
        messages.switchToRoundMessages();
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), clonedData);
        Assert.assertTrue(command.isOngoing());
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichActingIsRequiredForSubsequentAndInterruptsBattleShouldAddRoundEventDataToResolveList()
        throws CloneNotSupportedException {
        // GIVEN
        roundEvent.setTotalCount(-1);
        roundEvent.setSubsequentCount(2);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(eventStatistics.getSubsequent()).andReturn(2);
        expect(data.clone()).andReturn(clonedData);
        expect(clonedData.isInterrupt()).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), clonedData);
        Assert.assertFalse(command.isOngoing());
    }

}
