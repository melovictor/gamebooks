package hu.zagor.gamebooks.ff.pt.isotll.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagorJatekfuzetek;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazatZagorJatekfuzetek.AZ_ELVESZETT_FOLD_NYOMABAN)
public class Kjkzf2p3BookSaveController extends Pt14BookSaveController {
}
