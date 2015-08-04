package hu.zagor.gamebooks.books.saving.xml;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.startsWith;
import hu.zagor.gamebooks.support.logging.LoggerInjector;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;

/**
 * Unit test for class {@link DefaultXmlGameStateLoader}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultXmlGameStateLoaderProductiveTest {

    private DefaultXmlGameStateLoader underTest;
    private IMocksControl mockControl;
    private Logger logger;
    private AutowireCapableBeanFactory beanFactory;
    private DocumentBuilderFactory builderFactory;
    private LoggerInjector loggerInjector;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        beanFactory = mockControl.createMock(AutowireCapableBeanFactory.class);
        builderFactory = DocumentBuilderFactory.newInstance();
        loggerInjector = mockControl.createMock(LoggerInjector.class);

        underTest = new DefaultXmlGameStateLoader();
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "builderFactory", builderFactory);
        Whitebox.setInternalState(underTest, "loggerInjector", loggerInjector);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testLoadWhenInputIsActualSavedClassShouldReturnProperMap() {
        // GIVEN
        final String input = getInputString();
        final StringReader stringReader = new StringReader(input);
        expect(beanFactory.getBean("stringReader", input)).andReturn(stringReader);
        expect(beanFactory.getBean("inputSource", stringReader)).andReturn(new InputSource(stringReader));
        prepareForCreation(5);
        prepareForListCreation(5);
        prepareForCreation(2);
        prepareForListCreation(1);
        prepareForCreation(3);
        beanFactory.autowireBean(anyObject(String[].class));
        prepareForCreation(1);
        mockControl.replay();
        // WHEN
        final Object loadedObject = underTest.load(input);
        // THEN
        Assert.assertNotNull(loadedObject);
    }

    private void prepareForListCreation(final int repeat) {
        for (int i = 0; i < repeat; i++) {
            prepareForCreation(1);
            beanFactory.autowireBean(anyObject(String[].class));
        }
    }

    private void prepareForCreation(final int repetition) {
        for (int i = 0; i < repetition; i++) {
            logger.debug(startsWith("Creating new instance of class "));
            expect(loggerInjector.postProcessBeforeInitialization(anyObject(), isNull(String.class))).andReturn(null);
        }
    }

    private String getInputString() {
        final String inputPart1 = getInputString1();
        final String inputPart2 = getInputString2();
        final String inputPart3 = getInputString3();
        final String inputPart4 = getInputString4();
        return inputPart1 + inputPart2 + inputPart3 + inputPart4;
    }

    private String getInputString1() {
        final String inputPart1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <mainObject class=\"hu.zagor.gamebooks.books.saving."
            + "xml.domain.SavedGameMapWrapper\"> <element class=\"java.util.HashMap\" isMap=\"true\"> <mapEntry> <key class="
            + "\"java.lang.String\">paragraph</key> <value class=\"hu.zagor.gamebooks.content.Paragraph\"> <id class=\"java."
            + "lang.String\">0</id> <displayId class=\"java.lang.String\">Your mission</displayId> <data class=\"hu.zagor.gamebooks.books.saving.xml.output"
            + ".TmParagraphData\"> <hint isNull=\"true\"> </hint> <text class=\"java.lang.String\">&lt;p&gt;"
            + "You mission is to become a knight, and then find out how the most famous order, or group, of English knights "
            + "got its name.&lt;/p&gt;&lt;p&gt;For six hundred years, the highest honor in England has been to be made a knight "
            + "of the Order of the Garter. King Edward III began this order sometime in the 1340s. Members wear a blue garter "
            + "of cloth around their sleeves, on which is written &lt;i&gt;“Honi soit qui mal y pense.”&lt;/i&gt; This is the "
            + "motto of the Order of the Garter.\\par Why did the best knights in England choose a garter as their symbol - and "
            + "what does the motto mean? The secret of the knights is hidden back in time. You must travel back six centuries to "
            + "find it, but to do so you will first have to become a knight yourself!&lt;/p&gt;\r\n"
            + "\r\n"
            + "    &lt;p class=\"sectionTitle\"&gt;Equipment&lt;/p&gt;\r\n"
            + "    &lt;p&gt;For your mission, you will take with you a simple peasant's outfit. You will be wearing it when you arrive in the age of knights.&lt;/p&gt;\r\n"
            + "    &lt;p class=\"inlineImage\"&gt;&lt;img src=\"resources/tm1/equipment.jpg\" /&gt;&lt;/p&gt;\r\n"
            + "    \r\n"
            + "&lt;p&gt;You mission is to become a knight, and then find out how the most famous order, or group, of English "
            + "knights got its name.&lt;/p&gt;&lt;p&gt;For six hundred years, the highest honor in England has been to be made "
            + "a knight of the Order of the Garter. King Edward III began this order sometime in the 1340s. Members wear a blue "
            + "garter of cloth around their sleeves, on which is written &lt;i&gt;“Honi soit qui mal y pense.”&lt;/i&gt; This is "
            + "the motto of the Order of the Garter.\\par Why did the best knights in England choose a garter as their symbol - "
            + "and what does the motto mean? The secret of the knights is hidden back in time. You must travel back six centuries "
            + "to find it, but to do so you will first have to become a knight yourself!&lt;/p&gt;\r\n"
            + "\r\n"
            + "    &lt;p class=\"sectionTitle\"&gt;Equipment&lt;/p&gt;\r\n"
            + "    &lt;p&gt;For your mission, you will take with you a simple peasant's outfit. You will be wearing it when you arrive in the age of knights.&lt;/p&gt;\r\n"
            + "    &lt;p class=\"inlineImage\"&gt;&lt;img src=\"resources/tm1/equipment.jpg\" /&gt;&lt;/p&gt;\r\n"
            + "\r\n"
            + "    &lt;p class=\"sectionTitle\"&gt;Data bank&lt;/p&gt;\r\n"
            + "    &lt;p&gt;These facts about knights and medieval England will help you to complete your mission:&lt;/p&gt;\r\n"
            + "    &lt;ol&gt;\r\n"
            + "        &lt;li&gt;Knights preferred to fight on horseback rather than on foot. They used swords, spears, lances, and many kinds of metal and wooden clubs.\r\n"
            + "        &lt;p class=\"inlineImage\"&gt;&lt;img src=\"resources/tm1/rule1.jpg\" /&gt;&lt;/p&gt;&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Knights often fought mock battles called tournaments for sport. They used long lances to try and "
            + "knock their opponents off their horses.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Knights wore metal armor for protection in tournaments and battle. A suit of armor was called a harness.\r\n";
        return inputPart1;
    }

    private String getInputString2() {
        final String inputPart2 = "        &lt;p class=\"inlineImage\"&gt;&lt;img src=\"resources/tm1/rule2.jpg\" /&gt;&lt;/p&gt;&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Two kinds of bow and arrow were used in  battles in the 1300s. The crossbow was very  powerful but "
            + "slow. The string had to be pulled   by a complicated little machine. The longbow was much faster but tricky to use. "
            + "The longbow  was a new weapon, and only the English knew   how to use it.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Knights never used bow and arrow. They preferred their swords and looked down on the archers who accompanied them in battle.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Though gunpowder was used in Europe as early as 1300, it was not until the 1400s that guns were safe "
            + "enough or powerful enough to be important in fighting.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;In the Middle Ages, criminals could find  sanctuary in churches and cathedrals. It was    forbidden to pursue them there.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Young men learning to become craftsmen or tradesmen were called apprentices. Apprentice knights were called squires. A squire could only "
            + "become a knight in a ceremony performed    by a king or prince.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Knights had a code of honor called chivalry.  A knight swore to be brave, generous, and loyal    to his lord, and to protect and honor "
            + "women.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;People accused of being witches or sorcerers in the Middle Ages were often tied up with ropes and thrown into a pond. If the person floated, "
            + "he or she was supposed to be guilty. If they sank, they were judged innocent. If they\r\n"
            + " were found guilty, they were often burned at the    stake.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;King Edward III ruled England from 1327    to 1377. One of his most famous victories was the battle of Crecy, fought in northern France in "
            + "1346.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Edward III's son Edward, called the Black Prince because of the color of the armor he wore at Crecy, never became king, because he died "
            + "before his father did. He married Lady Joan of Kent in 1361. Their son Richard became king in 1377.\r\n"
            + "        &lt;p class=\"inlineImage\"&gt;&lt;img src=\"resources/tm1/map.jpg\" /&gt;&lt;/p&gt;\r\n"
            + "        &lt;/li&gt;\r\n"
            + "        &lt;li&gt;Each knight, wore a family design on his shield, clothes, and armor to identify him in battle. These heraldic designs were passed down from "
            + "one generation to another in noble families.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;The kings and princes of England wore a heraldic design, or crest, of three gold leopards on a red background. The kings of France wore a "
            + "crest of lily flowers.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;The famous King Arthur may not hav£ been a king at all. We don't even know if thai was his real name. All that is really known about him is "
            + "that he led one of the native tribes of Britons against the invading tribes of Angles and Saxons in the fifth century A.D.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Centuries after the real Arthur died, stories about him were told and retold. Each time the story was retold it got a little better, until it "
            + "became the elaborate legend we hear about today. The stories of the search for the Holy Grail and the Knights of the Round Table, for example, were invented in "
            + "the twelfth century, seven hundred years after Arthur died.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;The Black Death was one name for a terrible plague, or disease, that killed almost one third of the people in Europe in the 1340s. It was "
            + "spread by rats and fleas, though nobody knew that at the time.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;  The Black Death arrived in England in late 1348. It disappeared in winter, because the cold kept the fleas from spreading it, but it swept "
            + "the country in the warm months of 1349.&lt;/li&gt;\r\n"
            + "        &lt;li&gt;Medieval towns were usually surrounded by high walls for protection against attack. An attack on a town was called an assault or siege. "
            + "Armies besieging a town sometimes used giant catapults to throw rocks against the walls.&lt;/li&gt;\r\n";
        return inputPart2;
    }

    private String getInputString3() {
        final String inputPart3 = "        &lt;li&gt;The English kings had a castle in the town of Windsor from the late 1000s on. Winchester, about forty miles from Windsor, "
            + "was the capital of "
            + "England during Saxon times and was still a very important city in the 1300s.&lt;/li&gt;\r\n"
            + "      &lt;/ol&gt;</text> <choices class=\"hu.zagor.gamebooks.content.choice.DefaultChoiceSet\" isList=\"true\"> <listElement class=\"hu.zagor.gamebooks.content."
            + "choice.Choice\"> <id class=\"java.lang.String\">1</id> <text isNull=\"true\"> </text> <position class=\"java.lang.Integer\">0</position> "
            + "</listElement> </choices>"
            + "<commands class=\"hu.zagor.gamebooks.content.commandlist.CommandList\" isList=\"true\"> </commands> "
            + "<gatheredItems class=\"java.util.ArrayList\" isList=\"true\"> </gatheredItems> <lostItems "
            + "class=\"java.util.ArrayList\" isList=\"true\"> </lostItems> </data> <validMoves class=\"java.util.ArrayList\" isList=\"true\"> <listElement "
            + "class=\"java.lang.String"
            + "\">1</listElement> </validMoves> <validItems class=\"java.util.HashMap\" isMap=\"true\"> </validItems> </value> </mapEntry> <mapEntry> <key "
            + "class=\"java.lang.String\""
            + ">char</key> <value class=\"hu.zagor.gamebooks.character.Character\"> <paragraphs class=\"java.util.ArrayList\" isList=\"true\"> </paragraphs> "
            + "<equipment class=\""
            + "java.util.ArrayList\" isList=\"true\"> <listElement class=\"hu.zagor.gamebooks.character.item.Item\"> <id class=\"java.lang.String\">1001</id> "
            + "<name class=\"java."
            + "lang.String\">Peasant's outfit</name> <itemType class=\"hu.zagor.gamebooks.character.item.ItemType\" isEnum=\"true\" value=\"common\"> </itemType> <equipInfo "
            + "class=\"hu.zagor.gamebooks.character.item.EquipInfo\"> <equipped class=\"java.lang.Boolean\">true</equipped> <equippable class=\"java.lang.Boolean\">false"
            + "</equippable> </equipInfo> </listElement> </equipment> <userInteraction class=\"java.util.HashMap\" isMap=\"true\"> </userInteraction> "
            + "<commandView isNull=\"true\">"
            + "</commandView> <notes class=\"java.lang.String\">Data Bank\r\n"
            + "\r\n"
            + "These facts about knights and medieval England will help you to complete your mission:\r\n"
            + "\r\n"
            + "1. Knights preferred to fight on horseback rather than on foot. They used swords, spears, lances, and many kinds of metal and wooden clubs.\r\n"
            + "2. Knights often fought mock battles called tournaments for sport. They used long lances to try and knock their opponents off their horses.\r\n"
            + "3. Knights wore metal armor for protection in tournaments and battle. A suit of armor was called a harness.\r\n"
            + "4. Two kinds of bow and arrow were used in battles in the 1300s. The crossbow was very powerful but slow. The string had to be pulled by a complicated little "
            + "machine. The longbow was much faster but tricky to use. The longbow was a new weapon, and only the English knew how to use it.\r\n"
            + "5. Knights never used bow and arrow. They preferred their swords and looked down on the archers who accompanied them in battle.\r\n"
            + "6. Though gunpowder was used in Europe as early as 1300, it was not until the 1400s that guns were safe enough or powerful enough to be important in fighting."
            + "\r\n";
        return inputPart3;
    }

    private String getInputString4() {
        final String inputPart4 = "7. In the Middle Ages, criminals could find sanctuary in churches and cathedrals. It was forbidden to pursue them there.\r\n"
            + "8. Young men learning to become craftsmen or tradesmen were called apprentices. Apprentice knights were called squires. A squire could only become a knight in a"
            + " ceremony performed by a king or prince.\r\n"
            + "9. Knights had a code of honor called chivalry. A knight swore to be brave, generous, and loyal to his lord, and to protect and honor women.\r\n"
            + "10. People accused of being witches or sorcerers in the Middle Ages were often tied up with ropes and thrown into a pond. If the person floated, he or she was "
            + "supposed to be guilty. If they sank, they were judged innocent. If they were found guilty, they were often burned at the stake.\r\n"
            + "11. King Edward III ruled England from 1327 to 1377. One of his most famous victories was the battle of Crecy, fought in northern France in 1346.\r\n"
            + "12. Edward III's son Edward, called the Black Prince because of the color of the armor he wore at Crecy, never became king, because he died before his father "
            + "did. He married Lady Joan of Kent in 1361. Their son Richard became king in 1377.\r\n"
            + "13. Each knight wore a family design on his shield, clothes, and armor to identify him in battle. These heraldic designs were passed down from "
            + "one generation to another in noble families.\r\n"
            + "14. The kings and princes of England wore a heraldic design, or crest, of three gold leopards on a red background. The kings of France wore a crest of lily "
            + "flowers.\r\n"
            + "15. The famous King Arthur may not have been a king at all. We don't even know if that was his real name. All that is really known about him is that he led one "
            + "of the native tribes of Britons against the invading tribes of Angles and Saxons in the fifth century A.D.\r\n"
            + "16. Centuries after the real Arthur died, stories about him were told and retold. Each time the story was retold it got a little better, until it became the "
            + "elaborate legend we hear about today. The stories of the search for the Holy Grail and the Knights of the Round Table, for example, were invented in the twelfth"
            + " century, seven hundred years after Arthur died.\r\n"
            + "17. The Black Death was one name for a terrible plague, or disease, that killed almost one third of the people in Europe in the 1340s. It was spread by rats and"
            + " fleas, though nobody knew that at the time.\r\n"
            + "18. The Black Death arrived in England in late 1348. It disappeared in winter, because the cold kept the fleas from spreading it, but it swept the country in "
            + "the warm months of 1349.\r\n"
            + "19. Medieval towns were usually surrounded by high walls for protection against attack. An attack on a town was called an assault or siege. Armies besieging a "
            + "town sometimes used giant catapults to throw rocks against the walls.\r\n"
            + "20. The English kings had a castle in the town of Windsor from the late 1000s on. Winchester, about forty miles from Windsor, was the capital of England during "
            + "Saxon times and was still a very important city in the 1300s.</notes> </value> </mapEntry> </element> </mainObject>";
        return inputPart4;
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
