package hu.zagor.gamebooks.raw.cyoa.dc.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnak;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnak.FEJFAERDOK_VAROSA)
public class Vkm5BookExceptionController extends Cyoa8BookExceptionController {
}
