package hu.zagor.gamebooks.content.command.fight.enemyroundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;

/**
 * Basic extension of the {@link MapBasedFfCustomEnemyHandlingSingleFightRoundResolver}, with the two resolve methods implemented to do the least expected behaviour.
 * @author Tamas_Szekeres
 * @param <T> the type which will contain the data that was compiled during the pre-fight phase
 */
public abstract class BasicAbstractCustomEnemyHandlingFightRoundResolver<T extends BasicEnemyPrePostFightDataContainer>
    extends MapBasedFfCustomEnemyHandlingSingleFightRoundResolver<T> {

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final T data = executePreRoundActions(command, resolvationData);
        final FightRoundResult[] roundResult = getDecorated().resolveRound(command, resolvationData, beforeRoundResult);
        executePostRoundActions(command, resolvationData, roundResult, data);

        return roundResult;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        getDecorated().resolveFlee(command, resolvationData);
    }

    /**
     * Returns the {@link FightRoundResolver} object to be decorated.
     * @return the object to be decorated
     */
    protected FightRoundResolver getDecorated() {
        final Field field = ReflectionUtils.findField(getClass(), "decorated");
        ReflectionUtils.makeAccessible(field);
        return (FightRoundResolver) ReflectionUtils.getField(field, this);
    }

}
