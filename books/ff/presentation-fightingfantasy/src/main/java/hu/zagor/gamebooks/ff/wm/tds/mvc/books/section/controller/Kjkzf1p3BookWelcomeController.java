package hu.zagor.gamebooks.ff.wm.tds.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagorJatekfuzetek;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazatZagorJatekfuzetek.A_DERVIS_KOVE)
public class Kjkzf1p3BookWelcomeController extends Wm4BookWelcomeController {
}
