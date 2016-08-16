package hu.zagor.gamebooks.lw.mvc.book.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterContinuator;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.LwCharacterPageData;
import hu.zagor.gamebooks.lw.domain.LwBookInformations;
import hu.zagor.gamebooks.lw.mvc.book.newgame.domain.LwCharGenInput;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic new game handling controller for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwBookNewGameController extends RawBookNewGameController {

    @Override
    protected String doHandleNew(final HttpServletRequest request, final Model model, final Locale locale) {
        super.doHandleNew(request, model, locale);

        model.addAttribute("lwChoiceClass", "choiceHidden");
        addJsResource(model, "lw");
        addCssResource(model, "lw");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final Set<String> rewards = wrapper.getPlayer().getRewards().get(getInfo().getId());
        if (rewards != null) {
            model.addAttribute("earnedRewards", rewards.size());
            final ParagraphData data = wrapper.getParagraph().getData();
            final String text = data.getText();

            data.setText(text);
        }

        return "lwSection." + getInfo().getResourceDir();
    }

    @Override
    public LwCharacterPageData getCharacterPageData(final Character character) {
        return (LwCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    /**
     * Handles the generation of the new character.
     * @param request the {@link HttpServletRequest} object
     * @param input bean containing all user selections for the character to be generated
     * @return the compiled object
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/" + PageAddresses.BOOK_GENERATE_CHARACTER, produces = "application/json")
    @ResponseBody
    public final Map<String, Object> generateCharacter(final HttpServletRequest request, @ModelAttribute final LwCharGenInput input) {
        return doGenerateCharacter(request, input);
    }

    /**
     * Handles the actual generation of the new character.
     * @param request the {@link HttpServletRequest} object
     * @param input bean containing all user selections for the character to be generated
     * @return the compiled object
     */
    protected Map<String, Object> doGenerateCharacter(final HttpServletRequest request, final LwCharGenInput input) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();
        final LwBookInformations info = getInfo();
        final CharacterGenerator characterGenerator = info.getCharacterHandler().getCharacterGenerator();
        final Map<String, Object> result = characterGenerator.generateCharacter(character, info, input);

        return result;
    }

    /**
     * Handles the continuation of a character taken over from the previous book.
     * @param request the {@link HttpServletRequest} object
     * @param input bean containing all user selections for the character to be continued
     * @return the compiled object
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/continue", produces = "application/json")
    @ResponseBody
    public final Map<String, Object> continueCharacter(final HttpServletRequest request, @ModelAttribute final LwCharGenInput input) {
        return doContinueCharacter(request, input);
    }

    /**
     * Handles the actual continuation of the new character.
     * @param request the {@link HttpServletRequest} object
     * @param input bean containing all user selections for the character to be continued
     * @return the compiled object
     */
    protected Map<String, Object> doContinueCharacter(final HttpServletRequest request, final LwCharGenInput input) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();
        final LwBookInformations info = getInfo();
        final CharacterContinuator characterContinuator = info.getCharacterHandler().getCharacterContinuator();
        final Map<String, Object> result = characterContinuator.continueCharacter(character, info, input);

        return result;
    }

    @Override
    protected BookParagraphConstants getStarterParagraph() {
        return BookParagraphConstants.GENERATE;
    }

    @Override
    public LwBookInformations getInfo() {
        return (LwBookInformations) super.getInfo();
    }

}
