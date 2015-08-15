package hu.zagor.gamebooks.ff.ff.cotsw.mvc.books.save.controller;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + AventurasFantasticas.AS_CAVERNAS_DA_FEITICEIRA_DA_NEVE)
public class Afb17BookSaveController extends Ff9BookSaveController {
}
