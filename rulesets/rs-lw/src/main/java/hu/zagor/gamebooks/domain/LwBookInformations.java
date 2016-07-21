package hu.zagor.gamebooks.domain;

import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.lw.section.LwRuleBookParagraphResolver;

public class LwBookInformations extends BookInformations {

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
