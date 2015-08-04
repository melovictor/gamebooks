package hu.zagor.gamebooks.mvc.book.section.service;

import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;

import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * Class for handling the loading and initializing the sections for the different book rules.
 * @author Tamas_Szekeres
 */
public abstract class GenericSectionHandlingService implements SectionHandlingService {

    private final BookContentInitializer contentInitializer;

    /**
     * Basic constructor that expects a {@link BookContentInitializer} bean to be passed to this bean.
     * @param contentInitializer the {@link BookContentInitializer} used to initialize the model
     */
    public GenericSectionHandlingService(final BookContentInitializer contentInitializer) {
        Assert.notNull(contentInitializer, "The parameter 'contentInitializer' cannot be null!");
        this.contentInitializer = contentInitializer;
    }

    /**
     * Abstract method for getting the name of the page title for the sections.
     * @param info the {@link BookInformations} bean in case it becomes necessary
     * @return the tile name of the section for the given book
     */
    protected abstract String getPageTileName(final BookInformations info);

    /**
     * Abstract method for getting the name of the page title for the sections with a command view on it.
     * @param commandView the {@link CommandView} object
     * @param info the {@link BookInformations} bean in case it becomes necessary
     * @return the tile name of the section for the given book
     */
    protected abstract String getCommandView(final CommandView commandView, final BookInformations info);

    @Override
    public void initModel(final Model model, final PlayerUser player, final BookInformations info) {
        Assert.notNull(model, "The parameter 'model' cannot be null!");
        Assert.notNull(player, "The parameter 'player' cannot be null!");
        Assert.notNull(info, "The parameter 'info' cannot be null!");
        contentInitializer.initModel(model, player, info);
    }

    @Override
    public String resolveParagraphId(final BookInformations info, final String paragraphId) {
        Assert.notNull(info, "The parameter 'info' cannot be null!");
        Assert.notNull(paragraphId, "The parameter 'paragraphId' cannot be null!");
        final BookItemStorage itemStorage = contentInitializer.getItemStorage(info);
        return itemStorage.resolveParagraphId(paragraphId);
    }
}
