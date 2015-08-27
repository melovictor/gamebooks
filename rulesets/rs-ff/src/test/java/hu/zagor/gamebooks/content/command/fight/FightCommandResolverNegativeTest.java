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
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class FightCommandResolverNegativeTest {

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

        messages = new FightCommandMessageList();

        subResolvers = new HashMap<>();
        subResolvers.put("simpleResolver", resolver);

        underTest = new FightCommandResolver();
        underTest.setSubResolvers(subResolvers);

        replacementData = new WeaponReplacementData();
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
        rootData = mockControl.createMock(ParagraphData.class);
        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).build();

        forceWeapons = new ArrayList<>();
        forceWeapons.add("1001");
        forceWeapons.add("1005");
        replacementData.setForceWeapons(forceWeapons);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenSubResolverIsNotAvailableShouldThrowException() {
        // GIVEN
        command.setResolver("nonExistentResolver");
        command.setReplacementData(replacementData);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenWeaponToBeForcedIsNotAvailableWithTheCharacterShouldThrowException() {
        // GIVEN
        command.setReplacementData(replacementData);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(nonForcedWeapon);
        expect(itemHandler.hasItem(character, "1001")).andReturn(false);
        expect(itemHandler.hasItem(character, "1005")).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenForcedWeaponIsNotSelectedButIsNonRemovableShouldThrowException() {
        // GIVEN
        command.setReplacementData(replacementData);
        forcedWeapon.getEquipInfo().setRemovable(true);
        nonForcedWeapon.getEquipInfo().setRemovable(false);
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(nonForcedWeapon);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
