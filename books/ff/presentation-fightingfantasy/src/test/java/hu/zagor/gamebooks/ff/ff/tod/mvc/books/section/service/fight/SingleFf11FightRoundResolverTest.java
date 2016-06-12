package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleFf11FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleFf11FightRoundResolverTest {
    private SingleFf11FightRoundResolver underTest;

    @BeforeClass
    public void setUpClass() {
        underTest = new SingleFf11FightRoundResolver();
    }

    public void testGetTypeShouldReturnProperType() {
        // GIVEN
        // WHEN
        final Class<? extends CustomBeforeAfterRoundEnemyHandler<BasicEnemyPrePostFightDataContainer>> returned = underTest.getType();
        // THEN
        Assert.assertEquals(returned, Ff11BeforeAfterRoundEnemyHandler.class);
    }

    public void testGetDataBeanShouldReturnProperBean() {
        // GIVEN
        // WHEN
        final BasicEnemyPrePostFightDataContainer returned = underTest.getDataBean();
        // THEN
        Assert.assertNotNull(returned);
    }
}
