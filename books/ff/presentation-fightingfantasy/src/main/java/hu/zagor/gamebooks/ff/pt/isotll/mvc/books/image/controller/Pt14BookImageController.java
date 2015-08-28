package hu.zagor.gamebooks.ff.pt.isotll.mvc.books.image.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.image.controller.FfBookImageController;
import hu.zagor.gamebooks.support.bookids.english.Proteus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the image request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Proteus.IN_SEARCH_OF_THE_LOST_LAND)
public class Pt14BookImageController extends FfBookImageController {
}
