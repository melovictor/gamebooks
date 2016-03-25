package hu.zagor.gamebooks.raw.gyg.ttyd.mvc.books.newgame.controller;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + GiveYourselfGoosebumps.TICK_TOCK_YOURE_DEAD)
public class Gyg2BookNewGameController extends RawBookNewGameController {
}
