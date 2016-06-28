package hu.zagor.gamebooks.content.command.userinput;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SorUserInputCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SorUserInputCommandResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private SorUserInputCommandResolver underTest;
    @Inject private TypeAwareCommandResolver<UserInputCommand> decorated;
    @Mock private UserInputCommand command;
    @Instance private ResolvationData resolvationData;
    @Mock private CommandResolveResult resolveResult;
    private BookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private Paragraph paragraph;
    @Instance(inject = true) private Set<String> goblinGeneratorLocations;
    @Mock private List<ParagraphData> list;
    @Mock private SorCharacter character;
    private Item itemA;
    private Item itemB;
    private Item itemC;
    private Item itemD;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(Sorcery.KHARE_CITYPORT_OF_TRAPS);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);

        resolvationData.setParagraph(paragraph);
        resolvationData.setInfo(info);
        resolvationData.setCharacter(character);
        goblinGeneratorLocations.add("2-438");

        itemA = new FfItem("3201", "Goblin teeth", ItemType.common);
        itemB = new FfItem("3201", "Goblin teeth", ItemType.common);
        itemC = new FfItem("3201", "Goblin teeth", ItemType.common);
        itemD = new FfItem("3201", "Goblin teeth", ItemType.common);
        itemA.setAmount(1);
        itemB.setAmount(2);
        itemC.setAmount(1);
        itemD.setAmount(1);
    }

    public void testDoResolveWhenUserInputIsNotFromGoblinToothUsagePlaceShouldInit() {
        // GIVEN
        expect(decorated.resolve(command, resolvationData)).andReturn(resolveResult);
        expect(paragraph.getId()).andReturn("369");
        expect(resolveResult.getResolveList()).andReturn(list);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, list);
    }

    public void testDoResolveWhenUserInputIsFromGoblinToothUsagePlaceWithFewTeethShouldInitAndSetMaxValueToTeeth() {
        // GIVEN
        expect(decorated.resolve(command, resolvationData)).andReturn(resolveResult);
        expect(paragraph.getId()).andReturn("438");
        expect(itemHandler.getItems(character, "3201")).andReturn(Arrays.asList(itemA, itemB, itemC, itemD));
        expect(command.getMax()).andReturn(9);
        command.setMax(5);
        expect(resolveResult.getResolveList()).andReturn(list);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, list);
    }

    public void testDoResolveWhenUserInputIsFromGoblinToothUsagePlaceWithLotsOfTeethShouldInitAndSetMaxValueToOriginal() {
        // GIVEN
        expect(decorated.resolve(command, resolvationData)).andReturn(resolveResult);
        expect(paragraph.getId()).andReturn("438");
        expect(itemHandler.getItems(character, "3201")).andReturn(Arrays.asList(itemA, itemB, itemC, itemD));
        expect(command.getMax()).andReturn(3);
        command.setMax(3);
        expect(resolveResult.getResolveList()).andReturn(list);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, list);
    }

}
