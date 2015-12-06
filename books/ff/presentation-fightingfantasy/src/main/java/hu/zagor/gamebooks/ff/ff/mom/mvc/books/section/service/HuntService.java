package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt.PieceMover;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt.PositionManipulator;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt.SectionTextUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Service for handling the tiger hunt.
 * @author Tamas_Szekeres
 */
@Component
public class HuntService {

    @Autowired @Qualifier("dogPieceMover") private PieceMover dogPieceMover;
    @Autowired @Qualifier("tigerPieceMover") private PieceMover tigerPieceMover;
    @Autowired private PositionManipulator positionManipulator;
    @Autowired private SectionTextUpdater sectionTextUpdater;

    /**
     * Plays a single round of the hunt.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link BookInformations} object
     * @return the result of the round
     */
    public HuntRoundResult playRound(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        final HuntRoundResult result = new HuntRoundResult();
        final Character character = wrapper.getCharacter();
        final FfUserInteractionHandler interactionHandler = info.getCharacterHandler().getInteractionHandler();

        result.setTigerPosition(tigerPieceMover.getPosition(character, interactionHandler));
        result.setDogPosition(dogPieceMover.getPosition(character, interactionHandler));

        final int currentRound = positionManipulator.getRound(character, interactionHandler);

        if (currentRound % 2 == 0) {
            tigerPieceMover.movePiece(result);
        } else {
            dogPieceMover.movePiece(result);
        }

        positionManipulator.verifyPositions(result, currentRound);
        positionManipulator.saveData(character, interactionHandler, currentRound, result);
        final Paragraph paragraph = wrapper.getParagraph();
        sectionTextUpdater.updateParagraphContent(paragraph, result);

        if (result.isHuntFinished()) {
            paragraph.addValidMove(resolvePosToId(paragraph, result.getNextSectionPos()));
        }

        return result;
    }

    private String resolvePosToId(final Paragraph paragraph, final String nextSectionPos) {
        return paragraph.getData().getChoices().getChoiceByPosition(Integer.parseInt(nextSectionPos)).getId();
    }

}
