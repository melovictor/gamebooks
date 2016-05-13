package hu.zagor.gamebooks.raw.mvc.book.load.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.item.DefaultItemFactory;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.load.controller.GenericBookLoadController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.controller.CharacterPageDisplayingController;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * Generic load controller for books with no rule system.
 * @author Tamas_Szekeres
 */
public class RawBookLoadController extends GenericBookLoadController implements CharacterPageDisplayingController {

    @Autowired private BookContentInitializer contentInitializer;

    private final SectionHandlingService sectionHandlingService;

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookLoadController}.
     * @param sectionHandlingService he {@link SectionHandlingService} that will handle the section changing
     */
    public RawBookLoadController(final SectionHandlingService sectionHandlingService) {
        Assert.notNull(sectionHandlingService, "The parameter 'sectionHandlingService' cannot be null!");
        this.sectionHandlingService = sectionHandlingService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer) {
        Assert.notNull(model, "The parameter 'model' cannot be null!");
        Assert.notNull(request, "The parameter 'request' cannot be null!");
        Assert.notNull(savedGameContainer, "The parameter 'savedGameContainer' cannot be null!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final Character character = (Character) savedGameContainer.getElement(ControllerAddresses.CHARACTER_STORE_KEY);
        final Map<String, Enemy> savedEnemies = (Map<String, Enemy>) savedGameContainer.getElement(ControllerAddresses.ENEMY_STORE_KEY);
        final Map<String, Enemy> enemies = wrapper.getEnemies();
        enemies.clear();
        final BookInformations info = getInfo();
        final BookItemStorage itemStorage = contentInitializer.getItemStorage(info);
        enemies.putAll(itemStorage.getEnemies());
        enemies.putAll(savedEnemies);

        final CharacterHandler characterHandler = info.getCharacterHandler();
        model.addAttribute("charEquipments", getCharacterPageData(character));

        setUpCharacterHandler(wrapper, characterHandler);
        wrapper.setCharacter(character);

        final PlayerUser player = wrapper.getPlayer();
        final Paragraph paragraph = (Paragraph) savedGameContainer.getElement(ControllerAddresses.PARAGRAPH_STORE_KEY);
        setUpSectionDisplayOptions(paragraph, model, player);
        addJsResource(model, "raw");
        addCssResource(model, "raw");

        return sectionHandlingService.handleSection(model, wrapper, paragraph, info);
    }

    @Override
    protected void doLoadPrevious(final HttpSessionWrapper wrapper, final SavedGameContainer savedGameContainer) {
        Assert.notNull(wrapper, "The parameter 'wrapper' cannot be null!");
        Assert.notNull(savedGameContainer, "The parameter 'savedGameContainer' cannot be null!");

        final Character character = (Character) savedGameContainer.getElement(ControllerAddresses.CHARACTER_STORE_KEY);
        wrapper.setCharacter(character);
        doContinuePrevious(wrapper);
    }

    @Override
    protected void doContinuePrevious(final HttpSessionWrapper wrapper) {
        Assert.notNull(wrapper, "The parameter 'wrapper' cannot be null!");
        final Map<String, Enemy> enemies = wrapper.getEnemies();
        enemies.clear();
        final BookInformations info = getInfo();
        final BookItemStorage itemStorage = contentInitializer.getItemStorage(info);
        enemies.putAll(itemStorage.getEnemies());

        setUpCharacterHandler(wrapper, info.getCharacterHandler());
    }

    @Override
    protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandler) {
        final DefaultItemFactory itemFactory = (DefaultItemFactory) getBeanFactory().getBean("defaultItemFactory", getInfo());
        characterHandler.getItemHandler().setItemFactory(itemFactory);
    }

    @Override
    public RawCharacterPageData getCharacterPageData(final Character character) {
        return (RawCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character);
    }

}
