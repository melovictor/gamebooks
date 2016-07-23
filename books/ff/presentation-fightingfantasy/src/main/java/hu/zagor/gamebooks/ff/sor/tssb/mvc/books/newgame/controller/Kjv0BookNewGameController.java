package hu.zagor.gamebooks.ff.sor.tssb.mvc.books.newgame.controller;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekVarazslat.A_VARAZSLATOK_KONYVE)
public class Kjv0BookNewGameController extends Sor0BookNewGameController {
}
