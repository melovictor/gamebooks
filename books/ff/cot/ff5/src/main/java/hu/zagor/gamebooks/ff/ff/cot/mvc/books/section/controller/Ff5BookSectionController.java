package hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service.BallThrowChallenge;
import hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service.WhoThrowsHigherService;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.CITY_OF_THIEVES)
public class Ff5BookSectionController extends FfBookSectionController {

    @Autowired
    private WhoThrowsHigherService whoThrowsHigher;
    @Autowired
    private BallThrowChallenge ballThrowChallenge;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff5BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        if (isWhoThrowsHigher(paragraph)) {
            handleWhoThrowsHigher(wrapper);
        } else if (throwTheBall(paragraph)) {
            doThrowTheBall(wrapper);
        }
    }

    private void doThrowTheBall(final HttpSessionWrapper wrapper) {
        ballThrowChallenge.playGame((FfCharacter) wrapper.getCharacter(), wrapper.getParagraph().getData());
    }

    private boolean throwTheBall(final Paragraph paragraph) {
        return "378".equals(paragraph.getId());
    }

    private void handleWhoThrowsHigher(final HttpSessionWrapper wrapper) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final CharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "42063")) {
            itemHandler.addItem(character, "42064", 1);
        } else if (itemHandler.hasItem(character, "42062")) {
            itemHandler.addItem(character, "42063", 1);
        } else if (itemHandler.hasItem(character, "42061")) {
            itemHandler.addItem(character, "42062", 1);
        } else {
            itemHandler.addItem(character, "42061", 1);
        }
        whoThrowsHigher.playGame(character, wrapper.getParagraph().getData());
    }

    private boolean isWhoThrowsHigher(final Paragraph paragraph) {
        return "206a".equals(paragraph.getId());
    }

}
