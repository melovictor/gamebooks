package hu.zagor.gamebooks.lw.mvc.book.newgame.controller;

import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.lw.character.LwCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;

public class LwBookNewGameController extends RawBookNewGameController {

    @Override
    public LwCharacterPageData getCharacterPageData(final Character character) {
        return (LwCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    @Override
    protected BookParagraphConstants getStarterParagraph() {
        return BookParagraphConstants.GENERATE;
    }

}
