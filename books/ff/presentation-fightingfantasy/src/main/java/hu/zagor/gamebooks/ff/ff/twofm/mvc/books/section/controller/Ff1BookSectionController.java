package hu.zagor.gamebooks.ff.ff.twofm.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputNumericResponse;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.section.domain.UserInputResponseForm;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_WARLOCK_OF_FIRETOP_MOUNTAIN)
public class Ff1BookSectionController extends FfBookSectionController {

    private static final int MAX_BET = 20;
    private static final String OLD_MAN_DICING = "130";
    private static final String OUR_ROLL = "ourRoll";
    private static final String CURRENT_BET = "currentBet";

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff1BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        if (OLD_MAN_DICING.equals(paragraph.getId()) && hasNoBets(wrapper)) {
            final ItemCheckCommand itemCheckCommand = (ItemCheckCommand) wrapper.getParagraph().getData().getCommands().get(0);
            final UserInputCommand userInputCommand = (UserInputCommand) itemCheckCommand.getHave().getCommands().get(0);
            final UserInputNumericResponse userInputResponse = (UserInputNumericResponse) userInputCommand.getResponses().get(0);
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            userInputResponse.setMaxBound(Math.min(character.getGold(), MAX_BET));
        }
    }

    private boolean hasNoBets(final HttpSessionWrapper wrapper) {
        return getInfo().getCharacterHandler().getInteractionHandler().peekInteractionState(wrapper.getCharacter(), CURRENT_BET) == null;
    }

    @Override
    public String handleRandom(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final RandomCommand command = (RandomCommand) wrapper.getParagraph().getData().getCommands().get(0);
        final String paragraphId = getParagraphId(wrapper);

        final String rollResult = super.handleRandom(model, request);

        if (OLD_MAN_DICING.equals(paragraphId)) {
            if (isFirst(wrapper)) {
                storeOurRoll(wrapper, command);
            } else {
                calculateWinLoss(wrapper, command, model);
            }
        }

        return rollResult;
    }

    private boolean isFirst(final HttpSessionWrapper wrapper) {
        return getInfo().getCharacterHandler().getInteractionHandler().peekInteractionState(wrapper.getCharacter(), OUR_ROLL) == null;
    }

    private void calculateWinLoss(final HttpSessionWrapper wrapper, final RandomCommand command, final Model model) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final int oldManRoll = command.getDiceResult();
        final FfUserInteractionHandler interactionHandler = getInfo().getCharacterHandler().getInteractionHandler();
        final int ourRoll = Integer.parseInt(interactionHandler.getInteractionState(character, OUR_ROLL));
        final int bet = Integer.parseInt(interactionHandler.getInteractionState(character, CURRENT_BET));
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        String keyPostfix;
        if (ourRoll == oldManRoll) {
            keyPostfix = "tie";
        } else if (ourRoll > oldManRoll) {
            characterHandler.getItemHandler().addItem(character, "4005", 1);
            characterHandler.getAttributeHandler().handleModification(character, "gold", bet);
            keyPostfix = "win";
        } else {
            characterHandler.getAttributeHandler().handleModification(character, "gold", -bet);
            keyPostfix = "lose";
        }
        final String key = "page.ff1.label.oldManDice." + keyPostfix;
        wrapper.getParagraph().getData().appendText("[p]" + key + "[/p]");
        model.addAttribute("charEquipments", getCharacterPageData(character));
    }

    private void storeOurRoll(final HttpSessionWrapper wrapper, final RandomCommand command) {
        getInfo().getCharacterHandler().getInteractionHandler().setInteractionState(wrapper.getCharacter(), OUR_ROLL, String.valueOf(command.getDiceResult()));
    }

    private String getParagraphId(final HttpSessionWrapper wrapper) {
        final Paragraph paragraph = wrapper.getParagraph();
        return paragraph.getId();
    }

    @Override
    public String handleUserInput(final UserInputResponseForm form, final Model model, final HttpServletRequest request) throws UnsupportedEncodingException {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final String paragraphId = getParagraphId(wrapper);
        if (OLD_MAN_DICING.equals(paragraphId)) {
            storeBetAmount(wrapper, form);
        }

        return super.handleUserInput(form, model, request);
    }

    private void storeBetAmount(final HttpSessionWrapper wrapper, final UserInputResponseForm form) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final int maxBet = character.getGold();
        try {
            final int actualBet = Integer.parseInt(form.getResponseText());
            if (actualBet <= maxBet) {
                getInfo().getCharacterHandler().getInteractionHandler().setInteractionState(character, CURRENT_BET, String.valueOf(actualBet));
            }
        } catch (final NumberFormatException ex) {
            getLogger().warn("User provided invalid amount '{}' as the bet. Game will be finished.", form.getResponseText());
        }
    }
}
