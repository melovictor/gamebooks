package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt.PieceMover;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt.PositionManipulator;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt.SectionTextUpdater;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IAnswer;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link HuntService}.
 * @author Tamas_Szekeres
 */
@Test
public class HuntServiceTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private HuntService underTest;
    private FfBookInformations info;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private FfCharacter character;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Inject private PieceMover dogPieceMover;
    @Inject private PieceMover tigerPieceMover;
    @Inject private PositionManipulator positionManipulator;
    @Inject private SectionTextUpdater sectionTextUpdater;
    @Mock private Paragraph paragraph;
    @Mock private ParagraphData data;
    @Mock private ChoiceSet choiceSet;
    @Mock private Choice choice;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(3L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setInteractionHandler(interactionHandler);
    }

    public void testPlayRoundWhenOddRoundShouldDogMove() {
        // GIVEN
        final int roundNumber = 1;
        expect(wrapper.getCharacter()).andReturn(character);
        expect(tigerPieceMover.getPosition(character, interactionHandler)).andReturn("E4");
        expect(dogPieceMover.getPosition(character, interactionHandler)).andReturn("E12");
        expect(positionManipulator.getRound(character, interactionHandler)).andReturn(roundNumber);
        dogPieceMover.movePiece(anyObject(HuntRoundResult.class));
        positionManipulator.verifyPositions(anyObject(HuntRoundResult.class), eq(roundNumber));
        positionManipulator.saveData(eq(character), eq(interactionHandler), eq(roundNumber), anyObject(HuntRoundResult.class));
        expect(wrapper.getParagraph()).andReturn(paragraph);
        sectionTextUpdater.updateParagraphContent(eq(paragraph), anyObject(HuntRoundResult.class));
        mockControl.replay();
        // WHEN
        underTest.playRound(wrapper, info);
        // THEN
    }

    public void testPlayRoundWhenEvenRoundShouldTigerMove() {
        // GIVEN
        final int roundNumber = 2;
        expect(wrapper.getCharacter()).andReturn(character);
        expect(tigerPieceMover.getPosition(character, interactionHandler)).andReturn("E4");
        expect(dogPieceMover.getPosition(character, interactionHandler)).andReturn("E12");
        expect(positionManipulator.getRound(character, interactionHandler)).andReturn(roundNumber);
        tigerPieceMover.movePiece(anyObject(HuntRoundResult.class));
        positionManipulator.verifyPositions(anyObject(HuntRoundResult.class), eq(roundNumber));
        positionManipulator.saveData(eq(character), eq(interactionHandler), eq(roundNumber), anyObject(HuntRoundResult.class));
        expect(wrapper.getParagraph()).andReturn(paragraph);
        sectionTextUpdater.updateParagraphContent(eq(paragraph), anyObject(HuntRoundResult.class));
        mockControl.replay();
        // WHEN
        underTest.playRound(wrapper, info);
        // THEN
    }

    public void testPlayRoundWhenHuntIsFinishedShouldAddProperValidMove() {
        // GIVEN
        final int roundNumber = 2;
        expect(wrapper.getCharacter()).andReturn(character);
        expect(tigerPieceMover.getPosition(character, interactionHandler)).andReturn("E4");
        expect(dogPieceMover.getPosition(character, interactionHandler)).andReturn("E12");
        expect(positionManipulator.getRound(character, interactionHandler)).andReturn(roundNumber);
        tigerPieceMover.movePiece(anyObject(HuntRoundResult.class));
        positionManipulator.verifyPositions(anyObject(HuntRoundResult.class), eq(roundNumber));
        expectLastCall().andAnswer(new IAnswer<Object>() {

            @Override
            public Object answer() throws Throwable {
                final HuntRoundResult roundResult = (HuntRoundResult) getCurrentArguments()[0];
                roundResult.setHuntFinished(true);
                roundResult.setNextSectionPos("2");
                return null;
            }

        });
        positionManipulator.saveData(eq(character), eq(interactionHandler), eq(roundNumber), anyObject(HuntRoundResult.class));
        expect(wrapper.getParagraph()).andReturn(paragraph);
        sectionTextUpdater.updateParagraphContent(eq(paragraph), anyObject(HuntRoundResult.class));
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choiceSet);
        expect(choiceSet.getChoiceByPosition(2)).andReturn(choice);
        expect(choice.getId()).andReturn("371");
        paragraph.addValidMove("371");
        mockControl.replay();
        // WHEN
        underTest.playRound(wrapper, info);
        // THEN
    }

}
