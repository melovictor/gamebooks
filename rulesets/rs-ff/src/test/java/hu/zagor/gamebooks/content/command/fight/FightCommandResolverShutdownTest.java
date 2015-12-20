package hu.zagor.gamebooks.content.command.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.WeaponReplacementData;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.easymock.IAnswer;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class FightCommandResolverShutdownTest {

    @UnderTest private FightCommandResolver underTest;
    @MockControl private IMocksControl mockControl;
    private FightCommand command;
    private ResolvationData resolvationData;
    private Map<String, FightCommandSubResolver> subResolvers;
    @Mock private FightCommandSubResolver resolver;
    @Mock private ParagraphData rootData;
    @Instance private FfCharacter character;
    private BookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Instance private WeaponReplacementData replacementData;
    private List<String> forceWeapons;
    private FfItem forcedWeapon;
    private FfItem nonForcedWeapon;
    private List<ParagraphData> resolveList;
    @Instance private FightCommandMessageList messages;
    @Mock private Iterator<Item> itemIterator;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);

        info = new FfBookInformations(9L);
        info.setCharacterHandler(characterHandler);

        subResolvers = new HashMap<>();
        subResolvers.put("simpleResolver", resolver);

        underTest.setSubResolvers(subResolvers);

        resolveList = new ArrayList<>();
        resolveList.add(mockControl.createMock(ParagraphData.class));
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        forcedWeapon = new FfItem("1001", "Sword", ItemType.weapon1);
        nonForcedWeapon = new FfItem("1002", "Sword", ItemType.weapon1);

        command = new FightCommand();
        command.setResolver("simpleResolver");
        command.setOngoing(true);
        Whitebox.setInternalState(command, "messages", messages);

        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();

        forceWeapons = new ArrayList<>();
        forceWeapons.add("1001");
        forceWeapons.add("1005");
        replacementData.setForceWeapons(forceWeapons);
    }

    public void testResolveWhenBattleIsDoneAndNoReplacementDataIsAvailableShouldDoNothing() {
        // GIVEN
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.setOngoing(false);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(itemHandler.getItemIterator(character)).andReturn(itemIterator);
        expect(itemIterator.hasNext()).andReturn(false);
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isFinished());
        Assert.assertFalse(command.isOngoing());
        Assert.assertSame(returned.getResolveList(), resolveList);
    }

    public void testResolveWhenBattleIsDoneAndHasItemsShouldResetUsedInPreFightFlags() {
        // GIVEN
        forcedWeapon.setUsedInPreFight(true);
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.setOngoing(false);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(itemHandler.getItemIterator(character)).andReturn(itemIterator);
        expect(itemIterator.hasNext()).andReturn(true);
        expect(itemIterator.next()).andReturn(forcedWeapon);
        expect(itemIterator.hasNext()).andReturn(false);
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isFinished());
        Assert.assertFalse(command.isOngoing());
        Assert.assertSame(returned.getResolveList(), resolveList);
        Assert.assertFalse(forcedWeapon.isUsedInPreFight());
    }

    public void testResolveWhenBattleIsDoneAndReplacementDataIsAvailableWhichRefersToTheCurrentWeaponShouldReleaseTheWeapon() {
        // GIVEN
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.setOngoing(false);
        command.setReplacementData(replacementData);
        replacementData.setOrigWeapon("1001");
        replacementData.setSwitchedWeaponRemovable(true);
        forcedWeapon.getEquipInfo().setRemovable(false);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(itemHandler.getEquippedWeapon(character)).andReturn(forcedWeapon);
        itemHandler.setItemEquipState(character, "1001", true);
        expect(itemHandler.getItemIterator(character)).andReturn(itemIterator);
        expect(itemIterator.hasNext()).andReturn(false);
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isFinished());
        Assert.assertFalse(command.isOngoing());
        Assert.assertSame(returned.getResolveList(), resolveList);
        Assert.assertTrue(forcedWeapon.getEquipInfo().isRemovable());
    }

    public void testResolveWhenBattleIsDoneAndReplacementDataIsAvailableWhichRefersToTheOtherWeaponShouldReleaseAndSwitchTheWeapon() {
        // GIVEN
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.setOngoing(false);
        command.setReplacementData(replacementData);
        replacementData.setOrigWeapon("1002");
        replacementData.setSwitchedWeaponRemovable(true);
        forcedWeapon.getEquipInfo().setRemovable(false);
        nonForcedWeapon.getEquipInfo().setRemovable(true);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(itemHandler.getEquippedWeapon(character)).andReturn(forcedWeapon);
        itemHandler.setItemEquipState(character, "1002", true);
        expect(itemHandler.getItemIterator(character)).andReturn(itemIterator);
        expect(itemIterator.hasNext()).andReturn(false);
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isFinished());
        Assert.assertFalse(command.isOngoing());
        Assert.assertSame(returned.getResolveList(), resolveList);
        Assert.assertTrue(forcedWeapon.getEquipInfo().isRemovable());
        Assert.assertTrue(nonForcedWeapon.getEquipInfo().isRemovable());
    }

    public void testResolveWhenBattleIsDoneAndReplacementDataIsAvailableWhichRefersToTheSameWeaponWhichIsNotDroppableShouldDoNothing() {
        // GIVEN
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.setOngoing(false);
        command.setReplacementData(replacementData);
        replacementData.setOrigWeapon("1001");
        replacementData.setSwitchedWeaponRemovable(false);
        forcedWeapon.getEquipInfo().setRemovable(false);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(itemHandler.getEquippedWeapon(character)).andReturn(forcedWeapon);
        itemHandler.setItemEquipState(character, "1001", true);
        expect(itemHandler.getItemIterator(character)).andReturn(itemIterator);
        expect(itemIterator.hasNext()).andReturn(false);
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isFinished());
        Assert.assertFalse(command.isOngoing());
        Assert.assertSame(returned.getResolveList(), resolveList);
        Assert.assertFalse(forcedWeapon.getEquipInfo().isRemovable());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class MessageAddingAnswer implements IAnswer<List<ParagraphData>> {

        @Override
        public List<ParagraphData> answer() throws Throwable {
            final FightCommandMessageList messages = command.getMessages();
            Whitebox.setInternalState(messages, "roundMessage", "Round 1.");
            messages.add("The Orc is dead.");
            return resolveList;
        }
    }

}
