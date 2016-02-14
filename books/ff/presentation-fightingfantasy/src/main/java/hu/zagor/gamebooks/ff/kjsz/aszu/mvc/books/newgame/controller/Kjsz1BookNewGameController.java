package hu.zagor.gamebooks.ff.kjsz.aszu.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekSzerelem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekSzerelem.A_SZERELEM_UTVESZTOI)
public class Kjsz1BookNewGameController extends RawBookNewGameController {
}
