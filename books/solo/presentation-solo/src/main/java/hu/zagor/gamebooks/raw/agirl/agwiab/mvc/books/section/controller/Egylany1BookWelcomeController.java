package hu.zagor.gamebooks.raw.agirl.agwiab.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.EgyLany;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + EgyLany.EGY_LANY_BELEP_A_BARBA)
public class Egylany1BookWelcomeController extends Agirl1BookWelcomeController {
}
