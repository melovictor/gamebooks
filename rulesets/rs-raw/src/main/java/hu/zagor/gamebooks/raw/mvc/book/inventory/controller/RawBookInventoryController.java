package hu.zagor.gamebooks.raw.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.controller.CharacterPageDisplayingController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic inventory controller for books with no rule system.
 * @author Tamas_Szekeres
 */
public class RawBookInventoryController extends AbstractRequestWrappingController implements CharacterPageDisplayingController {

    @Autowired
    @Qualifier("rawSectionHandlingService")
    private SectionHandlingService sectionHandlingService;

    /**
     * Handles the inventory.
     * @param model the model
     * @param request the http request
     * @param response the http response
     * @return the name of the char page
     */
    @RequestMapping(value = PageAddresses.BOOK_INVENTORY, method = RequestMethod.GET)
    public String handleInventory(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        final HttpSessionWrapper wrapper = getWrapper(request);
        final PlayerUser player = wrapper.getPlayer();
        final BookInformations info = getInfo();
        sectionHandlingService.initModel(model, player, info);

        final Character c = wrapper.getCharacter();
        model.addAttribute("charEquipments", getCharacterPageData(c));
        model.addAttribute("bookInfo", info);

        return "rawCharPage";
    }

    @Override
    public RawCharacterPageData getCharacterPageData(final Character character) {
        return (RawCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character);
    }

    /**
     * Saves modifications in the notes.
     * @param notes the modified notes
     * @param request the {@link HttpServletRequest} bean
     */
    @RequestMapping(value = PageAddresses.BOOK_INVENTORY + "/notes", method = RequestMethod.POST)
    @ResponseBody
    public void saveNotes(final HttpServletRequest request, @Qualifier("notes") final String notes) {
        final Character c = getWrapper(request).getCharacter();
        c.setNotes(notes);
    }
}
