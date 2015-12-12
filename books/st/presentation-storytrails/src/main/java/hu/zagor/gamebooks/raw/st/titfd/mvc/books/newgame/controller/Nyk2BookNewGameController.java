package hu.zagor.gamebooks.raw.st.titfd.mvc.books.newgame.controller;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Nyomkereso.RETTEGES_A_NEGYEDIK_DIMENZIOBAN)
public class Nyk2BookNewGameController extends St2BookNewGameController {
}
