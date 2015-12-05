package hu.zagor.gamebooks.ff.section;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;

/**
 * Implementation of the {@link BookParagraphResolver} for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class SorRuleBookParagraphResolver extends FfRuleBookParagraphResolver {

    @Override
    protected String getRulesetPrefix() {
        return "sor";
    }
}
