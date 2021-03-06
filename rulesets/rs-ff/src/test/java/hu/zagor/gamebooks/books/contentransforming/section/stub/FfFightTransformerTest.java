package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link FfFightTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FfFightTransformerTest extends AbstractTransformerTest {

    @UnderTest private FfFightTransformer underTest;
    @MockControl private IMocksControl mockControl;
    @Instance private ParagraphData data;
    @Mock private BookParagraphDataTransformer parent;
    @Mock private ChoicePositionCounter positionCounter;
    private Map<String, CommandSubTransformer<FfFightCommand>> fightTransformers;
    @Mock private CommandSubTransformer<FfFightCommand> subTransformer;
    @Inject private BeanFactory beanFactory;
    @Instance private FfFightCommand command;

    @BeforeClass
    public void setUpClass() {
        node = mockControl.createMock(Node.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);
        data.setPositionCounter(positionCounter);

        final List<String> irrelevantNodes = new ArrayList<>();
        irrelevantNodes.add("#text");
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodes);
        fightTransformers = new HashMap<String, CommandSubTransformer<FfFightCommand>>();
        fightTransformers.put("enemy", subTransformer);
        underTest.setFightTransformers(fightTransformers);
    }

    @BeforeMethod
    public void setUpMethod() {
        data.getCommands().clear();
    }

    public void testDoTransformWhenNodeIsSupportedAndForceWeaponIsSetShouldTransformIt() {
        // GIVEN
        expect(beanFactory.getBean(FfFightCommand.class)).andReturn(command);
        expectAttribute("type", "single");
        expectAttribute("resolver");
        expectAttribute("forceOrder");
        expectAttribute("attackStrengthRolledDices");
        expectAttribute("attackStrengthUsedDices");
        expectAttribute("recoverDamage");
        expectAttribute("allyStaminaVisible", "true");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon", "1001");
        expectAttribute("preFight");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("enemy").times(2);
        subTransformer.transform(eq(parent), eq(nodeValue), anyObject(FfFightCommand.class), eq(positionCounter));
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final FfFightCommand command = (FfFightCommand) data.getCommands().get(0);
        Assert.assertEquals(command.getAttackStrengthRolledDices(), 2);
        Assert.assertEquals(command.getAttackStrengthUsedDices(), 2);
        Assert.assertEquals(command.getBattleType(), "single");
        Assert.assertEquals(command.getResolver(), "basic");
        Assert.assertTrue(command.getReplacementData().getForceWeapons().contains("1001"));
        Assert.assertEquals(command.getUsableWeaponTypes().size(), 2);
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.weapon1));
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.weapon2));
        Assert.assertTrue(command.isPreFightAvailable());
        Assert.assertTrue(command.isAllyStaminaVisible());
    }

    public void testDoTransformWhenFightTypeIsShootingShouldAutoGenerateShootingWeaponType() {
        // GIVEN
        expect(beanFactory.getBean(FfFightCommand.class)).andReturn(command);
        expectAttribute("type", "shooting");
        expectAttribute("resolver", "shooting");
        expectAttribute("forceOrder");
        expectAttribute("attackStrengthRolledDices");
        expectAttribute("attackStrengthUsedDices");
        expectAttribute("recoverDamage");
        expectAttribute("allyStaminaVisible");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon");
        expectAttribute("preFight", "false");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("enemy").times(2);
        subTransformer.transform(eq(parent), eq(nodeValue), anyObject(FfFightCommand.class), eq(positionCounter));
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final FfFightCommand command = (FfFightCommand) data.getCommands().get(0);
        Assert.assertEquals(command.getAttackStrengthRolledDices(), 2);
        Assert.assertEquals(command.getAttackStrengthUsedDices(), 2);
        Assert.assertFalse(command.isPreFightAvailable());
        Assert.assertEquals(command.getBattleType(), "shooting");
        Assert.assertEquals(command.getResolver(), "shooting");
        Assert.assertEquals(command.getUsableWeaponTypes().size(), 1);
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.shooting));
    }

    public void testDoTransformWhenNodeIsSupportedAndForceWeaponIsNotSetShouldTransformIt() {
        // GIVEN
        expect(beanFactory.getBean(FfFightCommand.class)).andReturn(command);
        expectAttribute("type", "single");
        expectAttribute("resolver");
        expectAttribute("forceOrder");
        expectAttribute("attackStrengthRolledDices", "3");
        expectAttribute("attackStrengthUsedDices", "3");
        expectAttribute("recoverDamage");
        expectAttribute("allyStaminaVisible");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon");
        expectAttribute("preFight");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("enemy").times(2);
        subTransformer.transform(eq(parent), eq(nodeValue), anyObject(FfFightCommand.class), eq(positionCounter));
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertEquals(command.getAttackStrengthRolledDices(), 3);
        Assert.assertEquals(command.getAttackStrengthUsedDices(), 3);
        Assert.assertEquals(command.getBattleType(), "single");
        Assert.assertEquals(command.getResolver(), "basic");
        Assert.assertNull(command.getReplacementData());
        Assert.assertEquals(command.getUsableWeaponTypes().size(), 2);
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.weapon1));
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.weapon2));
        Assert.assertTrue(command.isPreFightAvailable());
        Assert.assertFalse(command.isAllyStaminaVisible());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testDoTransformIfNodeIsNotSupportedShouldThrowException() {
        // GIVEN
        expect(beanFactory.getBean(FfFightCommand.class)).andReturn(command);
        expectAttribute("type", "single");
        expectAttribute("resolver");
        expectAttribute("forceOrder");
        expectAttribute("attackStrengthRolledDices");
        expectAttribute("attackStrengthUsedDices");
        expectAttribute("recoverDamage");
        expectAttribute("allyStaminaVisible");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon");
        expectAttribute("preFight");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("notEnemy").times(2);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

}
