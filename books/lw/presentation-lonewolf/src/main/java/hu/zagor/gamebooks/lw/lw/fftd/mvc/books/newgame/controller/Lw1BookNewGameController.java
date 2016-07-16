package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.lw.mvc.book.newgame.controller.LwBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.LoneWolf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + LoneWolf.FLIGHT_FROM_THE_DARK)
public class Lw1BookNewGameController extends LwBookNewGameController {
}
