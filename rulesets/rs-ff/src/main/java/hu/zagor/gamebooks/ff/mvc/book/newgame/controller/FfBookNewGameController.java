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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic new game handling controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookNewGameController extends RawBookNewGameController {

    @Override
    public String handleNew(final HttpSession session, final Model model, final Locale locale) {
        super.handleNew(session, model, locale);

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
    public FfCharacterPageData getCharacterPageData(final Character character, final CharacterHandler handler) {
        return (FfCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, handler);
    }

    /**
     * Handles the generation of the new character.
     * @param request the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} stream
     * @throws IOException when an error occurs during the writing into the output stream
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/" + PageAddresses.BOOK_GENERATE_CHARACTER, produces = "application/json; charset=utf-8")
    @ResponseBody
    public void generateCharacter(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final CharacterGenerator characterGenerator = getInfo().getCharacterHandler().getCharacterGenerator();
        final Map<String, Object> result = characterGenerator.generateCharacter(character, getInfo().getContentSpecification());

        initializeItems(request.getParameterMap(), character);

        final PrintWriter writer = response.getWriter();
        final String jsonString = JSONObject.toJSONString(result);
        writer.write(jsonString);
        writer.close();
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
