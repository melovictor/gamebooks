package hu.zagor.gamebooks.raw.agirl.agwiaw.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.EgyLany;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + EgyLany.EGY_LANY_BEALLIT_AZ_ESKUVORE)
public class Egylany2BookTakeItemController extends Agirl2BookTakeItemController {
}
