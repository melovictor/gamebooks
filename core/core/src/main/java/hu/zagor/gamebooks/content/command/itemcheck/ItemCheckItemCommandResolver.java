package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.support.logging.LogInject;
import org.slf4j.Logger;

/**
 * Class for resolving an item-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckItemCommandResolver implements ItemCheckStubCommandResolver {
    @LogInject private Logger logger;

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        ParagraphData toResolve;
        final CharacterItemHandler characterItemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final String id = parent.getId();
        final int amount = parent.getAmount();
        final Character character = resolvationData.getCharacter();
        if (amount == 1 && characterItemHandler.hasEquippedItem(character, id)) {
            logger.info("Player has single equipped item '{}'.", id);
            toResolve = parent.getHaveEquipped();
        } else if (characterItemHandler.hasItem(character, id, amount)) {
            logger.info("Player has item '{}'.", id);
            toResolve = parent.getHave();
        } else {
            logger.info("Player doesn't have item '{}'.", id);
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }
}
