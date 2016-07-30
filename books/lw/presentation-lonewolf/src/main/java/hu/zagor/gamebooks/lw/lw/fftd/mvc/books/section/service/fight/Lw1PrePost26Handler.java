package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.lw.content.command.fight.roundresolver.LwPrePostEnemyFightHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for marsh viper.
 * @author Tamas_Szekeres
 */
@Component
public class Lw1PrePost26Handler implements LwPrePostEnemyFightHandler {

    @Override
    public boolean shouldExecutePre(final LwFightCommand command, final ResolvationData resolvationData) {
        return true;
    }

    @Override
    public Object executePre(final LwFightCommand command, final ResolvationData resolvationData) {
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        return character.getEndurance();
    }

    @Override
    public boolean shouldExecutePost(final LwFightCommand command, final ResolvationData resolvationData) {
        return true;
    }

    @Override
    public void executePost(final LwFightCommand command, final ResolvationData resolvationData, final Object preGenData) {
        final int preEndurance = (int) preGenData;
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final int postEndurance = character.getEndurance();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        if (preEndurance > postEndurance && !itemHandler.hasItem(character, "50003")) {
            itemHandler.addItem(character, "50003", 1);
        }
    }

}
