package hu.zagor.gamebooks.lw.domain;

import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.lw.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.lw.section.LwRuleBookParagraphResolver;

/**
 * Bean for storing book informations that can be used to display to the user.
 * @author Tamas_Szekeres
 */
public class LwBookInformations extends BookInformations {
    /**
     * Basic constructor that sets the id of the book instance.
     * @param id the unique id of the book
     */
    public LwBookInformations(final long id) {
        super(id);
        setCharacterBeanId("lwCharacter");
        setCharacterPageDataBeanId("lwCharacterPageData");
    }

    @Override
    public LwCharacterHandler getCharacterHandler() {
        return (LwCharacterHandler) super.getCharacterHandler();
    }

    @Override
    public LwRuleBookParagraphResolver getParagraphResolver() {
        return (LwRuleBookParagraphResolver) super.getParagraphResolver();
    }

    @Override
    public String getInventoryServiceBeanName() {
        return "lwBookInventoryService";
    }

}
