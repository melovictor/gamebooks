package hu.zagor.gamebooks.raw.st.cob.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Storytrails;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Storytrails.COSNPIRACY_OF_BLOOD)
public class St11BookTakeItemController extends GenericBookTakeItemController {
}
