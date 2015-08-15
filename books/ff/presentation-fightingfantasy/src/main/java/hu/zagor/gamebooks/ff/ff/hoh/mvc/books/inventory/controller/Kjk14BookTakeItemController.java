package hu.zagor.gamebooks.ff.ff.hoh.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazat.AZ_ELATKOZOTT_HAZ)
public class Kjk14BookTakeItemController extends Ff10BookTakeItemController {
}
