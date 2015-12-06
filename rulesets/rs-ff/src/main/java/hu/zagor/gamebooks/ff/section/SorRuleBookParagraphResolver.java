package hu.zagor.gamebooks.ff.section;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;

/**
 * Implementation of the {@link BookParagraphResolver} for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class SorRuleBookParagraphResolver extends FfRuleBookParagraphResolver {

    @Override
    protected void executeBasics(final ResolvationData resolvationData, final ParagraphData subData) {
        super.executeBasics(resolvationData, subData);
        final SorParagraphData rootDataElement = (SorParagraphData) resolvationData.getRootData();
        final SorParagraphData sorSubData = (SorParagraphData) subData;
        rootDataElement.addSpellChoices(sorSubData.getSpellChoices());
    }

    @Override
    public String getRulesetPrefix() {
        return "sor";
    }
}
