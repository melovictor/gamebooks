package hu.zagor.gamebooks.ff.ff.twofm.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookInventoryController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_WARLOCK_OF_FIRETOP_MOUNTAIN)
public class Ff1BookInventoryController extends FfBookInventoryController {
}
