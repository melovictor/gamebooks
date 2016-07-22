package hu.zagor.gamebooks.lw.mvc.book.inventory.controller;

import hu.zagor.gamebooks.domain.LwBookInformations;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;

/**
 * Generic take item controller for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwBookTakeItemController extends GenericBookTakeItemController {
    @Override
    public LwBookInformations getInfo() {
        return (LwBookInformations) super.getInfo();
    }
}
