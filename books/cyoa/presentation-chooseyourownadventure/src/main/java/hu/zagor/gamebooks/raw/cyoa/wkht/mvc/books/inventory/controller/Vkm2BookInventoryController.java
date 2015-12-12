package hu.zagor.gamebooks.raw.cyoa.wkht.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.inventory.controller.RawBookInventoryController;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnak;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnak.KI_OLTE_MEG_HARLOWE_THROMBEY_T)
public class Vkm2BookInventoryController extends RawBookInventoryController {
}
