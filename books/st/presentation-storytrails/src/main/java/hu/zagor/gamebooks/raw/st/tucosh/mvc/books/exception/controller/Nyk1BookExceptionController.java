package hu.zagor.gamebooks.raw.st.tucosh.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.Nyomkereso;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Nyomkereso.SHERLOCK_HOLMES_MEGOLDATLAN_ESETE)
public class Nyk1BookExceptionController extends St14BookExceptionController {
}