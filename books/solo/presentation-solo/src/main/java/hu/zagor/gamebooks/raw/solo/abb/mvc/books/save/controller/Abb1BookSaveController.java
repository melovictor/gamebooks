package hu.zagor.gamebooks.raw.solo.abb.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.save.controller.RawBookSaveController;
import hu.zagor.gamebooks.support.bookids.hungarian.Solo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Solo.A_BIZONYTALANSAG_BORTONEBEN)
public class Abb1BookSaveController extends RawBookSaveController {
}
