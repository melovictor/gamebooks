package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.enemy.FfEnemyHandler;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Class for resolving an item-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckEnemyCommand implements ItemCheckStubCommand {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final ParagraphData toResolve;

        final FfCharacterHandler ffCharacterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfEnemyHandler enemyHandler = ffCharacterHandler.getEnemyHandler();
        final String id = parent.getId();
        if (enemyHandler.isEnemyAlive(id)) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }
}
