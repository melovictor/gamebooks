package hu.zagor.gamebooks.ff.sor.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.EnemyDependentFfBookPreFightHandlingService;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfBookPreFightHandlingService;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Implementation of the {@link FfBookPreFightHandlingService} interface for the Sorcery books.
 * @author Tamas_Szekeres
 */
public class SorBookPreFightHandlingService extends EnemyDependentFfBookPreFightHandlingService {
    private static final int CHAIN_ENEMY_STAMINA_LIMIT = 4;
    private static final String THROWING_DARTS_ID = "3031";
    private static final String CHAIN_ID = "3044";
    private static final String CHAKRAM_ID = "3055";
    private static final String CRYSTAL_BALL = "3060";
    private static final String ROCKS_ID = "3019a";
    private static final String ROCKS_NORMAL_ID = "3019";
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Autowired private HierarchicalMessageSource messageSource;
    @Autowired private LocaleProvider localeProvider;

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        FfItem usedItem = null;
        if (THROWING_DARTS_ID.equals(itemId)) {
            handleThrowingDarts(info, wrapper);
        } else if (ROCKS_ID.equals(itemId)) {
            handleRocks(info, wrapper);
        } else if (CHAKRAM_ID.equals(itemId)) {
            usedItem = handleChakram(info, wrapper);
        } else if (CHAIN_ID.equals(itemId)) {
            handleChainAttack(info, wrapper);
        } else if (CRYSTAL_BALL.equals(itemId)) {
            handleCrystalBallThrowing(info, wrapper);
        }
        return usedItem;
    }

    private void handleRocks(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final int[] rollResult = generator.getRandomNumber(2);
        final ParagraphData data = wrapper.getParagraph().getData();
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final FfEnemy enemy = getEnemy(wrapper, info);
        characterHandler.getItemHandler().removeItem(character, ROCKS_ID, 1);
        characterHandler.getItemHandler().removeItem(character, ROCKS_NORMAL_ID, 1);
        if (attributeHandler.resolveValue(character, "skill") > rollResult[0]) {
            recordRollResult(rollResult, data, "success");
            appendText(data, "page.sor.rockHit", enemy.getCommonName());
            enemy.setStamina(enemy.getStamina() - 1);
        } else {
            recordRollResult(rollResult, data, "failure");
            appendText(data, "page.sor.rockMissed", enemy.getCommonName());
        }
    }

    private void handleCrystalBallThrowing(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final int[] rollResult = generator.getRandomNumber(2);
        final ParagraphData data = wrapper.getParagraph().getData();
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final FfEnemy enemy = getEnemy(wrapper, info);

        String ballReportPostfix = "";
        final int[] breakResult = generator.getRandomNumber(1);
        final String breakRollResult = renderer.render(generator.getDefaultDiceSide(), breakResult);
        if (breakResult[0] % 2 == 1) {
            ballReportPostfix = ".broken";
            characterHandler.getItemHandler().removeItem(character, CRYSTAL_BALL, 1);
        }

        if (attributeHandler.resolveValue(character, "skill") > rollResult[0]) {
            recordRollResult(rollResult, data, "success");
            appendText(data, "page.sor.crystalBallHit" + ballReportPostfix, enemy.getCommonName(), breakRollResult);
            enemy.setStamina(enemy.getStamina() - 2);
        } else {
            recordRollResult(rollResult, data, "failure");
            appendText(data, "page.sor.crystalBallMissed" + ballReportPostfix, enemy.getCommonName(), breakRollResult);
        }
    }

    private void handleChainAttack(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final FfEnemy enemy = getEnemy(wrapper, info);
        String messageKey;
        if (enemy.getStamina() < CHAIN_ENEMY_STAMINA_LIMIT) {
            enemy.setStamina(0);
            info.getCharacterHandler().getItemHandler().removeItem(wrapper.getCharacter(), CHAIN_ID, 1);
            messageKey = "page.sor.magicChain.boundEnemy";
        } else {
            messageKey = "page.sor.magicChain.enemyTooStrong";
        }
        final ParagraphData data = wrapper.getParagraph().getData();
        final String message = messageSource.getMessage(messageKey, new Object[]{enemy.getCommonName()}, localeProvider.getLocale());
        data.setText(data.getText() + message);
    }

    private void handleThrowingDarts(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final int[] rollResult = generator.getRandomNumber(2);
        final ParagraphData data = wrapper.getParagraph().getData();
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final FfEnemy enemy = getEnemy(wrapper, info);
        characterHandler.getItemHandler().removeItem(character, THROWING_DARTS_ID, 1);
        if (attributeHandler.resolveValue(character, "skill") > rollResult[0]) {
            recordRollResult(rollResult, data, "success");
            appendText(data, "page.sor.dartHit", enemy.getCommonName());
            enemy.setStamina(enemy.getStamina() - 2);
        } else {
            recordRollResult(rollResult, data, "failure");
            appendText(data, "page.sor.dartMissed", enemy.getCommonName());
        }
    }

    private FfItem handleChakram(final FfBookInformations info, final HttpSessionWrapper wrapper) {
        final int[] rollResult = generator.getRandomNumber(2);
        final ParagraphData data = wrapper.getParagraph().getData();
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final FfEnemy enemy = getEnemy(wrapper, info);
        final FfItem item = (FfItem) characterHandler.getItemHandler().getItem(character, CHAKRAM_ID);
        if (attributeHandler.resolveValue(character, "skill") > rollResult[0]) {
            recordRollResult(rollResult, data, "success");
            appendText(data, "page.sor.chakramHit", enemy.getCommonName());
            enemy.setStamina(enemy.getStamina() - 2);
        } else {
            recordRollResult(rollResult, data, "failure");
            appendText(data, "page.sor.chakramMissed", enemy.getCommonName());
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
