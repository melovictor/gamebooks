package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.FantaziaHarcos;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FantaziaHarcos.LOPAKODO_LELKEK)
public class Fh10BookWelcomeController extends Ff34BookWelcomeController {

    /**
     * Redirects the user to the actual book's welcome page.
     * @return the redirect command
     */
    @RequestMapping(value = PageAddresses.BOOK_WELCOME)
    public String handleWelcome() {
        return "redirect:../" + KalandJatekKockazatZagor.LELEKRABLO + "/" + PageAddresses.BOOK_WELCOME;
    }

}
