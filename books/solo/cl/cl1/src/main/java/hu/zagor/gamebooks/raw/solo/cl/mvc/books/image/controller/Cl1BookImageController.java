package hu.zagor.gamebooks.raw.solo.cl.mvc.books.image.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.image.controller.GenericBookImageController;
import hu.zagor.gamebooks.support.bookids.hungarian.Solo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the image request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Solo.CIGANYLABIRINTUS)
public class Cl1BookImageController extends GenericBookImageController {
}
