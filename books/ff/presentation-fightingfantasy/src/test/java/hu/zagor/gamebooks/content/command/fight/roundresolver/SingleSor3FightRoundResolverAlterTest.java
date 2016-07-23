package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleSor3FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleSor3FightRoundResolverAlterTest {
    @MockControl private IMocksControl mockControl;
    @UnderTest private SingleSor3FightRoundResolver underTest;
    @Mock private FfFightCommand command;
    @Mock private FightCommandMessageList message;
    @Mock private SorCharacter character;
    @Instance private FfCharacterHandler characterHandler;
    @Instance private ResolvationData resolvationData;
    @Mock private FightBeforeRoundResult beforeRoundResult;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Inject private MessageSource source;

    @BeforeClass
    public void setUpClass() {
        resolvationData.setCharacter(character);
        final BookInformations info = new BookInformations(3);
        info.setCharacterHandler(characterHandler);
        resolvationData.setInfo(info);
        characterHandler.setInteractionHandler(interactionHandler);
    }

    public void testSuperResolveRound() {
        // GIVEN
        expect(command.getResolvedEnemies()).andReturn(new ArrayList<FfEnemy>());
        expect(command.getMessages()).andReturn(message);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(null);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] superResolveRound = underTest.superResolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(superResolveRound.length, 0);
    }

}
