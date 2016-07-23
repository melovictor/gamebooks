package hu.zagor.gamebooks.content.command.fight.stat;

import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import org.springframework.stereotype.Component;

/**
 * Provides lose statistics.
 * @author Tamas_Szekeres
 */
@Component("loseStatisticsProvider")
public class LoseStatisticsProvider implements StatisticsProvider {

    @Override
    public EventStatistics provideStatistics(final BattleStatistics battleStatistics, final RoundEvent roundEvent) {
        final int subsequentLose = battleStatistics.getSubsequentLose();
        return new EventStatistics(subsequentLose > 0 ? battleStatistics.getTotalLose() : RoundEvent.TOTAL_NOT_MEANINGFUL, subsequentLose);
    }
}
