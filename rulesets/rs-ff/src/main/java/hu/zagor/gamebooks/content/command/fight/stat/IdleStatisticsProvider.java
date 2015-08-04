package hu.zagor.gamebooks.content.command.fight.stat;

import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

import org.springframework.stereotype.Component;

/**
 * Provides statistics that will fit on none of the battle rounds.
 * @author Tamas_Szekeres
 */
@Component("idleStatisticsProvider")
public class IdleStatisticsProvider implements StatisticsProvider {

    @Override
    public EventStatistics provideStatistics(final BattleStatistics battleStatistics, final RoundEvent roundEvent) {
        return new EventStatistics(-1, -1);
    }
}
