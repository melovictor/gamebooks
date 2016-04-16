package hu.zagor.gamebooks.mvc.bookselection.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.bookselection.facade.BookListFacade;
import hu.zagor.gamebooks.mvc.generic.controller.LanguageAwareController;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the book selection page.
 * @author Tamas_Szekeres
 */
@Controller
public class BookSelectionController extends LanguageAwareController {

    @Autowired private BookListFacade bookListFacade;
    @LogInject private Logger logger;

    /**
     * Method for putting together the book selection screen.
     * @param model the model of the page
     * @param request the servlet request
     * @param locale the locale of the request
     * @return the book selection page
     */
    @RequestMapping(value = PageAddresses.BOOK_LIST)
    public String getBookList(final Model model, final HttpServletRequest request, final Locale locale) {
        logger.debug("BookSelectionController.getBookList");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final HttpSession session = request.getSession();

        model.addAttribute(ControllerAddresses.USER_STORE_KEY, session.getAttribute(ControllerAddresses.USER_STORE_KEY));
        initializeLanguages(model, request);
        model.addAttribute("bookList", "bookList");

        logger.debug("Locale: {}_{}", locale.getLanguage(), locale.getCountry());
        model.addAttribute("bookSeriesData", bookListFacade.getAvailableBooks(locale, wrapper.getPlayer()));

        model.addAttribute("pageTitle", "page.title");

        return PageAddresses.BOOK_LIST;
    }

    public void setBookListFacade(final BookListFacade bookListFacade) {
        this.bookListFacade = bookListFacade;
    }

    public void setLogger(final Logger logger) {
        this.logger = logger;
    }
}
