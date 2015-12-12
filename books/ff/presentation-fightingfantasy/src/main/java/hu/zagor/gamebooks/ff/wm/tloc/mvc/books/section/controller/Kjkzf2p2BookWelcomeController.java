package hu.zagor.gamebooks.ff.wm.tloc.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagorJatekfuzetek;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazatZagorJatekfuzetek.A_VALTOZASOK_FOLDJE)
public class Kjkzf2p2BookWelcomeController extends Wm11BookWelcomeController {
}
