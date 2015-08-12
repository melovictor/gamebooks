package hu.zagor.gamebooks.raw.cyoa.wkht.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.exception.controller.GenericBookExceptionController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventure.WHO_KILLED_HARLOWE_THROMBEY)
public class Cyoa9BookExceptionController extends GenericBookExceptionController {
}
