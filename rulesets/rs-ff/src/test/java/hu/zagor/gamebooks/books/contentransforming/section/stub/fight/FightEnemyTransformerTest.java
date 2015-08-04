package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightEnemyTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightEnemyTransformerTest extends AbstractTransformerTest {

    private FightEnemyTransformer underTest;
    private IMocksControl mockControl;
    private FightCommand command;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FightEnemyTransformer();
        init(mockControl);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformShouldReadAndSetEnemyId() {
        // GIVEN
        command = new FightCommand();
        expectAttribute("id", "26a");
        mockControl.replay();
        // WHEN
        underTest.doTransform(null, node, command, null);
        // THEN
        Assert.assertEquals(command.getEnemies().get(0), "26a");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
