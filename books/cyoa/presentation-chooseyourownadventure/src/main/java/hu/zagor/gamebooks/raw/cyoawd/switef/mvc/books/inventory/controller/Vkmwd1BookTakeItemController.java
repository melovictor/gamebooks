package hu.zagor.gamebooks.raw.cyoawd.switef.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnakWaltDisney;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnakWaltDisney.HOFEHERKE_AZ_ELVARAZSOLT_RENGETEGBEN)
public class Vkmwd1BookTakeItemController extends Cyoawd1BookTakeItemController {
}
