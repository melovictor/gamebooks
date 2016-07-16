package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.MaganyosFarkas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + MaganyosFarkas.MENEKULES_A_SOTETSEGBOL)
public class Mf1BookTakeItemController extends Lw1BookTakeItemController {
}
