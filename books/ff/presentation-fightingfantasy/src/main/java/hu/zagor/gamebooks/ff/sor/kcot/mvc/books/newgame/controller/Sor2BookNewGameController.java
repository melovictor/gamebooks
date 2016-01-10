package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.newgame.controller.SorBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.KHARE_CITYPORT_OF_TRAPS)
public class Sor2BookNewGameController extends SorBookNewGameController {
}
