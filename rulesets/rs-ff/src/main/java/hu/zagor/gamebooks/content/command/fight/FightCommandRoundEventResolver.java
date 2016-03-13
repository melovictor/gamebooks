package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.content.command.fight.stat.StatisticsProvider;
import java.util.List;
import java.util.Map;

/**
 * Class for resolving round events after a round of battle has been fought.
 * @author Tamas_Szekeres
 */
public class FightCommandRoundEventResolver {

    private Map<FightRoundResult, StatisticsProvider> statProviders;

    /**
     * Resolves all round events based on the current battle statistics.
     * @param command the {@link FightCommand} being resolved
     * @param resolveList the list of {@link ParagraphData} that has to be resolved afterwards
     */
    public void resolveRoundEvent(final FightCommand command, final List<ParagraphData> resolveList) {
        for (final RoundEvent roundEvent : command.getRoundEvents()) {
            if (shouldActOnEvent(roundEvent, command)) {
                boolean ongoing = command.isOngoing();
                final FfParagraphData paragraphData = cloneData(roundEvent.getParagraphData());
                ongoing &= !paragraphData.isInterrupt();
                command.setOngoing(ongoing);
                resolveList.add(paragraphData);
                if (ongoing) {
                    final FightCommandMessageList messages = command.getMessages();
                    messages.switchToPostRoundMessages();
                    messages.add(paragraphData.getText());
                    paragraphData.setText("");
                    messages.switchToRoundMessages();
                }
            }
        }
    }

    private FfParagraphData cloneData(final FfParagraphData paragraphData) {
        FfParagraphData clone;
        try {
            clone = paragraphData.clone();
        } catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Cloning of the paragraph data has failed, this should not have happened!", e);
        }
        return clone;
    }

    private boolean shouldActOnEvent(final RoundEvent roundEvent, final FightCommand command) {
        final BattleStatistics battleStatistics = command.getBattleStatistics(roundEvent.getEnemyId());
        final StatisticsProvider statisticsProvider = statProviders.get(roundEvent.getRoundResult());
        if (statisticsProvider == null) {
            throw new IllegalStateException("Unknown event type '" + roundEvent.getRoundResult() + "'!");
        }
        final EventStatistics eventStats = statisticsProvider.provideStatistics(battleStatistics, roundEvent);
        return shouldActInAllRound(roundEvent, eventStats) || shouldActInThisRound(roundEvent, eventStats);
    }

    private boolean shouldActInThisRound(final RoundEvent roundEvent, final EventStatistics eventStats) {
        return eventStats.getTotal() == roundEvent.getTotalCount() || eventStats.getSubsequent() == roundEvent.getSubsequentCount();
    }

    private boolean shouldActInAllRound(final RoundEvent roundEvent, final EventStatistics eventStats) {
        return roundEvent.getTotalCount() == RoundEvent.ALL_ROUNDS && eventStats.getTotal() > 0;
    }

    public void setStatProviders(final Map<FightRoundResult, StatisticsProvider> statProviders) {
        this.statProviders = statProviders;
    }
}
