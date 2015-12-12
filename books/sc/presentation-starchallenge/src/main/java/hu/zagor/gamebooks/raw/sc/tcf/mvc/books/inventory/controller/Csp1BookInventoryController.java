package hu.zagor.gamebooks.raw.sc.tcf.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.Csillagproba;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Csillagproba.AZ_ELVARAZSOLT_UR_KASTELY)
public class Csp1BookInventoryController extends Sc3BookInventoryController {
}
