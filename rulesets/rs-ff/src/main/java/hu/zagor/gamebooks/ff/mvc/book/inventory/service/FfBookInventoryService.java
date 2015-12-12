package hu.zagor.gamebooks.ff.mvc.book.inventory.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.inventory.service.RawBookInventoryService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Basic service for handling the inventory display for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
@Component
public class FfBookInventoryService extends RawBookInventoryService {

    @Override
    public String handleInventory(final Model model, final HttpSessionWrapper wrapper, final BookInformations info) {
        super.handleInventory(model, wrapper, info);
        return "ffCharPage." + info.getResourceDir();
    }

    @Override
    protected FfCharacterPageData getCharacterPageData(final Character character, final BookInformations info) {
        return (FfCharacterPageData) getBeanFactory().getBean(info.getCharacterPageDataBeanId(), character, info.getCharacterHandler());
    }

}
