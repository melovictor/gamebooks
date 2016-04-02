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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
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
    private IMocksControl mockControl;
    private ItemCheckCommandResolver underTest;
    private Character character;
    private ParagraphData rootDataElement;
    private Paragraph paragraph;
    private ParagraphData resolvedData;
    private ParagraphData afterData;
    private Map<CheckType, ItemCheckStubCommandResolver> stubCommands;
    private ItemCheckStubCommandResolver itemCheckStubCommand;
    private CharacterHandler characterHandler;
    private ResolvationData resolvationData;
    private BookInformations info;
    private ItemCheckCommand command;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        character = new Character();
        rootDataElement = mockControl.createMock(ParagraphData.class);
        paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootDataElement);
        resolvedData = mockControl.createMock(ParagraphData.class);
        afterData = mockControl.createMock(ParagraphData.class);
        itemCheckStubCommand = mockControl.createMock(ItemCheckStubCommandResolver.class);

        characterHandler = new CharacterHandler();
        info = new BookInformations(11L);
        info.setCharacterHandler(characterHandler);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
        underTest = new ItemCheckCommandResolver();
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new ItemCheckCommand();
        stubCommands = new HashMap<>();
        stubCommands.put(CHECK_TYPE, itemCheckStubCommand);
        mockControl.reset();
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
        underTest.setStubCommands(stubCommands);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    public void testResolveWhenNoAfterAvailableShouldReturnOnlyResolvedData() {
        // GIVEN
        command.setCheckType(CHECK_TYPE);
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

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
