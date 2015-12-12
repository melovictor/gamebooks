package hu.zagor.gamebooks.ff.ff.hoh.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.support.bookids.brazilian.AventurasFantasticas;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the inventory list request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + AventurasFantasticas.MANSAO_DAS_TREVAS)
public class Afb20BookInventoryController extends Ff10BookInventoryController {
}
