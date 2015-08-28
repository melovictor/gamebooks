package hu.zagor.gamebooks.ff.wm.tfc.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.save.controller.FfBookSaveController;
import hu.zagor.gamebooks.support.bookids.english.Warlock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Warlock.THE_FLOATING_CITY)
public class Wm8BookSaveController extends FfBookSaveController {
}
