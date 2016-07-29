package hu.zagor.gamebooks.lw.mvc.book.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.LwUserInteractionHandler;
import hu.zagor.gamebooks.content.LwParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.fight.ComplexFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.controller.session.LwHttpSessionWrapper;
import hu.zagor.gamebooks.domain.LwBookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.LwCharacterPageData;
import hu.zagor.gamebooks.lw.mvc.book.section.domain.LwFightCommandForm;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.section.controller.RawBookSectionController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Generic section selection controller for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwBookSectionController extends RawBookSectionController {
    private static final int MEAL_MISSOUT_DEDUCTION = -3;

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public LwBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapperObject, final boolean changedSection) {
        final LwHttpSessionWrapper wrapper = (LwHttpSessionWrapper) wrapperObject;
        if (changedSection) {
            final Paragraph previousParagraph = wrapper.getPreviousParagraph();
            final LwParagraphData data = (LwParagraphData) previousParagraph.getData();
            if (data.isMustEat()) {
                final LwCharacter character = wrapper.getCharacter();
                if (!character.getKaiDisciplines().isHunting() || !character.isHuntEnabled()) {
                    getInfo().getCharacterHandler().getAttributeHandler().handleModification(character, "endurance", MEAL_MISSOUT_DEDUCTION);
                }
            }
        }

        super.handleCustomSectionsPre(model, wrapper, changedSection);
    }

    @Override
    protected Paragraph loadSection(final String paragraphId, final HttpServletRequest request) {
        final LwHttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        wrapper.setPreviousParagraph(paragraph);
        return super.loadSection(paragraphId, request);
    }

    @Override
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        super.handleCustomSectionsPost(model, wrapper, changedSection);

        final Paragraph paragraph = wrapper.getParagraph();
        final LwParagraphData data = (LwParagraphData) paragraph.getData();
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();
        if (paragraph.getItemsToProcess().isEmpty() && !data.isFought() && character.getKaiDisciplines().isHealing()) {
            final int lostInBattle = character.getEnduranceLostInCombat();
            if (lostInBattle > 0) {
                character.setEnduranceLostInCombat(lostInBattle - 1);
                getInfo().getCharacterHandler().getAttributeHandler().handleModification(character, "endurance", 1);
            }
        }
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

    /**
     * Handler for fleeing entry points.
     * @param model the data model
     * @param request the request
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.FLEE)
    public final String handleFlee(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();

        getInfo().getCharacterHandler().getInteractionHandler().setFightCommand(character, LwFightCommand.FLEEING);

        return super.handleSection(model, request, null);
    }

    /**
     * Handles the closing of the market.
     * @param model the data model
     * @param request the request
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.BOOK_MARKET_CLOSE)
    public final String handleMarketClose(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();

        getInfo().getCharacterHandler().getInteractionHandler().setMarketCommand(character);
        return super.handleSection(model, request, null);
    }

    @Override
    public LwBookInformations getInfo() {
        return (LwBookInformations) super.getInfo();
    }

    @Override
    protected LwHttpSessionWrapper getWrapper(final HttpServletRequest request) {
        Assert.notNull(request, "The parameter 'request' cannot be null!");
        return (LwHttpSessionWrapper) getBeanFactory().getBean("lwHttpSessionWrapper", request);
    }
}
