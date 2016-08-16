package hu.zagor.gamebooks.lw.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.inventory.service.RawBookInventoryService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Basic service for handling the inventory display for Lone Wolf books.
 * @author Tamas_Szekeres
 */
@Component
public class LwBookInventoryService extends RawBookInventoryService {

    @Override
    public String handleInventory(final Model model, final HttpSessionWrapper wrapper, final BookInformations info) {
        super.handleInventory(model, wrapper, info);
        model.addAttribute("paragraph", wrapper.getParagraph());
        return "lwCharPage." + info.getResourceDir();
    }

    @Override
    protected LwCharacterPageData getCharacterPageData(final Character character, final BookInformations info) {
        return (LwCharacterPageData) getBeanFactory().getBean(info.getCharacterPageDataBeanId(), character, info.getCharacterHandler());
    }

}
