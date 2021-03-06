package hu.zagor.gamebooks.tm.tm.sfd.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.IdogepRegeny;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + IdogepRegeny.A_SARKANYGYIKOK_NYOMABAN)
public class Ir3BookTakeItemController extends Tm2BookTakeItemController {
}
