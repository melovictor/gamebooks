package hu.zagor.gamebooks.raw.mvc.book.section.service;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.GenericSectionHandlingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * Class for handling the loading and initializing the sections for the books without rules.
 * @author Tamas_Szekeres
 */
public class RawBookSectionHandlingService extends GenericSectionHandlingService {

    /**
     * Basic constructor that passes the received {@link BookContentInitializer} bean to it's parent class' constructor.
     * @param contentInitializer the {@link BookContentInitializer} bean to use for initializing the model
     */
    @Autowired
    public RawBookSectionHandlingService(final BookContentInitializer contentInitializer) {
        super(contentInitializer);
    }

    @Override
    public String handleSection(final Model model, final HttpSessionWrapper wrapper, final Paragraph paragraph, final BookInformations info) {
        Assert.notNull(model, "The parameter 'model' cannot be null!");
        Assert.notNull(wrapper, "The parameter 'wrapper' cannot be null!");
        Assert.notNull(info, "The parameter 'info' cannot be null!");

        initModel(model, wrapper.getPlayer(), info);

        String tileName = getPageTileName(info);

        final Character character = wrapper.getCharacter();
        final CommandView commandView = character.getCommandView();
        if (commandView != null) {
            model.addAllAttributes(commandView.getModel());
            tileName = getCommandView(commandView, info);
        }

        return checkParagraph(model, wrapper.setParagraph(paragraph), tileName, info);
    }

    @Override
    protected String getCommandView(final CommandView commandView, final BookInformations info) {
        return commandView.getViewName();
    }

    /**
     * Returns the name of the tile page to use for displaying the sections.
     * @param info the {@link BookInformations} object
     * @return the tile's name
     */
    @Override
    protected String getPageTileName(final BookInformations info) {
        return "rawSection";
    }

    @Override
    public String checkParagraph(final Model model, final Paragraph paragraph, final String tile, final BookInformations info) {
        Assert.notNull(model, "The parameter 'model' cannot be null!");
        Assert.notNull(tile, "The parameter 'tile' cannot be null!");
        Assert.notNull(info, "The parameter 'info' cannot be null!");

        final String response;
        if (paragraph != null) {
            model.addAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY, paragraph);
            model.addAttribute("bookInfo", info);
            response = tile;
        } else {
            model.addAttribute("problemCode", "page.book.paragraph.missing");
            response = "bookError";
        }
        return response;
    }

}
