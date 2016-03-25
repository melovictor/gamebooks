package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.SorBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_CROWN_OF_KINGS)
public class Sor4BookTakeItemController extends SorBookTakeItemController {
}
