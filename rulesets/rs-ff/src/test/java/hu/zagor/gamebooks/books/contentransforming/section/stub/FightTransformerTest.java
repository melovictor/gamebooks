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
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link FightTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightTransformerTest extends AbstractTransformerTest {

    private FightTransformer underTest;
    private IMocksControl mockControl;
    private ParagraphData data;
    private BookParagraphDataTransformer parent;
    private ChoicePositionCounter positionCounter;
    private Map<String, CommandSubTransformer<FightCommand>> fightTransformers;
    private CommandSubTransformer<FightCommand> subTransformer;
    private BeanFactory beanFactory;
    private FightCommand command;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        node = mockControl.createMock(Node.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        data = new ParagraphData();
        data.setPositionCounter(positionCounter);

        underTest = new FightTransformer();
        command = new FightCommand();
        final List<String> irrelevantNodes = new ArrayList<>();
        irrelevantNodes.add("#text");
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodes);
        underTest.setBeanFactory(beanFactory);
        subTransformer = mockControl.createMock(CommandSubTransformer.class);
        fightTransformers = new HashMap<String, CommandSubTransformer<FightCommand>>();
        fightTransformers.put("enemy", subTransformer);
        underTest.setFightTransformers(fightTransformers);
    }

    @BeforeMethod
    public void setUpMethod() {
        data.getCommands().clear();
        mockControl.reset();
    }

    public void testDoTransformWhenNodeIsSupportedAndForceWeaponIsSetShouldTransformIt() {
        // GIVEN
        expect(beanFactory.getBean(FightCommand.class)).andReturn(command);
        expectAttribute("type", "single");
        expectAttribute("resolver");
        expectAttribute("attackStrengthRolledDices");
        expectAttribute("attackStrengthUsedDices");
        expectAttribute("recoverDamage");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon", "1001");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("enemy").times(2);
        subTransformer.transform(eq(parent), eq(nodeValue), anyObject(FightCommand.class), eq(positionCounter));
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final FightCommand command = (FightCommand) data.getCommands().get(0);
        Assert.assertEquals(command.getAttackStrengthRolledDices(), 2);
        Assert.assertEquals(command.getAttackStrengthUsedDices(), 2);
        Assert.assertEquals(command.getBattleType(), "single");
        Assert.assertEquals(command.getResolver(), "basic");
        Assert.assertTrue(command.getReplacementData().getForceWeapons().contains("1001"));
        Assert.assertEquals(command.getUsableWeaponTypes().size(), 2);
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.weapon1));
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.weapon2));
    }

    public void testDoTransformWhenFightTypeIsShootingShouldAutoGenerateShootingWeaponType() {
        // GIVEN
        expect(beanFactory.getBean(FightCommand.class)).andReturn(command);
        expectAttribute("type", "shooting");
        expectAttribute("resolver", "shooting");
        expectAttribute("attackStrengthRolledDices");
        expectAttribute("attackStrengthUsedDices");
        expectAttribute("recoverDamage");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("enemy").times(2);
        subTransformer.transform(eq(parent), eq(nodeValue), anyObject(FightCommand.class), eq(positionCounter));
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final FightCommand command = (FightCommand) data.getCommands().get(0);
        Assert.assertEquals(command.getAttackStrengthRolledDices(), 2);
        Assert.assertEquals(command.getAttackStrengthUsedDices(), 2);

        Assert.assertEquals(command.getBattleType(), "shooting");
        Assert.assertEquals(command.getResolver(), "shooting");
        Assert.assertEquals(command.getUsableWeaponTypes().size(), 1);
        Assert.assertTrue(command.getUsableWeaponTypes().contains(ItemType.shooting));
    }

    public void testDoTransformWhenNodeIsSupportedAndForceWeaponIsNotSetShouldTransformIt() {
        // GIVEN
        expect(beanFactory.getBean(FightCommand.class)).andReturn(command);
        expectAttribute("type", "single");
        expectAttribute("resolver");
        expectAttribute("attackStrengthRolledDices", "3");
        expectAttribute("attackStrengthUsedDices", "3");
        expectAttribute("recoverDamage");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("enemy").times(2);
        subTransformer.transform(eq(parent), eq(nodeValue), anyObject(FightCommand.class), eq(positionCounter));
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
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testDoTransformIfNodeIsNotSupportedShouldThrowException() {
        // GIVEN
        expect(beanFactory.getBean(FightCommand.class)).andReturn(command);
        expectAttribute("type", "single");
        expectAttribute("resolver");
        expectAttribute("attackStrengthRolledDices");
        expectAttribute("attackStrengthUsedDices");
        expectAttribute("recoverDamage");
        expectAttribute("usableWeaponTypes");
        expectAttribute("forceWeapon");
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

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
