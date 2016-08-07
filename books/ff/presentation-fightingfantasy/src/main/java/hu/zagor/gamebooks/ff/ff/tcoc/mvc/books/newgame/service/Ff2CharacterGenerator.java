package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.tcoc.character.Ff2Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

/**
 * Character generator for FF2.
 * @author Tamas_Szekeres
 */
public class Ff2CharacterGenerator implements CharacterGenerator {

    private static final int DICE_SIDE = 6;
    private static final int SPELL_DEFAULT = 6;

    @Autowired @Qualifier("defaultFfCharacterGenerator") private CharacterGenerator superGenerator;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookInformations info, final Object generationInput) {
        final Map<String, Object> generateCharacter = superGenerator.generateCharacter(characterObject, info, generationInput);

        final Ff2Character character = (Ff2Character) characterObject;
        final int[] spell = getRand().getRandomNumber(2, SPELL_DEFAULT);

        character.setInitialSpell(spell[0]);

        generateCharacter.put("ffSpell", character.getInitialSpell() + getDiceRenderer().render(DICE_SIDE, spell));
        generateCharacter.put("ffSpellNumeric", character.getInitialSpell());

        return generateCharacter;
    }

    /**
     * Finalizes the character generation process.
     * @param character the {@link Ff2Character} we want to finalize
     * @param spells the spells selected by the user
     * @param characterHandler the {@link FfCharacterHandler} object
     */
    public void finalizeCharacter(final Ff2Character character, final String spells, final FfCharacterHandler characterHandler) {
        if (!StringUtils.isEmpty(spells.trim())) {
            final String[] selectedSpells = spells.split(" ");
            final int numberOfSpellsToAdd = Math.min(selectedSpells.length, character.getInitialSpell());

            for (int i = 0; i < numberOfSpellsToAdd; i++) {
                addSpell(character, selectedSpells[i], characterHandler);
            }
        }
    }

    private void addSpell(final Ff2Character character, final String spellId, final FfCharacterHandler characterHandler) {
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        final Item spellItem = getSpellFromCharacter(itemHandler, character, spellId);
        if (spellItem == null) {
            addSpellToCharacter(itemHandler, character, spellId);
        } else {
            spellItem.setAmount(spellItem.getAmount() + 1);
        }
    }

    private void addSpellToCharacter(final FfCharacterItemHandler itemHandler, final Ff2Character character, final String spellId) {
        itemHandler.addItem(character, spellId, 1);
    }

    private Item getSpellFromCharacter(final FfCharacterItemHandler itemHandler, final Ff2Character character, final String spellId) {
        return itemHandler.getItem(character, spellId);
    }

    @Override
    public RandomNumberGenerator getRand() {
        return superGenerator.getRand();
    }

    @Override
    public DiceResultRenderer getDiceRenderer() {
        return superGenerator.getDiceRenderer();
    }

}
