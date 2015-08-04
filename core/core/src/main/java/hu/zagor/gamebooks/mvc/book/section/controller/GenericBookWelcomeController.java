package hu.zagor.gamebooks.mvc.book.section.controller;

import hu.zagor.gamebooks.PageAddresses;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Class for handling the welcome requests for the
 * different book rules.
 * @author Tamas_Szekeres
 *
 */
public class GenericBookWelcomeController {
    private static final String REDIRECT = "redirect:";

    /**
     * Redirects the reader to the book's welcome page.
     * @param request the request
     * @return the redirection command
     */
    @RequestMapping
    public String handleNoCommand(final HttpServletRequest request) {
        return REDIRECT + request.getRequestURI().replaceFirst(request.getContextPath(), "") + "/" + PageAddresses.BOOK_WELCOME;
    }

    /**
     * Redirects the reader to the book's welcome page.
     * @return the redirection command
     */
    @RequestMapping(value = "/")
    public String handleEmpty() {
        return REDIRECT + PageAddresses.BOOK_WELCOME;
    }

}
