package hu.zagor.gamebooks.ff.enemy;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Unit test for class {@link FfRuleBookEnemyTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FfRuleBookEnemyTransformerTest extends AbstractTransformerTest {

    @UnderTest private FfRuleBookEnemyTransformer underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private Logger logger;
    @Instance(inject = true) private List<String> irrelevantNodeNames;
    @Mock private Document document;

    @BeforeClass
    public void setUpClass() {
        irrelevantNodeNames.add("#text");
    }

    @Test(expectedExceptions = XmlTransformationException.class)
    public void testTransformEnemiesWhenNodeListIsZeroShouldThrowException() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        logger.error("Couldn't find 'enemies' element in xml file!");
        mockControl.replay();
        // WHEN
        underTest.transformEnemies(document);
        // THEN throws exception
    }

    public void testTransformEnemiesWhenNodeListContainsEnemyShouldParseIt() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(node);
        expect(node.getNodeName()).andReturn("enemy");
        expectAttributeReads();
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = underTest.transformEnemies(document);
        // THEN
        final FfEnemy enemy = (FfEnemy) returned.get("26");
        Assert.assertEquals(enemy.getName(), "Orc");
        Assert.assertEquals(enemy.getFleeAtRound(), Integer.MAX_VALUE);
        Assert.assertEquals(enemy.getCommonName(), "Orc");
    }

    private void expectAttributeReads() {
        expectAttribute("id", "26");
        expectAttribute("name", "Orc");
        expectAttribute("commonName");
        expectAttribute("skill", "5");
        expectAttribute("stamina", "7");
        expectAttribute("attackStrength", "0");
        expectAttribute("attackStrengthBonus", "0");
        expectAttribute("staminaDamage");
        expectAttribute("staminaDamageWhenHit");
        expectAttribute("staminaAutoDamage");
        expectAttribute("staminaDamageWhileInactive");
        expectAttribute("skillDamage");
        expectAttribute("damageAbsorption");
        expectAttribute("damageAbsorptionEdged");
        expectAttribute("killableByBlessed");
        expectAttribute("killableByMagical");
        expectAttribute("killableByNormal");
        expectAttribute("resurrectable");
        expectAttribute("fleeAtStamina");
        expectAttribute("fleeAtRound");
        expectAttribute("same");
        expectAttribute("startAtRound");
        expectAttribute("indifferentAlly");
    }

}
