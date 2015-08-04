package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightBoundingCommandResolver;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link FightCommandBeforeEventResolver} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultFightCommandBeforeEventResolver implements FightCommandBeforeEventResolver {

    @Autowired
    private FightBoundingCommandResolver fightBoundingCommandResolver;

    @Override
    public FightBeforeRoundResult handleBeforeRoundEvent(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final FightBeforeRoundResult result = new FightBeforeRoundResult();

        final FightRoundBoundingCommand fightBoundingCommand = command.getBeforeBounding();
        if (fightBoundingCommand != null) {
            final List<ParagraphData> resolve = fightBoundingCommandResolver.resolve(fightBoundingCommand, resolvationData).getResolveList();
            if (resolve != null) {
                for (final ParagraphData data : resolve) {
                    final FfParagraphData ffData = (FfParagraphData) data;
                    result.setInterrupted(result.isInterrupted() || ffData.isInterrupt());
                    result.setLoseBattle(result.isLoseBattle() || ffData.isLoseBattleRound());
                }
                resolveList.addAll(resolve);
            }
        }

        return result;
    }

}
