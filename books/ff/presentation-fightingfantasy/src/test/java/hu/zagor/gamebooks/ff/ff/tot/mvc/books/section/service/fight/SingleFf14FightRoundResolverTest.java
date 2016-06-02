package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleFf14FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleFf14FightRoundResolverTest {

    private final SingleFf14FightRoundResolver underTest = new SingleFf14FightRoundResolver();

    public void testGetTypeShouldReturnCorrectType() {
        // GIVEN
        // WHEN
        final Class<? extends CustomBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer>> returned = underTest.getType();
        // THEN
        Assert.assertEquals(returned, Ff14BeforeAfterRoundEnemyHandler.class);
    }

    public void testGetDataBeanShouldReturnObject() {
        // GIVEN
        // WHEN
        final EnemyPrePostFightDataContainer returned = underTest.getDataBean();
        // THEN
        Assert.assertNotNull(returned);
    }
}
