package hu.zagor.gamebooks.ff.ff.rp.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.FantaziaHarcos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FantaziaHarcos.LAZADOK_BOLYGOJA)
public class Fh6BookNewGameController extends Ff18BookNewGameController {
}
