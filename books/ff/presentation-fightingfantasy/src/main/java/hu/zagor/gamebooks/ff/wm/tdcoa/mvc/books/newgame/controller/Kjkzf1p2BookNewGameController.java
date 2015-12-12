package hu.zagor.gamebooks.ff.wm.tdcoa.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagorJatekfuzetek;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazatZagorJatekfuzetek.ANAKENDIS_SOTET_KRONIKAI)
public class Kjkzf1p2BookNewGameController extends Wm6BookNewGameController {
}
