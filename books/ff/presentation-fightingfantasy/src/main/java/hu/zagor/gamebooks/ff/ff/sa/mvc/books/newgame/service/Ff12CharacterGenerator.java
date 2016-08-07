package hu.zagor.gamebooks.ff.ff.sa.mvc.books.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.sa.character.Ff12Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Character generator for FF12.
 * @author Tamas_Szekeres
 */
@Component
public class Ff12CharacterGenerator implements CharacterGenerator {
    private static final int PRICE_MULTIPLIER = 3;
    private static final int DICE_SIDE = 6;
    private static final int ARMOR_DEFAULT = 6;

    @Autowired @Qualifier("defaultFfCharacterGenerator") private CharacterGenerator superGenerator;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookInformations info, final Object generationInput) {
        final Map<String, Object> generateCharacter = superGenerator.generateCharacter(characterObject, info, generationInput);

        final Ff12Character character = (Ff12Character) characterObject;
        final int[] weapons = superGenerator.getRand().getRandomNumber(1);
        final int[] armour = superGenerator.getRand().getRandomNumber(1, ARMOR_DEFAULT);

        character.setArmour(armour[0]);
        character.setInitialArmour(armour[0]);
        character.setInitialWeapons(weapons[0]);

        generateCharacter.put("ffArmour", character.getInitialArmour() + superGenerator.getDiceRenderer().render(DICE_SIDE, armour));
        generateCharacter.put("ffWeapons", weapons[0] + superGenerator.getDiceRenderer().render(DICE_SIDE, weapons));
        generateCharacter.put("ffWeaponsNumeric", weapons[0]);

        return generateCharacter;
    }

    /**
     * Finalizes the character generation process.
     * @param character the {@link Ff12Character} we want to finalize
     * @param weapons the weapons and armor selected by the user
     * @param characterHandler the {@link FfCharacterHandler} object
     */
    public void finalizeCharacter(final Ff12Character character, final Ff12WeaponChoice weapons, final FfCharacterHandler characterHandler) {
        if (isValidSelection(character, weapons)) {
            addItem(characterHandler, character, "1001", weapons.getLash());
            addItem(characterHandler, character, "1002", weapons.getBlaster());
            addItem(characterHandler, character, "3001", weapons.getGrenade());
            addItem(characterHandler, character, "3002", weapons.getBomb());
            character.setArmour(character.getArmour() + weapons.getArmour() * 2);
            character.setInitialArmour(character.getArmour());
        } else {
            addItem(characterHandler, character, "1001", 1);
        }
    }

    private void addItem(final FfCharacterHandler characterHandler, final Ff12Character character, final String itemId, final int itemAmount) {
        if (itemAmount > 0) {
            characterHandler.getItemHandler().addItem(character, itemId, itemAmount);
        }
    }

    private boolean isValidSelection(final Ff12Character character, final Ff12WeaponChoice weapons) {
        return weapons.getLash() + weapons.getArmour() + weapons.getGrenade() + weapons.getBlaster() * PRICE_MULTIPLIER
            + weapons.getBomb() * PRICE_MULTIPLIER <= character.getInitialWeapons();
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
