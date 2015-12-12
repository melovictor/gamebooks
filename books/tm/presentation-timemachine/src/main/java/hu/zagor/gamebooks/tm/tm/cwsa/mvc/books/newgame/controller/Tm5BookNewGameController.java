package hu.zagor.gamebooks.tm.tm.cwsa.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;
import hu.zagor.gamebooks.tm.mvc.book.newgame.controller.TmBookNewGameController;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.CIVIL_WAR_SECRET_AGENT)
public class Tm5BookNewGameController extends TmBookNewGameController {
}
