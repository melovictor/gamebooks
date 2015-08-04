package hu.zagor.gamebooks.ff.ff.rp.mvc.books.image.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.image.controller.FfBookImageController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the image request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.REBEL_PLANET)
public class Ff18BookImageController extends FfBookImageController {
}
