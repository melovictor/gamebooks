package hu.zagor.gamebooks.domain;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.section.FfRuleBookParagraphResolver;

/**
 * FF-specific book information bean.
 * @author Tamas_Szekeres
 */
public class FfBookInformations extends BookInformations {

    /**
     * Basic constructor that sets the id of the book instance.
     * @param id the unique id of the book
     */
    public FfBookInformations(final long id) {
        super(id);
        setCharacterBeanId("ffCharacter");
        setCharacterPageDataBeanId("ffCharacterPageData");
    }

    @Override
    public FfCharacterHandler getCharacterHandler() {
        return (FfCharacterHandler) super.getCharacterHandler();
    }

    @Override
    public FfRuleBookParagraphResolver getParagraphResolver() {
        return (FfRuleBookParagraphResolver) super.getParagraphResolver();
    }

}
