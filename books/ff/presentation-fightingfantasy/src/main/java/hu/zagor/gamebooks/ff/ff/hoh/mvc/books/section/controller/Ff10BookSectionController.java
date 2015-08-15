package hu.zagor.gamebooks.ff.ff.hoh.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.HOUSE_OF_HELL)
public class Ff10BookSectionController extends FfBookSectionController {

    private static final int LOW_TOTAL_LIMIT = 0;
    private static final int MID_TOTAL_LIMIT = 8;
    private static final int HIGH_TOTAL_LIMIT = 12;

    private static final int TORTURER_POINT_LOWER = 3;
    private static final int TORTURER_POINT_HIGHER = 5;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff10BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final String sectionId = paragraph.getId();
        if ("280".equals(sectionId)) {
            int total = 0;
            total = add("4002", TORTURER_POINT_LOWER, total, character);
            total = add("4003", TORTURER_POINT_HIGHER, total, character);
            total = add("4004", TORTURER_POINT_LOWER, total, character);
            total = add("4005", TORTURER_POINT_HIGHER, total, character);
            total = add("4006", TORTURER_POINT_LOWER, total, character);
            total = add("4007", TORTURER_POINT_LOWER, total, character);
            total = add("4008", TORTURER_POINT_HIGHER, total, character);
            total = add("4009", TORTURER_POINT_LOWER, total, character);
            total = add("4010", TORTURER_POINT_LOWER, total, character);
            total = add("4011", TORTURER_POINT_HIGHER, total, character);
            total = add("4012", TORTURER_POINT_HIGHER, total, character);
            total = add("4013", TORTURER_POINT_LOWER, total, character);
            final ChoiceSet choices = paragraph.getData().getChoices();
            if (total < MID_TOTAL_LIMIT) {
                choices.removeByPosition(MID_TOTAL_LIMIT);
                choices.removeByPosition(HIGH_TOTAL_LIMIT);
            } else if (total < HIGH_TOTAL_LIMIT) {
                choices.removeByPosition(LOW_TOTAL_LIMIT);
                choices.removeByPosition(HIGH_TOTAL_LIMIT);
            } else {
                choices.removeByPosition(LOW_TOTAL_LIMIT);
                choices.removeByPosition(MID_TOTAL_LIMIT);
            }
        }
    }

    private int add(final String shadowId, final int increment, final int total, final FfCharacter character) {
        int newTotal = total;
        final CharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, shadowId)) {
            newTotal += increment;
        }
        return newTotal;
    }
}
