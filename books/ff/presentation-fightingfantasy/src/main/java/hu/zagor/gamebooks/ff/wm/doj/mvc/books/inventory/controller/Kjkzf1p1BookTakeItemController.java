package hu.zagor.gamebooks.ff.wm.doj.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagorJatekfuzetek;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazatZagorJatekfuzetek.AZ_ITELET_LABIRINTUSA)
public class Kjkzf1p1BookTakeItemController extends Wm5BookTakeItemController {
}
