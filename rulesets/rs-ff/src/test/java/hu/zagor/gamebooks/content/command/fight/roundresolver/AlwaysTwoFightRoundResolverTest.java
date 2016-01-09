package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AlwaysTwoFightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class AlwaysTwoFightRoundResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private AlwaysTwoFightRoundResolver underTest;
    private List<FfEnemy> resolvedEnemies;
    @Mock private List<String> enemies;
    @Instance private FfEnemy enemyA;
    @Instance private FfEnemy enemyB;
    @Instance private FfEnemy enemyC;

    @BeforeMethod
    public void setUpMethod() {
        resolvedEnemies = new ArrayList<>();
        mockControl.reset();
    }

    public void testFilterResolvedEnemiesWhenHasMoreThanTwoEnemiesShouldReturnTwo() {
        // GIVEN
        resolvedEnemies.add(enemyA);
        resolvedEnemies.add(enemyB);
        resolvedEnemies.add(enemyC);
        mockControl.replay();
        // WHEN
        underTest.filterResolvedEnemies(resolvedEnemies, enemies);
        // THEN
        Assert.assertEquals(resolvedEnemies.size(), 2);
        Assert.assertSame(resolvedEnemies.get(0), enemyA);
        Assert.assertSame(resolvedEnemies.get(1), enemyB);
    }

    public void testFilterResolvedEnemiesWhenHasTwoEnemiesShouldReturnBoth() {
        // GIVEN
        resolvedEnemies.add(enemyA);
        resolvedEnemies.add(enemyB);
        mockControl.replay();
        // WHEN
        underTest.filterResolvedEnemies(resolvedEnemies, enemies);
        // THEN
        Assert.assertEquals(resolvedEnemies.size(), 2);
        Assert.assertSame(resolvedEnemies.get(0), enemyA);
        Assert.assertSame(resolvedEnemies.get(1), enemyB);
    }

    public void testFilterResolvedEnemiesWhenHasOneEnemyShouldReturnOne() {
        // GIVEN
        resolvedEnemies.add(enemyA);
        mockControl.replay();
        // WHEN
        underTest.filterResolvedEnemies(resolvedEnemies, enemies);
        // THEN
        Assert.assertEquals(resolvedEnemies.size(), 1);
        Assert.assertSame(resolvedEnemies.get(0), enemyA);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
