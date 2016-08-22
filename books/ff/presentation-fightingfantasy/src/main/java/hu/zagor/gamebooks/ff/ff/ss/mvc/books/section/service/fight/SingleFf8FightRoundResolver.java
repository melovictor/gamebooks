package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF8.
 * @author Tamas_Szekeres
 */
@Component("singleff8FightRoundResolver")
public class SingleFf8FightRoundResolver implements FfFightRoundResolver {
    @Autowired @Qualifier("singleFightRoundResolver") private FfFightRoundResolver decorated;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final int stamina = character.getStamina();
        final FightRoundResult[] roundResult = decorated.resolveRound(command, resolvationData, beforeRoundResult);
        final int diff = Math.abs(character.getStamina() - stamina);
        if ("284".equals(resolvationData.getParagraph().getId()) && diff > 0) {
            resolvationData.getCharacterHandler().getItemHandler().addItem(character, "4004", diff);
        }
        return roundResult;
    }

    @Override
    public void resolveFlee(final FfFightCommand command, final ResolvationData resolvationData) {
        decorated.resolveFlee(command, resolvationData);
    }

}
