package hu.zagor.gamebooks.raw.cyoa.ycnij.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookWelcomeController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventure.YOUR_CODE_NAME_IS_JONAH)
public class Cyoa6BookWelcomeController extends GenericBookWelcomeController {
}
