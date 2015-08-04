package hu.zagor.gamebooks.raw.sc.tcf.mvc.books.exception.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.hungarian.Csillagproba;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the exceptions to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Csillagproba.AZ_ELVARAZSOLT_UR_KASTELY)
public class Csp1BookExceptionController extends Sc3BookExceptionController {
}