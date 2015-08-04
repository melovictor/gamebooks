package hu.zagor.gamebooks.ff.ff.cot.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.save.controller.FfBookSaveController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.CITY_OF_THIEVES)
public class Ff5BookSaveController extends FfBookSaveController {
}
