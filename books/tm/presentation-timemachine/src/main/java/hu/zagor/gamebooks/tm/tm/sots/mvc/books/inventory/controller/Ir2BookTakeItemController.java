package hu.zagor.gamebooks.tm.tm.sots.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.IdogepRegeny;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + IdogepRegeny.A_SZAMURAJ_KARDJA)
public class Ir2BookTakeItemController extends Tm3BookTakeItemController {
}
