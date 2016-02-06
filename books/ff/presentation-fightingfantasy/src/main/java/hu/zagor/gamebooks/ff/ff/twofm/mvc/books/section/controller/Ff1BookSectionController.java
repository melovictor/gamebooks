package hu.zagor.gamebooks.ff.ff.twofm.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
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

    private static final int CHOICE_VAMPIRE_GAZE = 3;
    private static final int CHOICE_CONTINUE_FIGHT = 2;
    private static final int VAMPIRE_GAZE_BOUNDARY = 11;
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
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        final Paragraph paragraph = wrapper.getParagraph();
        final String id = paragraph.getId();
        if (OLD_MAN_DICING.equals(id) && hasNoBets(wrapper)) {
            final ItemCheckCommand itemCheckCommand = (ItemCheckCommand) wrapper.getParagraph().getData().getCommands().get(0);
            final UserInputCommand userInputCommand = (UserInputCommand) itemCheckCommand.getHave().getCommands().get(0);
            final UserInputNumericResponse userInputResponse = (UserInputNumericResponse) userInputCommand.getResponses().get(0);
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            userInputResponse.setMaxBound(Math.min(character.getGold(), MAX_BET));
        } else if ("55".equals(id)) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final FfAttributeHandler attributeHandler = getInfo().getCharacterHandler().getAttributeHandler();
            final int stamina = attributeHandler.resolveValue(character, "stamina");
            final int luck = attributeHandler.resolveValue(character, "luck");
            final AttributeTestCommand testCommand = (AttributeTestCommand) wrapper.getParagraph().getData().getCommands().get(0);
            testCommand.setAgainstNumeric(Math.min(stamina, luck));
        } else if ("109".equals(id)) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final FfAttributeHandler attributeHandler = getInfo().getCharacterHandler().getAttributeHandler();
            final int stamina = attributeHandler.resolveValue(character, "stamina");
            final int initStamina = attributeHandler.resolveValue(character, "initialStamina");
            gain(character, "stamina", initStamina - 2 - stamina, attributeHandler);
            final int skill = attributeHandler.resolveValue(character, "skill");
            final int initSkill = attributeHandler.resolveValue(character, "initialSkill");
            gain(character, "skill", initSkill - 1 - skill, attributeHandler);
        }
    }

    private void gain(final FfCharacter character, final String attribute, final int change, final FfAttributeHandler attributeHandler) {
        if (change > 0) {
            attributeHandler.handleModification(character, attribute, change);
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

    @Override
    public String handleAttributeTest(final Model model, final HttpServletRequest request) {
        final Paragraph paragraph = getWrapper(request).getParagraph();
        final AttributeTestCommand testCommand = (AttributeTestCommand) paragraph.getData().getCommands().get(0);

        final String testResult = super.handleAttributeTest(model, request);

        if ("333a".equals(paragraph.getId())) {
            final int result = testCommand.getResult();
            if (result >= VAMPIRE_GAZE_BOUNDARY) {
                paragraph.getData().getChoices().removeByPosition(CHOICE_CONTINUE_FIGHT);
            } else {
                paragraph.getData().getChoices().removeByPosition(CHOICE_VAMPIRE_GAZE);
            }
        }

        return testResult;
    }
}
