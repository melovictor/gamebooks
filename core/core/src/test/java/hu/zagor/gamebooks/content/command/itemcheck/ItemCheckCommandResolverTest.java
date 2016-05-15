package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckCommandResolverTest {

    private static final CheckType CHECK_TYPE = CheckType.item;
    @MockControl private IMocksControl mockControl;
    @UnderTest private ItemCheckCommandResolver underTest;
    @Instance private Character character;
    @Mock private ParagraphData rootDataElement;
    private Paragraph paragraph;
    @Mock private ParagraphData resolvedData;
    @Mock private ParagraphData afterData;
    private Map<CheckType, ItemCheckStubCommandResolver> stubCommands;
    @Mock private ItemCheckStubCommandResolver itemCheckStubCommand;
    @Instance private CharacterHandler characterHandler;
    private ResolvationData resolvationData;
    private BookInformations info;
    private ItemCheckCommand command;
    @Inject private Logger logger;

    @BeforeClass
    public void setUpClass() {
        paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootDataElement);

        info = new BookInformations(11L);
        info.setCharacterHandler(characterHandler);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new ItemCheckCommand();
        command.setId("3257");
        stubCommands = new HashMap<>();
        stubCommands.put(CHECK_TYPE, itemCheckStubCommand);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testResolveWhenResolvationDataIsNullShouldThrowException() {
        // GIVEN
        command.setCheckType(CHECK_TYPE);
        underTest.setStubCommands(stubCommands);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenCheckTypeIsNotSetShouldThrowException() {
        // GIVEN
        underTest.setStubCommands(stubCommands);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenStubCommandsNotSetNullShouldThrowException() {
        // GIVEN
        underTest.setStubCommands(null);
        command.setCheckType(CHECK_TYPE);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testResolveWhenUnsupportedCheckTypeShouldThrowException() {
        // GIVEN
        command.setCheckType(CheckType.section);
        logger.info("Checking availability of {} '{}'.", CheckType.section, "3257");
        underTest.setStubCommands(stubCommands);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    public void testResolveWhenNoAfterAvailableShouldReturnOnlyResolvedData() {
        // GIVEN
        command.setCheckType(CHECK_TYPE);
        logger.info("Checking availability of {} '{}'.", CHECK_TYPE, "3257");
        underTest.setStubCommands(stubCommands);
        expect(itemCheckStubCommand.resolve(command, resolvationData)).andReturn(resolvedData);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), resolvedData);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenNoAfterAndResolvedDataAvailableShouldReturnEmptyList() {
        // GIVEN
        command.setCheckType(CHECK_TYPE);
        logger.info("Checking availability of {} '{}'.", CHECK_TYPE, "3257");
        underTest.setStubCommands(stubCommands);
        expect(itemCheckStubCommand.resolve(command, resolvationData)).andReturn(null);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 0);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenAfterAvailableShouldReturnResolvedAndAfterData() {
        // GIVEN
        command.setCheckType(CHECK_TYPE);
        logger.info("Checking availability of {} '{}'.", CHECK_TYPE, "3257");
        underTest.setStubCommands(stubCommands);
        expect(itemCheckStubCommand.resolve(command, resolvationData)).andReturn(resolvedData);
        command.setAfter(afterData);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 2);
        Assert.assertSame(resolveList.get(0), resolvedData);
        Assert.assertSame(resolveList.get(1), afterData);
        Assert.assertTrue(returned.isFinished());
    }

}
