package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.MapBasedFfCustomEnemyHandlingSingleFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF60.
 * @author Tamas_Szekeres
 */
@Component("singleff60FightRoundResolver")
public class SingleFf60FightRoundResolver extends MapBasedFfCustomEnemyHandlingSingleFightRoundResolver<EnemyPrePostFightDataContainer> implements FightRoundResolver {
    @Autowired @Qualifier("singleFightRoundResolver") private FightRoundResolver decorated;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final EnemyPrePostFightDataContainer data = executePreRoundActions(command, resolvationData);
        final FightRoundResult[] results = decorated.resolveRound(command, resolvationData, beforeRoundResult);
        executePostRoundActions(command, resolvationData, results, data);
        return results;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        decorated.resolveFlee(command, resolvationData);
    }

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer>> getType() {
        return Ff60BeforeAfterRoundEnemyHandler.class;
    }

}
