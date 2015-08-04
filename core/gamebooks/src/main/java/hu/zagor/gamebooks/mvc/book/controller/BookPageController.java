package hu.zagor.gamebooks.mvc.book.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling request to a given, otherwise unhandled book.
 * @author Tamas_Szekeres
 */
@Controller
public class BookPageController {

    @LogInject
    private Logger logger;

    /**
     * Displays a generic page with basically no content. This controller shouldn't be activated at any time, if it is, it means that there is no book-specific controller, which is
     * a problem.
     * @param model the data model
     * @param session the session
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.BOOK_PAGE + "/{bookId}")
    public String handleGet(final Model model, final HttpSession session) {
        logger.error("The primitive BookPageController has ben called! Something is really wrong!");

        final PlayerUser player = (PlayerUser) session.getAttribute(ControllerAddresses.USER_STORE_KEY);
        model.addAttribute(ControllerAddresses.USER_STORE_KEY, player);
        model.addAttribute("pageTitle", "page.title.books");
        return PageAddresses.BOOK_PAGE;
    }

}
