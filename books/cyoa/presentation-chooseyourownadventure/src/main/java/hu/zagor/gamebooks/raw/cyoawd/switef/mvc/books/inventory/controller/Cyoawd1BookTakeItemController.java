package hu.zagor.gamebooks.raw.cyoawd.switef.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventureWaltDisney;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventureWaltDisney.SNOW_WHITE_IN_THE_ENCHANTED_FOREST)
public class Cyoawd1BookTakeItemController extends GenericBookTakeItemController {
}
