package hu.zagor.gamebooks.raw.cyoawd.switef.mvc.books.image.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.image.controller.GenericBookImageController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventureWaltDisney;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the image request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventureWaltDisney.SNOW_WHITE_IN_THE_ENCHANTED_FOREST)
public class Cyoawd1BookImageController extends GenericBookImageController {
}
