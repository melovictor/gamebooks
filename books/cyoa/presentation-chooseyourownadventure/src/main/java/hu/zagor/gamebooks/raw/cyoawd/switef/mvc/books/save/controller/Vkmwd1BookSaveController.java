package hu.zagor.gamebooks.raw.cyoawd.switef.mvc.books.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnakWaltDisney;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the save request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnakWaltDisney.HOFEHERKE_AZ_ELVARAZSOLT_RENGETEGBEN)
public class Vkmwd1BookSaveController extends Cyoawd1BookSaveController {
}
