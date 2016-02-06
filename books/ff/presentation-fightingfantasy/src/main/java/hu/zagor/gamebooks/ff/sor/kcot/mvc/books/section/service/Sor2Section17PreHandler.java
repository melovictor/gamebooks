package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.service;

import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import java.util.List;
import org.springframework.ui.Model;

/**
 * Section 17 pre handler for Sorcery 2.
 * @author Tamas_Szekeres
 */
public class Sor2Section17PreHandler implements CustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final Paragraph paragraph = wrapper.getParagraph();
        final List<ProcessableItemHolder> itemsToProcess = paragraph.getItemsToProcess();
        if (!itemsToProcess.isEmpty()) {
            final SorCharacter character = (SorCharacter) wrapper.getCharacter();
            final AttributeTestCommand command = (AttributeTestCommand) itemsToProcess.get(0).getCommand();
            final int itemCount = getItemCountForDancers(character, (FfBookInformations) info);
            command.setAgainst(String.valueOf(itemCount));
        }
    }

    private int getItemCountForDancers(final SorCharacter character, final FfBookInformations info) {
        int count = 0;
        if (character.getGold() > 0) {
            count = 1;
        }
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "2000")) {
            count++;
        }
        for (final Item item : character.getEquipment()) {
            if (item.getItemType() == ItemType.shadow) {
                continue;
            }
            if (item.getItemType() == ItemType.provision) {
                if (!"2000".equals(item.getId())) {
                    count++;
                }
            } else if (!"defWpn".equals(item.getId())) {
                count++;
            }
        }

        return count;
    }

}
