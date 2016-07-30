package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.lw.content.command.fight.roundresolver.LwPrePostEnemyFightHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for Vordak #9.
 * @author Tamas_Szekeres
 */
@Component
public class Lw1PrePost9Handler implements LwPrePostEnemyFightHandler {

    @Override
    public boolean shouldExecutePre(final LwFightCommand command, final ResolvationData resolvationData) {
        return false;
    }

    @Override
    public Object executePre(final LwFightCommand command, final ResolvationData resolvationData) {
        return null;
    }

    @Override
    public boolean shouldExecutePost(final LwFightCommand command, final ResolvationData resolvationData) {
        return command.getRoundNumber() == 1;
    }

    @Override
    public void executePost(final LwFightCommand command, final ResolvationData resolvationData, final Object preGenData) {
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getInfo().getCharacterHandler().getItemHandler();
        itemHandler.removeItem(character, "50001", 2);
        final LwEnemy lwEnemy = command.getResolvedEnemies().get(0);
        lwEnemy.setMindblast(true);
    }

}
