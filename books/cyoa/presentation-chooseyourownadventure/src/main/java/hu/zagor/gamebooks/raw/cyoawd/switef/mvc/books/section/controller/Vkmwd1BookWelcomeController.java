package hu.zagor.gamebooks.raw.cyoawd.switef.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnakWaltDisney;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnakWaltDisney.HOFEHERKE_AZ_ELVARAZSOLT_RENGETEGBEN)
public class Vkmwd1BookWelcomeController extends Cyoawd1BookWelcomeController {
}
