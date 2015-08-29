package hu.zagor.gamebooks.content.command.fight.roundresolver.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

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
public class Ff7SpecialAttackStrengthGeneratorEnemyTest {

    private IMocksControl mockControl;
    private Ff7SpecialAttackStrengthGenerator underTest;
    private FightCommand command;
    private List<Item> equipmentList;
    private Item item;
    private List<String> enemyList;
    private List<RoundEvent> roundEvents;
    private FfEnemy enemy;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff7SpecialAttackStrengthGenerator();
        command = mockControl.createMock(FightCommand.class);
        item = mockControl.createMock(FfItem.class);
        equipmentList = new ArrayList<Item>();
        equipmentList.add(item);
        equipmentList.add(item);
        roundEvents = new ArrayList<RoundEvent>();
        enemy = mockControl.createMock(FfEnemy.class);
        setUpClassWithWarning();
    }

    @SuppressWarnings("unchecked")
    private void setUpClassWithWarning() {
        enemyList = mockControl.createMock(List.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        roundEvents.clear();
    }

    public void testGetEnemyAttackStrengthWhenNotFirstRoundShouldReturnNull() {
        // GIVEN
        expect(command.getRoundNumber()).andReturn(3);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetEnemyAttackStrengthWhenFirstRoundButHasNoSpecialitiesShouldReturnNull() {
        // GIVEN
        final RoundEvent event = new RoundEvent();
        event.setEnemyId("1");
        roundEvents.add(event);

        expect(command.getRoundNumber()).andReturn(1);
        expect(command.getRoundEvents()).andReturn(roundEvents).times(2);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn("1");

        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetEnemyAttackStrengthWhenFirstRoundAndEnemyIsHullTrollShouldReturnMaxAttackStrength() {
        // GIVEN
        final RoundEvent event = new RoundEvent();
        event.setEnemyId("1");
        roundEvents.add(event);

        expect(command.getRoundNumber()).andReturn(1);
        expect(command.getRoundEvents()).andReturn(roundEvents).times(2);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn("41");

        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertEquals(returned, new int[]{12, 6, 6});
    }

    public void testGetEnemyAttackStrengthWhenFirstRoundAndSogActiveShouldReturnMinAttackStrength() {
        // GIVEN
        final RoundEvent eventA = new RoundEvent();
        eventA.setEnemyId("900");
        roundEvents.add(eventA);
        final RoundEvent eventB = new RoundEvent();
        eventB.setEnemyId("2");
        roundEvents.add(eventB);

        expect(command.getRoundNumber()).andReturn(1);
        expect(command.getRoundEvents()).andReturn(roundEvents).times(2);

        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertEquals(returned, new int[]{2, 1, 1});
    }

    public void testGetEnemyAttackStrengthWhenFirstRoundAndPotionOfConfusionActiveShouldReturnMaxAttackStrength() {
        // GIVEN
        final RoundEvent eventA = new RoundEvent();
        eventA.setEnemyId("950");
        roundEvents.add(eventA);
        final RoundEvent eventB = new RoundEvent();
        eventB.setEnemyId("2");
        roundEvents.add(eventB);

        expect(command.getRoundNumber()).andReturn(1);
        expect(command.getRoundEvents()).andReturn(roundEvents).times(2);

        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertEquals(returned, new int[]{12, 6, 6});
    }

    public void testGetEnemyAttackStrengthWhenFirstRoundAndSogAndPotionOfConfusionActiveShouldReturnMedianAttackStrength() {
        // GIVEN
        final RoundEvent eventA = new RoundEvent();
        eventA.setEnemyId("950");
        roundEvents.add(eventA);
        final RoundEvent eventB = new RoundEvent();
        eventB.setEnemyId("900");
        roundEvents.add(eventB);

        expect(command.getRoundNumber()).andReturn(1);
        expect(command.getRoundEvents()).andReturn(roundEvents).times(2);

        expect(enemy.getSkill()).andReturn(11).times(2);

        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertEquals(returned, new int[]{14, 2, 1});
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
