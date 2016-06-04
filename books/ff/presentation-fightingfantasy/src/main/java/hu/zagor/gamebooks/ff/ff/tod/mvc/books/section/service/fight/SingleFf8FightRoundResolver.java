package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF8.
 * @author Tamas_Szekeres
 */
@Component("singleff8FightRoundResolver")
public class SingleFf8FightRoundResolver implements FightRoundResolver {
    @Autowired @Qualifier("singleFightRoundResolver") private FightRoundResolver decorated;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final int stamina = character.getStamina();
        final FightRoundResult[] roundResult = decorated.resolveRound(command, resolvationData, beforeRoundResult);
        final int diff = character.getStamina() - stamina;
        if ("284".equals(resolvationData.getParagraph().getId())) {
            resolvationData.getCharacterHandler().getItemHandler().addItem(character, "4004", diff);
        }
        return roundResult;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        decorated.resolveFlee(command, resolvationData);
    }

}
