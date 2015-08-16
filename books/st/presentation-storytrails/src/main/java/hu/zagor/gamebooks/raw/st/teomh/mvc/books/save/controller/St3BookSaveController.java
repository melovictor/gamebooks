package hu.zagor.gamebooks.raw.st.teomh.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.save.controller.RawBookSaveController;
import hu.zagor.gamebooks.support.bookids.english.Storytrails;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Storytrails.THE_EVIL_OF_MR_HAPPINESS)
public class St3BookSaveController extends RawBookSaveController {
}