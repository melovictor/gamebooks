package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

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
 * All at once fight round resolver for FF11.
 * @author Tamas_Szekeres
 */
@Component("allAtOnceff11FightRoundResolver")
public class AllAtOnceFf11FightRoundResolver extends MapBasedFfCustomEnemyHandlingSingleFightRoundResolver<EnemyPrePostFightDataContainer> implements FightRoundResolver {
    @Autowired @Qualifier("allAtOnceFightRoundResolver") private FightRoundResolver decorated;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final EnemyPrePostFightDataContainer data = executePreRoundActions(command, resolvationData);
        final FightRoundResult[] roundResult = decorated.resolveRound(command, resolvationData, beforeRoundResult);
        executePostRoundActions(command, resolvationData, roundResult, data);

        return roundResult;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        decorated.resolveFlee(command, resolvationData);
    }

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer>> getType() {
        return Ff11BeforeAfterRoundEnemyHandler.class;
    }

    @Override
    protected EnemyPrePostFightDataContainer getDataBean() {
        return new EnemyPrePostFightDataContainer();
    }

}
