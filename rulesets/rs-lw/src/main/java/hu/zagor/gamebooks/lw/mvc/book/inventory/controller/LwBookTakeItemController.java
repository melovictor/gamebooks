package hu.zagor.gamebooks.lw.mvc.book.inventory.controller;

import hu.zagor.gamebooks.domain.LwBookInformations;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.support.messages.MessageSource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generic take item controller for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwBookTakeItemController extends GenericBookTakeItemController {
    @Autowired private MessageSource messages;

    @Override
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final TakeItemResponse response = super.doHandleItemTake(request, itemId, amount);
        String msgKeyPostfix = null;
        if (response.getTotalTaken() == 0) {
            if (amount == 1) {
                msgKeyPostfix = "singleItemNotTaken";
            } else {
                msgKeyPostfix = "multiItemsNotTaken";
            }
        } else if (response.getTotalTaken() < amount) {
            msgKeyPostfix = "notAllItemsTaken";
        }
        if (msgKeyPostfix != null) {
            response.setWarningMessage(messages.getMessage("page.book.inventory.takeItem." + msgKeyPostfix));
        }
        return response;
    }

    @Override
    public LwBookInformations getInfo() {
        return (LwBookInformations) super.getInfo();
    }
}
