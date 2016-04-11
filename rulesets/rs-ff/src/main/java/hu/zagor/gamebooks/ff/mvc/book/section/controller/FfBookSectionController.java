package hu.zagor.gamebooks.ff.mvc.book.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestDecision;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfBookPreFightHandlingService;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Generic section selection controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookSectionController extends AbstractFfBookSectionController {

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public FfBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    /**
     * Handler for attribute test testing entry points.
     * @param model the data model
     * @param request the request
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.ATTRIBUTE_TEST_TESTING)
    public final String handleAttributeTestTesting(final Model model, final HttpServletRequest request) {
        return doHandleAttributeTestTesting(model, request);
    }

    /**
     * Handler for actually executing the attribute test.
     * @param model the data model
     * @param request the request
     * @return the book page's name
     */
    protected String doHandleAttributeTestTesting(final Model model, final HttpServletRequest request) {
        getInfo().getCharacterHandler().getInteractionHandler().setAttributeTestResult((FfCharacter) getWrapper(request).getCharacter(), AttributeTestDecision.TEST);
        return super.handleSection(model, request, null);
    }

    /**
     * Handler for attribute test skipping entry points.
     * @param model the data model
     * @param request the request
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.ATTRIBUTE_TEST_SKIPPING)
    public final String handleAttributeTestSkipping(final Model model, final HttpServletRequest request) {
        getInfo().getCharacterHandler().getInteractionHandler().setAttributeTestResult((FfCharacter) getWrapper(request).getCharacter(), AttributeTestDecision.SKIP);
        return super.handleSection(model, request, null);
    }

    /**
     * Handler for fight entry points.
     * @param model the data model
     * @param request the request
     * @param enemyId the id of the enemy to fight
     * @param luckOnHit should a luck test be executed on successful attack
     * @param luckOnDefense should a luck test be executed on a failed defense
     * @param luckOnOther should a luck test be executed on other attacks (eg. when there is an extra attack at the beginning of the round)
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.FIGHT)
    public final String handleFight(final Model model, final HttpServletRequest request, @RequestParam("id") final String enemyId,
        @RequestParam("hit") final Boolean luckOnHit, @RequestParam("def") final Boolean luckOnDefense, @RequestParam("oth") final Boolean luckOnOther) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        handleBeforeFight(wrapper, enemyId);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        final FfUserInteractionHandler interactionHandler = getInfo().getCharacterHandler().getInteractionHandler();

        prepareFight(wrapper);
        interactionHandler.setFightCommand(character, LastFightCommand.ENEMY_ID, enemyId);
        interactionHandler.setFightCommand(character, LastFightCommand.LUCK_ON_HIT, luckOnHit.toString());
        interactionHandler.setFightCommand(character, LastFightCommand.LUCK_ON_DEFENSE, luckOnDefense.toString());
        interactionHandler.setFightCommand(character, LastFightCommand.LUCK_ON_OTHER, luckOnOther.toString());
        getInteractionRecorder().prepareFightCommand(wrapper, luckOnHit, luckOnDefense, luckOnOther);
        getInteractionRecorder().recordFightCommand(wrapper, enemyId);

        final String handleSection = super.handleSection(model, request, null);

        handleAfterFight(wrapper, enemyId);
        model.addAttribute("charEquipments", getCharacterPageData(character));

        return handleSection;
    }

    @Override
    protected void handleBeforeFight(final HttpSessionWrapper wrapper, final String enemyId) {
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
    }

    /**
     * Custom fight handler method for future books to add their own preparation.
     * @param wrapper the {@link HttpSessionWrapper} object
     */
    protected void prepareFight(final HttpSessionWrapper wrapper) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        getInfo().getCharacterHandler().getInteractionHandler().setFightCommand(character, FightCommand.ATTACKING);
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
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        getInfo().getCharacterHandler().getInteractionHandler().setFightCommand(character, FightCommand.FLEEING);

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
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        getInteractionRecorder().recordMarketClosing(wrapper);

        getInfo().getCharacterHandler().getInteractionHandler().setMarketCommand(character);
        return super.handleSection(model, request, null);
    }

    @Override
    public FfCharacterPageData getCharacterPageData(final Character character) {
        return (FfCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    @Override
    protected void addResources(final Model model) {
        super.addResources(model);
        addJsResource(model, "ff");
        addCssResource(model, "ff");
    }

    /**
     * Entry point for handling usage/consumption of pre-fight items.
     * @param model the {@link Model} object
     * @param request the {@link HttpServletRequest} item
     * @param itemId the id of the item to be used
     * @param enemyId the id of the enemy that is currently selected
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.PRE_FIGHT_ITEM_USAGE)
    public final String preFightHandler(final Model model, final HttpServletRequest request, @RequestParam("id") final String itemId,
        @RequestParam("enemyId") final String enemyId) {
        final FfBookInformations info = getInfo();
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        getInfo().getCharacterHandler().getInteractionHandler().setFightCommand(character, LastFightCommand.ENEMY_ID, enemyId);
        final FfItem preFightItem = getBeanFactory().getBean(fetchBookIdByReflection() + "BookPreFightHandlingService", FfBookPreFightHandlingService.class)
            .handlePreFightItemUsage(info, wrapper, itemId);
        if (preFightItem != null) {
            preFightItem.setUsedInPreFight(true);
        }
        return super.handleSection(model, request, null);
    }

    @Override
    public FfBookInformations getInfo() {
        return (FfBookInformations) super.getInfo();
    }

}
