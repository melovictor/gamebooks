package hu.zagor.gamebooks.raw.cyoawd.switef.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookWelcomeController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventureWaltDisney;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventureWaltDisney.SNOW_WHITE_IN_THE_ENCHANTED_FOREST)
public class Cyoawd1BookWelcomeController extends GenericBookWelcomeController {
}
