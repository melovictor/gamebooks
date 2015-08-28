package hu.zagor.gamebooks.ff.wm.tfc.mvc.books.image.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.image.controller.FfBookImageController;
import hu.zagor.gamebooks.support.bookids.english.Warlock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the image request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Warlock.THE_FLOATING_CITY)
public class Wm8BookImageController extends FfBookImageController {
}
