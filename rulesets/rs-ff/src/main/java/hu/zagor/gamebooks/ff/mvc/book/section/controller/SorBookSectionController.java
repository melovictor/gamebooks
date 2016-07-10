package hu.zagor.gamebooks.ff.mvc.book.section.controller;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.SorMagicChainPreparatorService;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Generic section handler for Sorcery books.
 * @author Tamas_Szekeres
 */
public class SorBookSectionController extends FfBookSectionController {
    private static final int SPELL_POSITION = 999;
    private static final String CURSED_CHAINMAIL_GLOVES = "3042";
    private static final int POSITION_FOR_ONES = 10;
    @Autowired private SorMagicChainPreparatorService magicChainService;

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public SorBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    /**
     * Handles the spell casting navigation.
     * @param model the {@link Model} object
     * @param request the {@link HttpServletRequest} object
     * @param spellTarget the target location of the spell in the list
     * @return the book page's name
     */
    @RequestMapping(value = "spl-{spellTarget}")
    public final String handleSpellSectionChangeBySpellPosition(final Model model, final HttpServletRequest request,
        @PathVariable("spellTarget") final String spellTarget) {
        getLogger().info("Handling spell section switch to position 'spl-{}'.", spellTarget);
        String sectionIdentifier;
        final int position;
        final SorParagraphData data = (SorParagraphData) getWrapper(request).getParagraph().getData();
        final List<Choice> spellChoices = data.getSpellChoices();
        final Choice choice;
        if (spellTarget.contains("~")) {
            final String[] split = spellTarget.split("~");
            sectionIdentifier = split[0];
            choice = getChoice(spellChoices, sectionIdentifier);
        } else {
            position = Integer.valueOf(spellTarget);
            choice = spellChoices.get(position);
            sectionIdentifier = choice.getId();
        }
        data.getChoices().add(new Choice(sectionIdentifier, choice.getText(), SPELL_POSITION, null));
        getLogger().info("Handling spell section switch to section '{}'.", sectionIdentifier);
        return super.handleSection(model, request, "s-" + sectionIdentifier);
    }

    private Choice getChoice(final List<Choice> spellChoices, final String sectionIdentifier) {
        Choice selectedChoice = null;
        for (final Choice choice : spellChoices) {
            if (sectionIdentifier.equals(choice.getId())) {
                selectedChoice = choice;
            }
        }
        return selectedChoice;
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final Paragraph paragraph = wrapper.getParagraph();
        saveCharacterIfNecessary(character, paragraph.getId());
        character.setLastEatenBonus(0);
        super.handleCustomSectionsPre(model, wrapper, changedSection);
    }

    @Override
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        character.setLuckCookieActive(false);
        super.handleCustomSectionsPost(model, wrapper, changedSection);
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        if (character.getCommandView() == null) {
            getInfo().getCharacterHandler().getItemHandler().removeItem(character, CURSED_CHAINMAIL_GLOVES, 1);
        }
    }

    private void saveCharacterIfNecessary(final SorCharacter character, final String sectionIdentifier) {
        final String bookNumber = String.valueOf(getInfo().getId() % POSITION_FOR_ONES);
        final String identifier = bookNumber + "-" + sectionIdentifier;
        final Map<String, SorCharacter> characterSaveLocations = character.getCharacterSaveLocations();
        if (characterSaveLocations.containsKey(identifier)) {
            try {
                characterSaveLocations.put(identifier, character.clone());
            } catch (final CloneNotSupportedException ex) {
                getLogger().error("Failed to create clone from the character for a possible spell return location.");
                throw new IllegalStateException(ex);
            }
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

    @Override
    protected void prepareFight(final HttpSessionWrapper wrapper) {
        magicChainService.preparateIfNeeded(wrapper, getInfo());
        super.prepareFight(wrapper);
    }

}
