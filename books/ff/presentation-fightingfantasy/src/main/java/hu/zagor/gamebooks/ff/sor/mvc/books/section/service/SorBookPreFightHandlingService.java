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
    private static final String THROWING_DARTS_ID = "3031";
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Autowired private HierarchicalMessageSource messageSource;
    @Autowired private LocaleProvider localeProvider;

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        if (THROWING_DARTS_ID.equals(itemId)) {
            final int[] rollResult = generator.getRandomNumber(2);
            final ParagraphData data = wrapper.getParagraph().getData();
            final SorCharacter character = (SorCharacter) wrapper.getCharacter();
            final FfCharacterHandler characterHandler = info.getCharacterHandler();
            final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
            final FfEnemy enemy = getEnemy(wrapper, info);
            characterHandler.getItemHandler().removeItem(character, itemId, 1);
            if (attributeHandler.resolveValue(character, "skill") > rollResult[0]) {
                recordRollResult(rollResult, data, "success");
                appendText(data, "page.sor.dartHit", enemy.getCommonName());
                enemy.setStamina(enemy.getStamina() - 2);
            } else {
                recordRollResult(rollResult, data, "failure");
                appendText(data, "page.sor.dartMissed", enemy.getCommonName());
            }
        }
        return null;
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