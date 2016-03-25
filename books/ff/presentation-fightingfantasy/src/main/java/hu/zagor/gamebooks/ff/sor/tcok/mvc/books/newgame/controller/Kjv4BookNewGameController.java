package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekVarazslat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekVarazslat.A_KIRALYOK_KORONAJA)
public class Kjv4BookNewGameController extends Sor4BookNewGameController {
}
