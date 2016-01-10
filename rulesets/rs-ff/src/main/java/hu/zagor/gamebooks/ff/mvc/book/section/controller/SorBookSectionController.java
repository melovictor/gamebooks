package hu.zagor.gamebooks.ff.mvc.book.section.controller;

import hu.zagor.gamebooks.books.saving.xml.XmlGameStateSaver;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

/**
 * Generic section handler for Sorcery books.
 * @author Tamas_Szekeres
 */
public class SorBookSectionController extends FfBookSectionController {
    private static final int POSITION_FOR_ONES = 10;
    @Autowired private XmlGameStateSaver gameStateSaver;

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public SorBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        saveCharacterIfNecessary(character, paragraph.getId());
        character.setLastEatenBonus(0);
    }

    private void saveCharacterIfNecessary(final SorCharacter character, final String sectionIdentifier) {
        final String bookNumber = String.valueOf(getInfo().getId() % POSITION_FOR_ONES);
        final String identifier = bookNumber + "-" + sectionIdentifier;
        final Map<String, String> characterSaveLocations = character.getCharacterSaveLocations();
        if (characterSaveLocations.containsKey(identifier)) {
            final String savedCharacter = gameStateSaver.save(character);
            characterSaveLocations.put(identifier, savedCharacter);
        }
    }

    @Override
    protected void resolveChoiceDisplayNames(final Paragraph paragraph) {
        super.resolveChoiceDisplayNames(paragraph);

        final SorParagraphData data = (SorParagraphData) paragraph.getData();
        final List<Choice> choices = data.getSpellChoices();
        for (final Choice choice : choices) {
            resolveSingleChoiceDisplayName(choice);
        }
    }

}
