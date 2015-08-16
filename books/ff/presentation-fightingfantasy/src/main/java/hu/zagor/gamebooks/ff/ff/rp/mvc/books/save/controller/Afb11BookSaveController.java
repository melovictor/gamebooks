package hu.zagor.gamebooks.ff.ff.rp.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.brazilian.AventurasFantasticas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + AventurasFantasticas.PLANETA_REBELDE)
public class Afb11BookSaveController extends Ff18BookSaveController {
}