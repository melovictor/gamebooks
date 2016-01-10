package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookWelcomeController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.KHARE_CITYPORT_OF_TRAPS)
public class Sor2BookWelcomeController extends GenericBookWelcomeController {
}
