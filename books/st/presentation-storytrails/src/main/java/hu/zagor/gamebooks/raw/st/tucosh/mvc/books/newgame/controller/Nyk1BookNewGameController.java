package hu.zagor.gamebooks.raw.st.tucosh.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.Nyomkereso;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Nyomkereso.SHERLOCK_HOLMES_MEGOLDATLAN_ESETE)
public class Nyk1BookNewGameController extends St14BookNewGameController {
}
