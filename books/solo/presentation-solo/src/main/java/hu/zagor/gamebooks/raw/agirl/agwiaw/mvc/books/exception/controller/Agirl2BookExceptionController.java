package hu.zagor.gamebooks.raw.agirl.agwiaw.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.exception.controller.GenericBookExceptionController;
import hu.zagor.gamebooks.support.bookids.english.AGirl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + AGirl.A_GIRL_WALKS_INTO_A_WEDDING)
public class Agirl2BookExceptionController extends GenericBookExceptionController {
}