package hu.zagor.gamebooks.raw.cyoa.wkht.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookWelcomeController;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnak;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnak.KI_OLTE_MEG_HARLOWE_THROMBEY_T)
public class Vkm2BookWelcomeController extends GenericBookWelcomeController {
}
