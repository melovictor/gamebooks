package hu.zagor.gamebooks.raw.cyoa.juts.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventure;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventure.JOURNEY_UNDER_THE_SEA)
public class Cyoa2BookNewGameController extends RawBookNewGameController {
}
