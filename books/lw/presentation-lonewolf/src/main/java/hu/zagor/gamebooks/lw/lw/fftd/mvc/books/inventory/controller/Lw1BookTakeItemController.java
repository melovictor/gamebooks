package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.lw.mvc.book.inventory.controller.LwBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.LoneWolf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + LoneWolf.FLIGHT_FROM_THE_DARK)
public class Lw1BookTakeItemController extends LwBookTakeItemController {
}
