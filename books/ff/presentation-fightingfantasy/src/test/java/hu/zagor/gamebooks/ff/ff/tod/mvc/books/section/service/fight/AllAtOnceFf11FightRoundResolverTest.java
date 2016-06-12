package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AllAtOnceFf11FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class AllAtOnceFf11FightRoundResolverTest {

    private AllAtOnceFf11FightRoundResolver underTest;

    @BeforeClass
    public void setUpClass() {
        underTest = new AllAtOnceFf11FightRoundResolver();
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
