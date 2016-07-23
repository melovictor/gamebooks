package hu.zagor.gamebooks.ff.ff.sa.mvc.books.newgame.controller;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FantaziaHarcos.UR_ORGYILKOS)
public class Fh1BookNewGameController extends Ff12BookNewGameController {
}
