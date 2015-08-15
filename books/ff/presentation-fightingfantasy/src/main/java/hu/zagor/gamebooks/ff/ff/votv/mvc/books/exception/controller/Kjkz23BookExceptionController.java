package hu.zagor.gamebooks.ff.ff.votv.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekKockazatZagor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekKockazatZagor.A_VAMPIR_KRIPTAJA)
public class Kjkz23BookExceptionController extends Ff38BookExceptionController {
}
