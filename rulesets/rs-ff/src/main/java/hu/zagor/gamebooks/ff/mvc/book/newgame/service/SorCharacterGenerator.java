package hu.zagor.gamebooks.ff.mvc.book.newgame.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link CharacterGenerator} interface for the Sorcery ruleset using the default fighting fantasy character generator for common tasks.
 * @author Tamas_Szekeres
 */
@Component("sorCharacterGenerator")
public class SorCharacterGenerator implements CharacterGenerator {

    private static final int SKILL_DEFAULT_WIZARD = 4;
    private static final int DICE_SIDE = 6;
    @Autowired @Qualifier("defaultFfCharacterGenerator") private CharacterGenerator superGenerator;
    @Autowired private HttpServletRequest request;

    @Override
    public Map<String, Object> generateCharacter(final Character characterObject, final BookInformations info) {
        final Map<String, Object> result = superGenerator.generateCharacter(characterObject, info);
        final SorCharacter character = (SorCharacter) characterObject;

        final String caste = request.getParameterMap().get("caste")[0];
        character.setWizard("1".equals(caste));

        if (character.isWizard()) {
            character.initializeSaveLocations();
            final int[] skill = getRand().getRandomNumber(1, SKILL_DEFAULT_WIZARD);

            character.setSkill(skill[0]);
            character.setInitialSkill(skill[0]);

            result.put("ffSkill", skill[0] + getDiceRenderer().render(DICE_SIDE, skill));
        }

        return result;
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
