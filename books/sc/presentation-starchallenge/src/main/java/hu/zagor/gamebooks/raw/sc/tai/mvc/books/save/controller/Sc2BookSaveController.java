package hu.zagor.gamebooks.raw.sc.tai.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.save.controller.RawBookSaveController;
import hu.zagor.gamebooks.support.bookids.english.StarChallenge;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + StarChallenge.THE_ANDROID_INVASION)
public class Sc2BookSaveController extends RawBookSaveController {
}
