package hu.zagor.gamebooks.raw.agirl.agwiab.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.EgyLany;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + EgyLany.EGY_LANY_BELEP_A_BARBA)
public class Egylany1BookNewGameController extends Agirl1BookNewGameController {
}
