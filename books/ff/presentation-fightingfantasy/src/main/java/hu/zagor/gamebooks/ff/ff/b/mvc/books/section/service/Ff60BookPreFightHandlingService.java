package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.EnemyDependentFfBookPreFightHandlingService;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

/**
 * Pre fight handler for FF60.
 * @author Tamas_Szekeres
 */
public class Ff60BookPreFightHandlingService extends EnemyDependentFfBookPreFightHandlingService {
    private static final String BOLORANG = "3027";
    private static final String THROWING_KNIFE = "3017";
    private static final String ZOMBIE_DUST = "3020";
    private static final String GAS_GLOBE = "3019";
    private static final String VOODOO_DEFENSE = "4009";
    private static final int SPELL_COST = -2;
    private static final String INSECT_DEFENSE = "4010";

    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private MessageSource messageSource;
    @Autowired private LocaleProvider localeProvider;

    @Resource(name = "ff60AttackEffectivenessVerification") private Map<String, Set<String>> effectivenessVerification;

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        FfItem usedItem = null;

        if (BOLORANG.equals(itemId)) {
            usedItem = doSkillTest(BOLORANG, "bolorang", info, wrapper);
        } else if (THROWING_KNIFE.equals(itemId)) {
            usedItem = doSkillTest(THROWING_KNIFE, "throwkingKnife", info, wrapper);
        } else if (ZOMBIE_DUST.equals(itemId)) {
            handleZombieDust(info, wrapper);
        } else if (GAS_GLOBE.equals(itemId)) {
            handleGasGlobe(info, wrapper);
        } else if (VOODOO_DEFENSE.equals(itemId)) {
            usedItem = handleVoodooDefense(info, wrapper);
        } else if (INSECT_DEFENSE.equals(itemId)) {
            usedItem = handleInsectDefense(info, wrapper);
        }

        return usedItem;
    }

    private FfItem handleVoodooDefense(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        character.changeStamina(SPELL_COST);
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        final FfItem item = (FfItem) itemHandler.getItem(character, VOODOO_DEFENSE);
        if (item != null) {
            final ParagraphData data = wrapper.getParagraph().getData();
            final int[] randomNumber = generator.getRandomNumber(1);
            final int maxEnemiesKilled = (randomNumber[0] + 1) / 2;
            final int enemiesKilled = killEnemies(wrapper, maxEnemiesKilled, effectivenessVerification.get(VOODOO_DEFENSE));
            final String diceText = renderer.render(generator.getDefaultDiceSide(), randomNumber);
            appendText(data, "page.ff.label.random.after", diceText, randomNumber[0]);
            appendText(data, "page.ff60.fight.voodoo", enemiesKilled);
        }

        return item;
    }

    private FfItem handleInsectDefense(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        character.changeStamina(SPELL_COST);
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        final FfItem item = (FfItem) itemHandler.getItem(character, INSECT_DEFENSE);
        if (item != null) {
            final ParagraphData data = wrapper.getParagraph().getData();
            final int[] randomNumber = generator.getRandomNumber(1);
            final int maxEnemiesKilled = (randomNumber[0] + 1) / 2;
            killEnemies(wrapper, maxEnemiesKilled, effectivenessVerification.get(INSECT_DEFENSE));
            final String diceText = renderer.render(generator.getDefaultDiceSide(), randomNumber);
            appendText(data, "page.ff.label.random.after", diceText, randomNumber[0]);
            appendText(data, "page.ff60.fight.insect");
        }

        return item;
    }

    private int killEnemies(final HttpSessionWrapper wrapper, final int maxEnemiesKilled, final Set<String> effectiveAgainst) {
        final FightCommand command = (FightCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand();
        final List<String> enemies = command.getEnemies();
        int killed = 0;
        for (final String enemyId : enemies) {
            if (killed < maxEnemiesKilled) {
                if (effectiveAgainst.contains(enemyId)) {
                    final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get(enemyId);
                    if (enemy.getStamina() > 0) {
                        enemy.setStamina(0);
                        killed++;
                    }
                }
            }
        }
        return killed;
    }

    private void handleGasGlobe(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final FfEnemy enemy = getEnemy(wrapper, info);
        if (effectivenessVerification.get(GAS_GLOBE).contains(enemy.getId())) {
            enemy.setSkill(enemy.getSkill() - 2);
            appendText(wrapper.getParagraph().getData(), "page.ff60.fight.gasGlobeEffective", enemy.getName());
        } else {
            appendText(wrapper.getParagraph().getData(), "page.ff60.fight.gasGlobeIneffective", enemy.getName());
        }
        info.getCharacterHandler().getItemHandler().removeItem(wrapper.getCharacter(), GAS_GLOBE, 1);
    }

    private void handleZombieDust(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final FfEnemy enemy = getEnemy(wrapper, info);
        if (isZombieDustEffective(enemy)) {
            killEnemy(wrapper, enemy);
        } else {
            reportDustFailure(wrapper, enemy);
        }
        info.getCharacterHandler().getItemHandler().removeItem(wrapper.getCharacter(), ZOMBIE_DUST, 1);
    }

    private void reportDustFailure(final HttpSessionWrapper wrapper, final FfEnemy enemy) {
        appendText(wrapper.getParagraph().getData(), "page.ff60.fight.zombieDustUnaffected", enemy.getName());
    }

    private void killEnemy(final HttpSessionWrapper wrapper, final FfEnemy enemy) {
        enemy.setStamina(0);
        appendText(wrapper.getParagraph().getData(), "page.ff60.fight.zombieDustHit", enemy.getName());
    }

    private boolean isZombieDustEffective(final FfEnemy enemy) {
        return effectivenessVerification.get(ZOMBIE_DUST).contains(enemy.getId());
    }

    private FfItem doSkillTest(final String itemId, final String keySegment, final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final int[] rollResult = generator.getRandomNumber(2);
        final ParagraphData data = wrapper.getParagraph().getData();
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final FfEnemy enemy = getEnemy(wrapper, info);
        FfItem item = null;
        item = (FfItem) characterHandler.getItemHandler().getItem(character, itemId);

        if (attributeHandler.resolveValue(character, "skill") >= rollResult[0]) {
            recordRollResult(rollResult, data, "success");
            appendText(data, "page.ff60.fight." + keySegment + "Hit", enemy.getCommonName());
            enemy.setStamina(enemy.getStamina() - 2);
        } else {
            recordRollResult(rollResult, data, "failure");
            appendText(data, "page.ff60.fight." + keySegment + "Missed", enemy.getCommonName());
        }
        return item;
    }

    private void recordRollResult(final int[] rollResult, final ParagraphData data, final String result) {
        final String diceImage = renderer.render(generator.getDefaultDiceSide(), rollResult);
        appendText(data, "page.ff.label.test.skill.compact", diceImage, rollResult[0], resolveKey("page.ff.label.test." + result));
    }

    private String resolveKey(final String textKey, final Object... params) {
        return messageSource.getMessage(textKey, params, localeProvider.getLocale());
    }

    private void appendText(final ParagraphData data, final String textKey, final Object... params) {
        String text = data.getText();

        text += "<p>" + resolveKey(textKey, params) + "</p>";

        data.setText(text);
    }

}
