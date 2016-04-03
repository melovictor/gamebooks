package hu.zagor.gamebooks.ff.section;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

/**
 * Implementation of the {@link BookParagraphResolver} for the Sorcery ruleset.
 * @author Tamas_Szekeres
 */
public class SorRuleBookParagraphResolver extends FfRuleBookParagraphResolver {
    private static final int TOTAL_JAPANESE_SYMBOLS = 21;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private MessageSource messageSource;

    @Override
    protected void executeBasics(final ResolvationData resolvationData, final ParagraphData subData) {
        super.executeBasics(resolvationData, subData);
        final SorParagraphData rootDataElement = (SorParagraphData) resolvationData.getParagraph().getData();
        final SorParagraphData sorSubData = (SorParagraphData) subData;
        final List<Choice> spellChoices = sorSubData.getSpellChoices();
        if (!spellChoices.isEmpty()) {
            if (!hasShakingDisease(resolvationData)) {
                filterSpellChoices(spellChoices);
            }
            rootDataElement.addSpellChoices(spellChoices);
        }
    }

    private void filterSpellChoices(final List<Choice> spellChoices) {
        final int spellFilterResult = generator.getRandomNumber(1)[0];
        if (spellFilterResult <= spellChoices.size()) {
            final Set<String> symbols = new HashSet<>();
            for (int i = 0; i < spellChoices.size(); i++) {
                if (spellFilterResult - 1 != i) {
                    final Choice newChoice = new Choice("-1", getUniqueRandomSymbol(symbols), -1, null);
                    spellChoices.set(i, newChoice);
                }
            }
        }
    }

    private String getUniqueRandomSymbol(final Set<String> symbols) {
        String symbol;
        do {
            symbol = getRandomSymbol();
        } while (symbols.contains(symbol));
        symbols.add(symbol);
        return symbol;
    }

    private String getRandomSymbol() {
        final int idx = generator.getRandomNumber(1, TOTAL_JAPANESE_SYMBOLS, 0)[0];
        final String key = "page.sor.spell.encrypted." + idx;
        return messageSource.getMessage(key, null, null);
    }

    private boolean hasShakingDisease(final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final Character character = resolvationData.getCharacter();
        return itemHandler.hasItem(character, "5008");
    }

    @Override
    public String getRulesetPrefix() {
        return "sor";
    }

}
