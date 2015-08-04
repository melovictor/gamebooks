package hu.zagor.gamebooks.raw.sc.tcf.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.inventory.controller.RawBookInventoryController;
import hu.zagor.gamebooks.support.bookids.english.StarChallenge;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + StarChallenge.THE_COSMIC_FUNHOUSE)
public class Sc3BookInventoryController extends RawBookInventoryController {
}
