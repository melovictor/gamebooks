package hu.zagor.gamebooks.raw.cyoar.sotn.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnak;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnak.A_NINDZSA_TITKA)
public class Vkm4BookNewGameController extends RawBookNewGameController {
}
