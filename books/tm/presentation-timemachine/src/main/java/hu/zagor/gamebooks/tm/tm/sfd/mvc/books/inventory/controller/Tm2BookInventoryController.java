package hu.zagor.gamebooks.tm.tm.sfd.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;
import hu.zagor.gamebooks.tm.mvc.book.inventory.controller.TmBookInventoryController;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.SEARCH_FOR_DINOSAURS)
public class Tm2BookInventoryController extends TmBookInventoryController {
}
