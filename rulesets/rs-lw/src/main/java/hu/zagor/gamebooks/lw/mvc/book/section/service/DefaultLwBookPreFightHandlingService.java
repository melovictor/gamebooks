package hu.zagor.gamebooks.lw.mvc.book.section.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.character.item.LwItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.LwBookInformations;
import hu.zagor.gamebooks.support.logging.LogInject;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link LwBookPreFightHandlingService} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultLwBookPreFightHandlingService implements LwBookPreFightHandlingService {
    @LogInject private Logger logger;

    @Override
    public void handlePreFightItemUsage(final LwBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        logger.info("Handling item {} during pre-fight.", itemId);
        final LwCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        final LwItem resolveItem = (LwItem) itemHandler.resolveItem(itemId);
        final Character character = wrapper.getCharacter();
        itemHandler.removeItem(character, itemId, 1);
        itemHandler.addItem(character, "50000", resolveItem.getCombatSkill());
    }

}
