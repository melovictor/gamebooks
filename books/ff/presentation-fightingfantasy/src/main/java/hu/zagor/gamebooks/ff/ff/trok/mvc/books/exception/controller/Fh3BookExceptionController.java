package hu.zagor.gamebooks.ff.ff.trok.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.FantaziaHarcos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FantaziaHarcos.A_KETHERI_MAFFIA)
public class Fh3BookExceptionController extends Ff15BookExceptionController {
}
