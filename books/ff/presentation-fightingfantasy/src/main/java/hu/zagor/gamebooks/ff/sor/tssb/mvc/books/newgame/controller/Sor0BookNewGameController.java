package hu.zagor.gamebooks.ff.sor.tssb.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.newgame.controller.FfBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SORCERY_SPELL_BOOK)
public class Sor0BookNewGameController extends FfBookNewGameController {
}
