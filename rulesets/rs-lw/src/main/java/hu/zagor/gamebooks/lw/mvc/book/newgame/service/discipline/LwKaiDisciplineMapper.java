package hu.zagor.gamebooks.lw.mvc.book.newgame.service.discipline;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.lw.character.KaiDisciplines;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Rank;
import hu.zagor.gamebooks.lw.character.Weaponskill;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.messages.MessageSource;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A discipline mapper which can be used to map the user-selected Kai disciplines into the {@link LwCharacter}.
 * @author Tamas_Szekeres
 */
public class LwKaiDisciplineMapper implements LwDisciplineMapper {
    private static final int DAGGER_WEAPONSKILL_ROLL = 0;
    private static final int SPEAR_WEAPONSKILL_ROLL = 1;
    private static final int MACE_WEAPONSKILL_ROLL = 2;
    private static final int SHORT_SWORD_WEAPONSKILL_ROLL = 3;
    private static final int WARHAMMER_WEAPONSKILL_ROLL = 4;
    private static final int SWORD_1_WEAPONSKILL_ROLL = 5;
    private static final int AXE_WEAPONSKILL_ROLL = 6;
    private static final int SWORD_2_WEAPONSKILL_ROLL = 7;
    private static final int QUARTERSTAFF_WEAPONSKILL_ROLL = 8;
    private static final int BROADSWORD_WEAPONSKILL_ROLL = 9;

    @Autowired private HttpServletRequest request;
    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Autowired private MessageSource messageSource;
    @Autowired private DiceResultRenderer renderer;

    @Override
    public void mapDisciplines(final LwCharacter character, final Map<String, Object> result) {
        character.setRank(Rank.KaiInitiate);

        final Map<String, String[]> parameterMap = request.getParameterMap();
        final KaiDisciplines kaiDisciplines = character.getKaiDisciplines();
        kaiDisciplines.setCamouflage(checkSelection(parameterMap, "camouflage"));
        kaiDisciplines.setHunting(checkSelection(parameterMap, "hunting"));
        kaiDisciplines.setSixthSense(checkSelection(parameterMap, "sixthSense"));
        kaiDisciplines.setTracking(checkSelection(parameterMap, "tracking"));
        kaiDisciplines.setHealing(checkSelection(parameterMap, "healing"));
        kaiDisciplines.setMindshield(checkSelection(parameterMap, "mindshield"));
        kaiDisciplines.setMindblast(checkSelection(parameterMap, "mindblast"));
        kaiDisciplines.setAnimalKinship(checkSelection(parameterMap, "animalKinship"));
        kaiDisciplines.setMindOverMatter(checkSelection(parameterMap, "mindOverMatter"));

        if (checkSelection(parameterMap, "weaponskill")) {
            addWeaponskill(result, kaiDisciplines);
        }
    }

    private void addWeaponskill(final Map<String, Object> result, final KaiDisciplines kaiDisciplines) {
        final Weaponskill weaponskill = kaiDisciplines.getWeaponskill();
        final DiceConfiguration weaponRollConfig = new DiceConfiguration(1, 0, 9);
        final int[] randomNumber = generator.getRandomNumber(weaponRollConfig);
        final int weaponskillRolledValue = randomNumber[0];
        weaponskill.setDagger(weaponskillRolledValue == DAGGER_WEAPONSKILL_ROLL);
        weaponskill.setSpear(weaponskillRolledValue == SPEAR_WEAPONSKILL_ROLL);
        weaponskill.setMace(weaponskillRolledValue == MACE_WEAPONSKILL_ROLL);
        weaponskill.setShortSword(weaponskillRolledValue == SHORT_SWORD_WEAPONSKILL_ROLL);
        weaponskill.setWarhammer(weaponskillRolledValue == WARHAMMER_WEAPONSKILL_ROLL);
        weaponskill.setSword(weaponskillRolledValue == SWORD_1_WEAPONSKILL_ROLL);
        weaponskill.setAxe(weaponskillRolledValue == AXE_WEAPONSKILL_ROLL);
        weaponskill.setSword(weaponskillRolledValue == SWORD_2_WEAPONSKILL_ROLL);
        weaponskill.setQuarterstaff(weaponskillRolledValue == QUARTERSTAFF_WEAPONSKILL_ROLL);
        weaponskill.setBroadsword(weaponskillRolledValue == BROADSWORD_WEAPONSKILL_ROLL);
        result.put("weaponskill",
            messageSource.getMessage("page.lw.characterGeneration.weaponObtained." + weaponskillRolledValue, renderer.render(weaponRollConfig, randomNumber)));
    }

    private boolean checkSelection(final Map<String, String[]> parameterMap, final String discipline) {
        boolean selected = false;
        final String[] userInputs = parameterMap.get(discipline);
        if (userInputs != null && userInputs.length > 0) {
            selected = Boolean.parseBoolean(userInputs[0]);
        }
        return selected;
    }

}
