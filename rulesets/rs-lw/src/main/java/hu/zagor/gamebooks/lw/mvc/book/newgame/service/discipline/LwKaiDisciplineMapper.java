package hu.zagor.gamebooks.lw.mvc.book.newgame.service.discipline;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.lw.character.KaiDisciplines;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Rank;
import hu.zagor.gamebooks.lw.character.WeaponSkill;
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
            addWeaponSkill(result, kaiDisciplines);
        }
    }

    private void addWeaponSkill(final Map<String, Object> result, final KaiDisciplines kaiDisciplines) {
        final WeaponSkill weaponSkill = kaiDisciplines.getWeaponSkill();
        final DiceConfiguration weaponRollConfig = new DiceConfiguration(1, 0, 9);
        final int[] randomNumber = generator.getRandomNumber(weaponRollConfig);
        final int weaponSkillRolledValue = randomNumber[0];
        weaponSkill.setDagger(weaponSkillRolledValue == 0);
        weaponSkill.setSpear(weaponSkillRolledValue == 1);
        weaponSkill.setMace(weaponSkillRolledValue == 2);
        weaponSkill.setShortSword(weaponSkillRolledValue == 3);
        weaponSkill.setWarhammer(weaponSkillRolledValue == 4);
        weaponSkill.setSword(weaponSkillRolledValue == 5);
        weaponSkill.setAxe(weaponSkillRolledValue == 6);
        weaponSkill.setSword(weaponSkillRolledValue == 7);
        weaponSkill.setQuarterstaff(weaponSkillRolledValue == 8);
        weaponSkill.setBroadsword(weaponSkillRolledValue == 9);
        result.put("weaponSkill",
            messageSource.getMessage("page.lw.characterGeneration.weaponObtained." + weaponSkillRolledValue, renderer.render(weaponRollConfig, randomNumber)));
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
