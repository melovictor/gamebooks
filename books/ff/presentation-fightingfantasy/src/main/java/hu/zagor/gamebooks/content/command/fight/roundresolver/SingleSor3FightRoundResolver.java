package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link FightRoundResult} interface for resolving single fight rounds in SOR3.
 * @author Tamas_Szekeres
 */
@Component("singlesor3FightRoundResolver")
public class SingleSor3FightRoundResolver implements FightRoundResolver {

    @Autowired @Qualifier("singleFightRoundResolver") private FightRoundResolver superResolver;
    @Autowired private MessageSource source;
    @Autowired private LocaleProvider provider;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FightRoundResult[] resolveRound = superResolver.resolveRound(command, resolvationData, beforeRoundResult);

        if (centaurReadyToSurrender(command, resolvationData, resolveRound)) {
            command.setFleeAllowed(true);
            final FightFleeData fleeData = new FightFleeData();
            fleeData.setAfterRound(1);
            fleeData.setSufferDamage(false);
            fleeData.setText(source.getMessage("page.sor3.fight.centaur.flee", null, provider.getLocale()));
            command.setFleeData(fleeData);
        }

        return resolveRound;
    }

    private boolean centaurReadyToSurrender(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] resolveRound) {
        boolean willigToSurrender = false;
        final List<String> enemies = command.getEnemies();
        if (enemies.contains("5")) {
            int dead = enemies.size();
            Enemy alive = null;
            if (!isDead(resolvationData.getEnemies().get("5"))) {
                dead--;
                alive = resolvationData.getEnemies().get("5");
            }
            if (!isDead(resolvationData.getEnemies().get("6"))) {
                dead--;
                alive = resolvationData.getEnemies().get("6");
            }
            if (!isDead(resolvationData.getEnemies().get("7"))) {
                dead--;
                alive = resolvationData.getEnemies().get("7");
            }
            if (dead == 2 && alive != null) {
                final String enemyId = alive.getId();
                final BattleStatistics battleStatistics = command.getBattleStatistics(enemyId);
                willigToSurrender = (battleStatistics.getTotalWin() + (resolveRound[0] == FightRoundResult.WIN ? 1 : 0)) > 1;
            }
        }
        return willigToSurrender;
    }

    private boolean isDead(final Enemy enemy) {
        final FfEnemy ffEnemy = (FfEnemy) enemy;
        return ffEnemy.getStamina() < 1;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        superResolver.resolveFlee(command, resolvationData);
    }

}
