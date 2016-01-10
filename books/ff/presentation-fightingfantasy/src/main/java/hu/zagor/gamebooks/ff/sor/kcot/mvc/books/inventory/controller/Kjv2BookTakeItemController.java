package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekVarazslat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekVarazslat.KHARE_A_CSAPDAK_KIKOTOVAROSA)
public class Kjv2BookTakeItemController extends Sor2BookTakeItemController {
}
