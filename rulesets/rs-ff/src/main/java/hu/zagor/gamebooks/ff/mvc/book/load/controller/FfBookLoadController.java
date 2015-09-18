package hu.zagor.gamebooks.ff.mvc.book.load.controller;

import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.mvc.book.load.controller.GenericBookLoadController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.load.controller.RawBookLoadController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

/**
 * Generic load controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookLoadController extends RawBookLoadController {

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookLoadController}.
     * @param sectionHandlingService he {@link SectionHandlingService} that will handle the section changing
     */
    public FfBookLoadController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandlerObject) {
        super.setUpCharacterHandler(wrapper, characterHandlerObject);

        final FfCharacterHandler characterHandler = (FfCharacterHandler) characterHandlerObject;
        characterHandler.getEnemyHandler().setEnemies(wrapper.getEnemies());
    }

    @Override
    protected String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer) {
        addJsResource(model, "ff");
        addCssResource(model, "ff");
        return super.doLoad(model, request, savedGameContainer);
    }

    @Override
    public FfCharacterPageData getCharacterPageData(final Character character) {
        return (FfCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }
}
