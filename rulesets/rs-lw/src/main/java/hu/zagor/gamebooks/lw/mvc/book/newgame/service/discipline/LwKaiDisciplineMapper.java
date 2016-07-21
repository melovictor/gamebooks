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

public class LwKaiDisciplineMapper implements LwDisciplineMapper {
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
        weaponskill.setDagger(weaponskillRolledValue == 0);
        weaponskill.setSpear(weaponskillRolledValue == 1);
        weaponskill.setMace(weaponskillRolledValue == 2);
        weaponskill.setShortSword(weaponskillRolledValue == 3);
        weaponskill.setWarhammer(weaponskillRolledValue == 4);
        weaponskill.setSword(weaponskillRolledValue == 5);
        weaponskill.setAxe(weaponskillRolledValue == 6);
        weaponskill.setSword(weaponskillRolledValue == 7);
        weaponskill.setQuarterstaff(weaponskillRolledValue == 8);
        weaponskill.setBroadsword(weaponskillRolledValue == 9);
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
