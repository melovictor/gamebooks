package hu.zagor.gamebooks.tm.tm.sotk.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;
import hu.zagor.gamebooks.tm.mvc.book.save.controller.TmBookSaveController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.SECRET_OF_THE_KNIGHTS)
public class Tm1BookSaveController extends TmBookSaveController {
}
