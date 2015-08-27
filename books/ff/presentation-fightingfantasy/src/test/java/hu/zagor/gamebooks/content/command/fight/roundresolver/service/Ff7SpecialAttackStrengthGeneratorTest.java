package hu.zagor.gamebooks.content.command.fight.roundresolver.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff7SpecialAttackStrengthGenerator}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff7SpecialAttackStrengthGeneratorTest {

    private IMocksControl mockControl;
    private Ff7SpecialAttackStrengthGenerator underTest;
    private FfCharacter character;
    private FightCommand command;
    private FfAttributeHandler attributeHandler;
    private List<Item> equipmentList;
    private Item item;
    private List<String> paragraphList;
    private FightCommandMessageList messages;
    private List<String> enemyList;
    private EquipInfo equipInfo;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff7SpecialAttackStrengthGenerator();
        command = mockControl.createMock(FightCommand.class);
        character = mockControl.createMock(FfCharacter.class);
        item = mockControl.createMock(FfItem.class);
        equipmentList = new ArrayList<Item>();
        equipmentList.add(item);
        equipmentList.add(item);
        messages = mockControl.createMock(FightCommandMessageList.class);
        equipInfo = mockControl.createMock(EquipInfo.class);
        setUpClassWithWarning();
    }

    @SuppressWarnings("unchecked")
    private void setUpClassWithWarning() {
        paragraphList = mockControl.createMock(List.class);
        enemyList = mockControl.createMock(List.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGetSelfAttackStrengthWhenNotFirstRoundShouldReturnNull() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(3);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetSelfAttackStrengthWhenFirstRoundAndGenericEnemyWithoutSpecialEquipmentShouldReturnNull() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(1);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(item.getId()).andReturn("1001").andReturn("3001");
        expect(character.getParagraphs()).andReturn(paragraphList);
        expect(paragraphList.contains("311")).andReturn(false);
        expect(command.getMessages()).andReturn(messages);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn("1");
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetSelfAttackStrengthWhenFirstRoundAndGenericEnemyWithSogsHelmetUnequippedShouldReturnNull() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(1);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(item.getId()).andReturn("1001");
        expect(item.getId()).andReturn("3009");
        expect(item.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isEquipped()).andReturn(false);
        expect(character.getParagraphs()).andReturn(paragraphList);
        expect(paragraphList.contains("311")).andReturn(false);
        expect(command.getMessages()).andReturn(messages);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn("1");
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertNull(returned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
