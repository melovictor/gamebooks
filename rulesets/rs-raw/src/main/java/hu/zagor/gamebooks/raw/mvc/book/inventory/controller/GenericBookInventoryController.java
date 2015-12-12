package hu.zagor.gamebooks.raw.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.inventory.service.BookInventoryService;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic inventory controller for books with no rule system.
 * @author Tamas_Szekeres
 */
@Controller
public class GenericBookInventoryController extends AbstractRequestWrappingController {

    @Autowired @Qualifier("rawSectionHandlingService") private SectionHandlingService sectionHandlingService;
    @Resource(name = "inventoryControllerMap") private Map<Long, String> inventoryControllerIdBeanNameMap;

    /**
     * Handles the inventory.
     * @param model the model
     * @param request the http request
     * @param response the http response
     * @param bookId the ID of the book we're working with
     * @return the name of the char page
     */
    @RequestMapping(value = PageAddresses.BOOK_PAGE + "/{bookId}/s/" + PageAddresses.BOOK_INVENTORY)
    public String handleInventory(final Model model, final HttpServletRequest request, final HttpServletResponse response, @PathVariable("bookId") final Long bookId) {
        response.setCharacterEncoding("UTF-8");
        final BookInformations info = getInfo(bookId);
        final String inventoryServiceBeanName = inventoryControllerIdBeanNameMap.get(info.getSeriesId());
        if (inventoryServiceBeanName == null) {
            throw new IllegalStateException("No service bean name is specified for series ID '" + info.getSeriesId() + "'.");
        }
        final BookInventoryService inventoryService = (BookInventoryService) getBeanFactory().getBean(inventoryServiceBeanName);
        return inventoryService.handleInventory(model, getWrapper(request), info);
    }

    /**
     * Saves modifications in the notes.
     * @param notes the modified notes
     * @param request the {@link HttpServletRequest} bean
     */
    @RequestMapping(value = PageAddresses.BOOK_PAGE + "/{bookId}/s/" + PageAddresses.BOOK_INVENTORY + "/notes")
    @ResponseBody
    public void saveNotes(final HttpServletRequest request, @Qualifier("notes") final String notes) {
        final Character c = getWrapper(request).getCharacter();
        c.setNotes(notes);
    }
}
