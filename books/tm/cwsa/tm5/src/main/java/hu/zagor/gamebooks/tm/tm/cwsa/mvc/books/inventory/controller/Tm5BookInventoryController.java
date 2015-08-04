package hu.zagor.gamebooks.tm.tm.cwsa.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.tm.mvc.book.inventory.controller.TmBookInventoryController;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.CIVIL_WAR_SECRET_AGENT)
public class Tm5BookInventoryController extends TmBookInventoryController {
}