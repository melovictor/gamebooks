package hu.zagor.gamebooks.tm.tm.sfd.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.exception.controller.GenericBookExceptionController;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.SEARCH_FOR_DINOSAURS)
public class Tm2BookExceptionController extends GenericBookExceptionController {
}
