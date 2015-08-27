package hu.zagor.gamebooks.content.command.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.WeaponReplacementData;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.easymock.IMocksControl;
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

    private FightCommandResolver underTest;
    private IMocksControl mockControl;
    private FightCommand command;
    private ResolvationData resolvationData;
    private Map<String, FightCommandSubResolver> subResolvers;
    private FightCommandSubResolver resolver;
    private ParagraphData rootData;
    private FfCharacter character;
    private BookInformations info;
    private CharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private FfCharacterItemHandler itemHandler;
    private WeaponReplacementData replacementData;
    private List<String> forceWeapons;
    private FfItem forcedWeapon;
    private FfItem nonForcedWeapon;
    private List<ParagraphData> resolveList;
    private FightCommandMessageList messages;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        resolver = mockControl.createMock(FightCommandSubResolver.class);
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);

        characterHandler = new FfCharacterHandler();
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);

        character = new FfCharacter();
        info = new FfBookInformations(9L);
        info.setCharacterHandler(characterHandler);
        rootData = mockControl.createMock(ParagraphData.class);

        messages = new FightCommandMessageList();

        subResolvers = new HashMap<>();
        subResolvers.put("simpleResolver", resolver);

        underTest = new FightCommandResolver();
        underTest.setSubResolvers(subResolvers);

        replacementData = new WeaponReplacementData();

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

        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).build();

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
