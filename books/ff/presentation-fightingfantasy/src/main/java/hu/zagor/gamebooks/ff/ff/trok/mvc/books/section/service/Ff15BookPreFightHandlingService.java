package hu.zagor.gamebooks.ff.ff.trok.mvc.books.section.service;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfBookPreFightHandlingService;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Implementation of the {@link FfBookPreFightHandlingService} interface for the FF15 book.
 * @author Tamas_Szekeres
 */
public class Ff15BookPreFightHandlingService implements FfBookPreFightHandlingService {

    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;
    @Autowired private HierarchicalMessageSource messageSource;
    @Autowired private LocaleProvider localeProvider;

    @Override
    public FfItem handlePreFightItemUsage(final FfBookInformations info, final HttpSessionWrapper wrapper, final String itemId) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        itemHandler.removeItem(character, itemId, 1);

        final int[] totalDamageThrow = generator.getRandomNumber(3);
        final String grenadeDamageDices = renderer.render(generator.getDefaultDiceSide(), totalDamageThrow);
        final int totalDamage = totalDamageThrow[0];

        final Paragraph paragraph = wrapper.getParagraph();
        final ParagraphData data = paragraph.getData();
        final FfFightCommand command = (FfFightCommand) paragraph.getItemsToProcess().get(0).getCommand();
        for (final FfEnemy enemy : command.getResolvedEnemies()) {
            enemy.setStamina(enemy.getStamina() - totalDamage);
        }

        final String firstMessage = resolveText("page.ff.fight.preFight.grenadeThrow", grenadeDamageDices);
        final String secondMessage = resolveText("page.ff.fight.preFight.grenadeDamage", totalDamage);

        data.setText(data.getText() + firstMessage + secondMessage);

        return null;
    }

    private String resolveText(final String key, final Object... params) {
        final Locale locale = localeProvider.getLocale();
        return messageSource.getMessage(key, params, locale);
    }

}
