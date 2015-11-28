package hu.zagor.gamebooks.ff.sor.tsh.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookInventoryController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SHAMUTANTI_HILLS)
public class Sor1BookInventoryController extends FfBookInventoryController {
}
