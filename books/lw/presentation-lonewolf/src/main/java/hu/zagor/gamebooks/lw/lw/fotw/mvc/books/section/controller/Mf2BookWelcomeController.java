package hu.zagor.gamebooks.lw.lw.fotw.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.MaganyosFarkas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + MaganyosFarkas.LANGOLO_TENGER)
public class Mf2BookWelcomeController extends Lw2BookWelcomeController {
}
