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
                ongoing &= !roundEvent.getParagraphData().isInterrupt();
                command.setOngoing(ongoing);
                resolveList.add(roundEvent.getParagraphData());
                if (ongoing) {
                    final FightCommandMessageList messages = command.getMessages();
                    messages.switchToPostRoundMessages();
                    final FfParagraphData paragraphData = roundEvent.getParagraphData();
                    messages.add(paragraphData.getText());
                    paragraphData.setText("");
                    messages.switchToRoundMessages();
                }
            }
        }
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
