package hu.zagor.gamebooks.raw.cyoar.sotn.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.save.controller.RawBookSaveController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventureReissue;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventureReissue.SECRET_OF_THE_NINJA)
public class Cyoar16BookSaveController extends RawBookSaveController {
}
