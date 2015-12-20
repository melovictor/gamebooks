package hu.zagor.gamebooks.content.command.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.SilentCapableResolver;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.command.random.RandomCommandResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightBoundingCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class FightBoundingCommandResolverTest {

    private FightBoundingCommandResolver underTest;
    private IMocksControl mockControl;
    private FightRoundBoundingCommand fightRandomCommand;
    private ResolvationData resolvationData;
    private RandomCommandResolver randomResolver;
    private List<ParagraphData> paragraphList;
    private FightCommand command;
    private FightCommandMessageList messages;
    private Locale locale;
    private Map<Class<? extends Command>, SilentCapableResolver<? extends Command>> boundingResolvers;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FightBoundingCommandResolver();
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(null);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(null).withCharacter(null).build();
        randomResolver = mockControl.createMock(RandomCommandResolver.class);
        paragraphList = new ArrayList<>();
        command = new FightCommand();
        messages = mockControl.createMock(FightCommandMessageList.class);
        locale = Locale.ENGLISH;
        boundingResolvers = new HashMap<>();
        boundingResolvers.put(RandomCommand.class, randomResolver);
        underTest.setBoundingResolvers(boundingResolvers);
        Whitebox.setInternalState(command, "messages", messages);
    }

    @BeforeMethod
    public void setUpMethod() {
        fightRandomCommand = new FightRoundBoundingCommand(command);
        fightRandomCommand.setNth(1);
        mockControl.reset();
    }

    public void testDoResolveIfRandomIsNullShouldDoNothing() {
        // GIVEN
        expect(messages.getLocale()).andReturn(locale);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(fightRandomCommand, resolvationData);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoResolveIfBoundingShouldNotBeExecutedDueToIncorrectRoundNumberShouldNotCallResolver() {
        // GIVEN
        fightRandomCommand.setNth(2);
        fightRandomCommand.setRoundNumber(1);
        final RandomCommand random = new RandomCommand();
        fightRandomCommand.getCommands().add(random);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(fightRandomCommand, resolvationData);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoResolveIfRandomIsNotNullShouldCallResolver() {
        // GIVEN
        final RandomCommand random = new RandomCommand();
        fightRandomCommand.getCommands().add(random);
        expect(messages.getLocale()).andReturn(locale);
        expect(randomResolver.resolveSilently(random, resolvationData, messages, locale)).andReturn(paragraphList);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(fightRandomCommand, resolvationData);
        // THEN
        Assert.assertSame(returned, paragraphList);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
