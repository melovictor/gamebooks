package hu.zagor.gamebooks.tm.tm.sfd.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.IdogepRegeny;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + IdogepRegeny.A_SARKANYGYIKOK_NYOMABAN)
public class Ir3BookNewGameController extends Tm2BookNewGameController {
}
