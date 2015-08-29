package hu.zagor.gamebooks.content.command.fight.roundresolver.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
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
public class Ff7SpecialAttackStrengthGeneratorSelfTest {

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
    private List<RoundEvent> roundEvents;
    private RandomNumberGenerator generator;
    private DiceResultRenderer renderer;

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
        roundEvents = new ArrayList<RoundEvent>();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        renderer = mockControl.createMock(DiceResultRenderer.class);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        Whitebox.setInternalState(underTest, "generator", generator);
        Whitebox.setInternalState(underTest, "renderer", renderer);
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
        roundEvents.clear();
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

    public void testGetSelfAttackStrengthWhenFirstRoundAndGenericEnemyWithSogsHelmetEquippedShouldReturnMaxAttackStrength() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(1);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(item.getId()).andReturn("1001");
        expect(item.getId()).andReturn("3009");
        expect(item.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isEquipped()).andReturn(true);
        expect(character.getParagraphs()).andReturn(paragraphList);
        expect(paragraphList.contains("311")).andReturn(false);
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff7.label.sogsHelmet")).andReturn(true);
        expect(command.getRoundEvents()).andReturn(roundEvents);
        expect(command.getRoundEvents()).andReturn(roundEvents);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertEquals(returned, new int[]{12, 6, 6});
        Assert.assertFalse(roundEvents.isEmpty());
        Assert.assertEquals(roundEvents.get(0).getEnemyId(), "900");
    }

    public void testGetSelfAttackStrengthWhenFirstRoundAndGenericEnemyWithSogsHelmetEquippedAndPotionOfConfusionNotActivatedShouldReturnMaxAttackStrength() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(1);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(item.getId()).andReturn("1001");
        expect(item.getId()).andReturn("3009");
        expect(item.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isEquipped()).andReturn(true);
        expect(character.getParagraphs()).andReturn(paragraphList);
        expect(paragraphList.contains("311")).andReturn(true);
        expect(command.getMessages()).andReturn(messages);

        final int[] roll = new int[]{3, 3};
        expect(generator.getRandomNumber(1)).andReturn(roll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, roll)).andReturn("3");
        expect(messages.addKey("page.ff.label.random.after", "3", 3)).andReturn(true);

        expect(messages.addKey("page.ff7.label.sogsHelmet")).andReturn(true);
        expect(command.getRoundEvents()).andReturn(roundEvents);
        expect(command.getRoundEvents()).andReturn(roundEvents);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertEquals(returned, new int[]{12, 6, 6});
        Assert.assertFalse(roundEvents.isEmpty());
        Assert.assertEquals(roundEvents.get(0).getEnemyId(), "900");
    }

    public void testGetSelfAttackStrengthWhenFirstRoundAndGenericEnemyWithSogsHelmetEquippedAndPotionOfConfusionActivatedShouldReturnMedianAttackStrength() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(1);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(item.getId()).andReturn("1001");
        expect(item.getId()).andReturn("3009");
        expect(item.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isEquipped()).andReturn(true);
        expect(character.getParagraphs()).andReturn(paragraphList);
        expect(paragraphList.contains("311")).andReturn(true);
        expect(command.getMessages()).andReturn(messages);

        final int[] roll = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(roll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, roll)).andReturn("1");
        expect(messages.addKey("page.ff.label.random.after", "1", 1)).andReturn(true);
        expect(messages.addKey("page.ff7.label.potionOfConfusion")).andReturn(true);
        expect(command.getRoundEvents()).andReturn(roundEvents);

        expect(messages.addKey("page.ff7.label.sogsHelmet")).andReturn(true);

        expect(command.getRoundEvents()).andReturn(roundEvents);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);

        expect(command.getRoundEvents()).andReturn(roundEvents);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertEquals(returned[0], 14);
        Assert.assertFalse(roundEvents.isEmpty());
        Assert.assertEquals(roundEvents.get(0).getEnemyId(), "950");
        Assert.assertEquals(roundEvents.get(1).getEnemyId(), "900");
    }

    public void testGetSelfAttackStrengthWhenFirstRoundAndGenericEnemyWithPotionOfConfusionActivatedShouldReturnMinAttackStrength() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(1);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(item.getId()).andReturn("1001");
        expect(item.getId()).andReturn("3001");
        expect(character.getParagraphs()).andReturn(paragraphList);
        expect(paragraphList.contains("311")).andReturn(true);
        expect(command.getMessages()).andReturn(messages);

        final int[] roll = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(roll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, roll)).andReturn("1");
        expect(messages.addKey("page.ff.label.random.after", "1", 1)).andReturn(true);
        expect(messages.addKey("page.ff7.label.potionOfConfusion")).andReturn(true);
        expect(command.getRoundEvents()).andReturn(roundEvents);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn("1");

        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertEquals(returned, new int[]{2, 1, 1});
        Assert.assertFalse(roundEvents.isEmpty());
        Assert.assertEquals(roundEvents.get(0).getEnemyId(), "950");
    }

    public void testGetSelfAttackStrengthWhenFirstRoundAndHillTrollSurprisesUsShouldReturnMinAttackStrength() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(1);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(item.getId()).andReturn("1001");
        expect(item.getId()).andReturn("3001");
        expect(character.getParagraphs()).andReturn(paragraphList);
        expect(paragraphList.contains("311")).andReturn(false);
        expect(command.getMessages()).andReturn(messages);

        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn("41");

        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getSelfAttackStrength(character, command, attributeHandler);
        // THEN
        Assert.assertEquals(returned, new int[]{2, 1, 1});
        Assert.assertTrue(roundEvents.isEmpty());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
