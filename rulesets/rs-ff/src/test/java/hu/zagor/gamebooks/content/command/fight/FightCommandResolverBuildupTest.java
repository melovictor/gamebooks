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
import hu.zagor.gamebooks.content.Paragraph;
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
public class FightCommandResolverBuildupTest {

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
    private Paragraph paragraph;

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

        paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();

        forceWeapons = new ArrayList<>();
        forceWeapons.add("1001");
        forceWeapons.add("1005");
        replacementData.setForceWeapons(forceWeapons);
    }

    public void testResolveWhenInFirstRoundShouldForceWeaponAndCommandWasAlreadyGivenShouldNotTryForcingWeaponReplacement() {
        // GIVEN
        command.setReplacementData(replacementData);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isFinished());
        Assert.assertTrue(command.isOngoing());
        Assert.assertNull(returned.getResolveList());
    }

    public void testResolveWhenNotInFirstRoundAndShouldForceWeaponShouldNotTryForcingWeaponReplacement() {
        // GIVEN
        command.increaseBattleRound();
        command.setReplacementData(replacementData);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isFinished());
        Assert.assertTrue(command.isOngoing());
        Assert.assertNull(returned.getResolveList());
    }

    public void testResolveWhenInFirstRoundWithoutCommandAndShouldNotForceWeaponShouldNotTryForcingWeaponReplacement() {
        // GIVEN
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isFinished());
        Assert.assertTrue(command.isOngoing());
        Assert.assertNull(returned.getResolveList());
    }

    public void testResolveWhenInFirstRoundWithoutCommandAndForcableWeaponListIsNullShouldNotTryForcingWeaponReplacement() {
        // GIVEN
        command.setReplacementData(replacementData);
        replacementData.setForceWeapons(null);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isFinished());
        Assert.assertTrue(command.isOngoing());
        Assert.assertNull(returned.getResolveList());
    }

    public void testResolveWhenInFirstRoundWithoutCommandAndShouldForceWeaponWhichIsSelectedAnywayShouldDoNothing() {
        // GIVEN
        command.setReplacementData(replacementData);
        forcedWeapon.getEquipInfo().setRemovable(true);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(forcedWeapon).times(2);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isFinished());
        Assert.assertTrue(command.isOngoing());
        Assert.assertNull(returned.getResolveList());
        Assert.assertEquals(replacementData.getOrigWeapon(), "1001");
        Assert.assertTrue(replacementData.isSwitchedWeaponRemovable());
        Assert.assertFalse(forcedWeapon.getEquipInfo().isRemovable());
    }

    public void testResolveWhenInFirstRoundWithoutCommandAndShouldForceWeaponWhichIsSelectedAnywayButIsNonRemovableShouldDoNothing() {
        // GIVEN
        command.setReplacementData(replacementData);
        forcedWeapon.getEquipInfo().setRemovable(false);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(forcedWeapon).times(2);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isFinished());
        Assert.assertTrue(command.isOngoing());
        Assert.assertNull(returned.getResolveList());
        Assert.assertEquals(replacementData.getOrigWeapon(), "1001");
        Assert.assertFalse(replacementData.isSwitchedWeaponRemovable());
        Assert.assertFalse(forcedWeapon.getEquipInfo().isRemovable());
    }

    public void testResolveWhenInFirstRoundWithoutCommandAndShouldForceWeaponWhichIsNotSelectedShouldReplaceWeapons() {
        // GIVEN
        command.setReplacementData(replacementData);
        forcedWeapon.getEquipInfo().setRemovable(true);
        nonForcedWeapon.getEquipInfo().setRemovable(true);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(nonForcedWeapon);
        expect(itemHandler.hasItem(character, "1001")).andReturn(true);
        itemHandler.setItemEquipState(character, "1001", true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(forcedWeapon);
        expect(resolver.doResolve(command, resolvationData)).andAnswer(new MessageAddingAnswer());
        expect(rootData.getText()).andReturn("<p>Original text.</p>");
        rootData.setText("<p>Original text.</p><p>Round 1.<br />\nThe Orc is dead.</p>");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isFinished());
        Assert.assertTrue(command.isOngoing());
        Assert.assertNull(returned.getResolveList());
        Assert.assertEquals(replacementData.getOrigWeapon(), "1002");
        Assert.assertTrue(replacementData.isSwitchedWeaponRemovable());
        Assert.assertFalse(forcedWeapon.getEquipInfo().isRemovable());
        Assert.assertTrue(nonForcedWeapon.getEquipInfo().isRemovable());
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
            return null;
        }
    }
}
