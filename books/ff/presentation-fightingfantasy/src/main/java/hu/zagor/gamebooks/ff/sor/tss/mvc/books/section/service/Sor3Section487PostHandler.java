package hu.zagor.gamebooks.ff.sor.tss.mvc.books.section.service;

import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 487 post handler for Sorcery 3. When the confusion is gone, remove the after-fight random throwing.
 * @author Tamas_Szekeres
 */
@Component
public class Sor3Section487PostHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "4075")) {
            itemHandler.removeItem(character, "4075", 1);
            final List<ProcessableItemHolder> itemsToProcess = wrapper.getParagraph().getItemsToProcess();
            if (!itemsToProcess.isEmpty()) {
                final ProcessableItemHolder processableItemHolder = itemsToProcess.get(0);
                if (!processableItemHolder.isParagraphDataHolder()) {
                    final FightCommand command = (FightCommand) processableItemHolder.getCommand();
                    command.setAfterBounding(null);
                }
            }
        }
    }

}
