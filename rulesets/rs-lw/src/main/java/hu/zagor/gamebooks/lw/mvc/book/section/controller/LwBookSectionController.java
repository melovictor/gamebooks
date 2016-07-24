package hu.zagor.gamebooks.lw.mvc.book.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.LwUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.LwBookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.LwCharacterPageData;
import hu.zagor.gamebooks.lw.mvc.book.section.domain.LwFightCommandForm;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.section.controller.RawBookSectionController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Generic section selection controller for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwBookSectionController extends RawBookSectionController {
    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public LwBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    public LwCharacterPageData getCharacterPageData(final Character character) {
        return (LwCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    @Override
    protected void addResources(final Model model) {
        super.addResources(model);
        addJsResource(model, "lw");
        addCssResource(model, "lw");
    }

    /**
     * Handler for fight entry points.
     * @param model the data model
     * @param request the request
     * @param form the form containing all incoming data
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.FIGHT)
    public final String handleFight(final Model model, final HttpServletRequest request, @ModelAttribute final LwFightCommandForm form) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();

        final LwUserInteractionHandler interactionHandler = getInfo().getCharacterHandler().getInteractionHandler();

        interactionHandler.setFightCommand(character, LastFightCommand.ENEMY_ID, form.getId());
        interactionHandler.setFightCommand(character, ComplexFightCommand.ATTACKING);

        final String handleSection = super.handleSection(model, request, null);

        model.addAttribute("data", getCharacterPageData(character));

        return handleSection;
    }

    @Override
    public LwBookInformations getInfo() {
        return (LwBookInformations) super.getInfo();
    }
}
