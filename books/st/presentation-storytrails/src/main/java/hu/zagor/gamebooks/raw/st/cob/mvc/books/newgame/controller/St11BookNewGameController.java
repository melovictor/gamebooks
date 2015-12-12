package hu.zagor.gamebooks.raw.st.cob.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.Storytrails;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Storytrails.COSNPIRACY_OF_BLOOD)
public class St11BookNewGameController extends RawBookNewGameController {
}
