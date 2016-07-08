package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.controller;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FantaziaHarcos.A_HALAL_SEREGEI)
public class Fh11BookWelcomeController extends Ff36BookWelcomeController {

    /**
     * Redirects the user to the actual book's welcome page.
     * @return the redirect command
     */
    @RequestMapping(value = PageAddresses.BOOK_WELCOME)
    public String handleWelcome() {
        return "redirect:../" + KalandJatekKockazatZagor.A_HALAL_SEREGEI + "/" + PageAddresses.BOOK_WELCOME;
    }

}
