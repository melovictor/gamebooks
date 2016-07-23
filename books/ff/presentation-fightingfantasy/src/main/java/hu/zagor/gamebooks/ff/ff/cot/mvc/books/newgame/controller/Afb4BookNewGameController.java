package hu.zagor.gamebooks.ff.ff.cot.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.brazilian.AventurasFantasticas;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + AventurasFantasticas.A_CIDADE_DOS_LADROES)
public class Afb4BookNewGameController extends Ff5BookNewGameController {
}
