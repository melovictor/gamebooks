package hu.zagor.gamebooks.ff.mvc.book.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic new game handling controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookNewGameController extends RawBookNewGameController {

    @Override
    public String handleNew(final HttpServletRequest request, final Model model, final Locale locale) {
        super.handleNew(request, model, locale);

        model.addAttribute("ffChoiceClass", "ffChoiceHidden");
        addJsResource(model, "ff");
        addCssResource(model, "ff");

        return "ffSection." + getInfo().getResourceDir();
    }

    @Override
    protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandlerObject) {
        super.setUpCharacterHandler(wrapper, characterHandlerObject);

        final FfCharacterHandler characterHandler = (FfCharacterHandler) characterHandlerObject;
        characterHandler.getEnemyHandler().setEnemies(wrapper.getEnemies());
    }

    @Override
    public FfCharacterPageData getCharacterPageData(final Character character) {
        return (FfCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    /**
     * Handles the generation of the new character.
     * @param request the {@link HttpServletRequest} object
     * @return the compiled object
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/" + PageAddresses.BOOK_GENERATE_CHARACTER, produces = "application/json")
    @ResponseBody
    public Map<String, Object> generateCharacter(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final CharacterGenerator characterGenerator = getInfo().getCharacterHandler().getCharacterGenerator();
        final Map<String, Object> result = characterGenerator.generateCharacter(character, getInfo().getContentSpecification());

        initializeItems(request.getParameterMap(), character);

        return result;
    }

    /**
     * Method for processing the extra parameters received from the browser. The default implementation simply takes care of the potions.
     * @param parameterMap the parameters received
     * @param character the {@link FfCharacter} to initialize
     */
    protected void initializeItems(final Map<String, String[]> parameterMap, final FfCharacter character) {
        if (parameterMap.containsKey("potion")) {
            final String potionId = parameterMap.get("potion")[0];
            getInfo().getCharacterHandler().getItemHandler().addItem(character, potionId, 1);
        }
    }

    @Override
    protected BookParagraphConstants getStarterParagraph() {
        return BookParagraphConstants.GENERATE;
    }

    @Override
    public FfBookInformations getInfo() {
        return (FfBookInformations) super.getInfo();
    }

}
