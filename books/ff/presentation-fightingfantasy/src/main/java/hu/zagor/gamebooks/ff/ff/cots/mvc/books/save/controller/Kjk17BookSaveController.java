package hu.zagor.gamebooks.ff.ff.cots.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazat.A_VARAZSLO_KRIPTAJA)
public class Kjk17BookSaveController extends Ff26BookSaveController {
}
