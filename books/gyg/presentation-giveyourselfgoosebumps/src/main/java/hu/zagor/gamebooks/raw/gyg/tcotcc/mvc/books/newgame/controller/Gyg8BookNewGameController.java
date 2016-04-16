package hu.zagor.gamebooks.raw.gyg.tcotcc.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.GiveYourselfGoosebumps;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + GiveYourselfGoosebumps.THE_CURSE_OF_THE_CREEPING_COFFIN)
public class Gyg8BookNewGameController extends RawBookNewGameController {
}
