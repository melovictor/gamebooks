package hu.zagor.gamebooks.tm.tm.sotk.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.SECRET_OF_THE_KNIGHTS)
public class Tm1BookTakeItemController extends GenericBookTakeItemController {
}
