package hu.zagor.gamebooks.tm.tm.sotk.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookWelcomeController;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.SECRET_OF_THE_KNIGHTS)
public class Tm1BookWelcomeController extends GenericBookWelcomeController {
}
