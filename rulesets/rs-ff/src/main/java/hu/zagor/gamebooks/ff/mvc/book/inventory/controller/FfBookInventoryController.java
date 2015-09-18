package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.inventory.controller.RawBookInventoryController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

/**
 * Generic inventory controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookInventoryController extends RawBookInventoryController {

    @Override
    public String handleInventory(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        super.handleInventory(model, request, response);

        return "ffCharPage." + getInfo().getResourceDir();
    }

    @Override
    public FfCharacterPageData getCharacterPageData(final Character character) {
        return (FfCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }
}
