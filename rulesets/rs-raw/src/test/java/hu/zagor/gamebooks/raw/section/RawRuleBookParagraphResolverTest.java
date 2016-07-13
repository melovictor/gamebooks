package hu.zagor.gamebooks.raw.section;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.CommandResolver;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.google.common.collect.Sets;

/**
 * Unit test for class {@link RawRuleBookParagraphResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class RawRuleBookParagraphResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private RawRuleBookParagraphResolver underTest;
    @Inject private Logger logger;
    @Inject private ServerCommunicator communicator;
    private ResolvationData resolvationData;
    @Mock private Paragraph paragraph;
    @Instance private List<ProcessableItemHolder> processableItemList;
    @Mock private ParagraphData data;
    @Instance private Character character;
    private BookInformations info;
    @Mock private Command command;
    @Mock private ParagraphData paragraphData;
    @Mock private Map<Class<? extends Command>, CommandResolver> commandResolvers;
    @Mock private CommandResolver fightCommandResolver;
    @Mock private CommandResolveResult resolveResult;
    @Instance private List<ParagraphData> resolveList;
    @Mock private CommandView commandView;

    private ProcessableItemHolder fightCommandHolder;
    private ProcessableItemHolder paragraphDataHolder;
    @Mock private ParagraphData newParagraphData;
    @Mock private ChoiceSet choices;
    @Instance private CharacterHandler characterHandler;
    @Mock private CharacterItemHandler itemHandler;
    @Mock private List<String> rewardList;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        info.setCommandResolvers(commandResolvers);
        fightCommandHolder = new ProcessableItemHolder(command);
        paragraphDataHolder = new ProcessableItemHolder(paragraphData);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
        processableItemList.clear();
        resolveList.clear();
    }

    public void testResolveWhenCommandReturnsWithEmptyListAndUnfinishedShouldStopForNextUserInteraction() {
        // GIVEN
        processableItemList.add(fightCommandHolder);
        processableItemList.add(paragraphDataHolder);

        expect(paragraph.getItemsToProcess()).andReturn(processableItemList).times(2);
        logger.debug("Executing command {}.", "EasyMock for class hu.zagor.gamebooks.content.command.Command");
        expect(commandResolvers.get(command.getClass())).andReturn(fightCommandResolver);
        expect(fightCommandResolver.resolve(command, resolvationData)).andReturn(resolveResult);
        expect(resolveResult.isFinished()).andReturn(false);
        expect(command.getCommandView("raw")).andReturn(commandView);
        character.setCommandView(commandView);
        expect(paragraph.getItemsToProcess()).andReturn(processableItemList);
        expect(resolveResult.getResolveList()).andReturn(resolveList);
        expect(resolveResult.isFinished()).andReturn(false);

        mockControl.replay();
        // WHEN
        underTest.resolve(resolvationData, paragraph);
        // THEN
        Assert.assertEquals(processableItemList, Arrays.asList(fightCommandHolder, paragraphDataHolder));
    }

    public void testResolveWhenCommandReturnsWithParagraphAndUnfinishedShouldResolveParagraphAndStopForNextUserInteraction() {
        // GIVEN
        processableItemList.add(fightCommandHolder);
        processableItemList.add(paragraphDataHolder);
        resolveList.add(newParagraphData);

        expect(paragraph.getItemsToProcess()).andReturn(processableItemList).times(2);
        logger.debug("Executing command {}.", "EasyMock for class hu.zagor.gamebooks.content.command.Command");
        expect(commandResolvers.get(command.getClass())).andReturn(fightCommandResolver);
        expect(fightCommandResolver.resolve(command, resolvationData)).andReturn(resolveResult);
        expect(resolveResult.isFinished()).andReturn(false);
        expect(command.getCommandView("raw")).andReturn(commandView);
        character.setCommandView(commandView);
        expect(paragraph.getItemsToProcess()).andReturn(processableItemList);
        expect(resolveResult.getResolveList()).andReturn(resolveList);
        expect(resolveResult.isFinished()).andReturn(false);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(newParagraphData.getText()).andReturn("New text.");
        paragraphData.appendText("New text.");
        expect(newParagraphData.getChoices()).andReturn(choices);
        paragraphData.addChoices(choices);
        expect(newParagraphData.getHiddenItems()).andReturn(new ArrayList<GatheredLostItem>());
        expect(newParagraphData.getUnhiddenItems()).andReturn(new ArrayList<GatheredLostItem>());
        expect(newParagraphData.getGatheredItems()).andReturn(new ArrayList<GatheredLostItem>());
        expect(newParagraphData.getLostItems()).andReturn(new ArrayList<GatheredLostItem>());
        expect(newParagraphData.getCodewords()).andReturn(Arrays.asList("ship", "crivens"));
        expect(newParagraphData.getCommands()).andReturn(new CommandList());
        expect(newParagraphData.getReward()).andReturn(null);
        expect(paragraph.getRewards()).andReturn(rewardList);
        rewardList.clear();

        mockControl.replay();
        // WHEN
        underTest.resolve(resolvationData, paragraph);
        // THEN
        Assert.assertEquals(processableItemList, Arrays.asList(fightCommandHolder, paragraphDataHolder));
        Assert.assertEquals(character.getCodeWords(), Sets.newHashSet("ship", "crivens"));
    }

}
