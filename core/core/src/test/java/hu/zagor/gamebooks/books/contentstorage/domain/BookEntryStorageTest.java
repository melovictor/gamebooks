package hu.zagor.gamebooks.books.contentstorage.domain;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.CloneFailedException;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.HashMap;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BookEntryStorage}.
 * @author Tamas_Szekeres
 */
@Test
public class BookEntryStorageTest {

    private static final long BOOK_ID = 454654L;

    private static final String EXISTENT_ELEMENT_ID = "1002";
    private static final String NONEXISTENT_ELEMENT_ID = "1003";
    private static final String EXISTENT_ELEMENT_DISPLAY = "1002a";

    private BookEntryStorage underTest;
    @Inject private Logger logger;
    @MockControl private IMocksControl mockControl;
    @Mock private Paragraph paragraph;
    @Mock private Paragraph clonedParagraph;
    @Mock private Item item;
    @Mock private Item clonedItem;
    @Instance private Enemy enemy;

    @Instance private Map<String, Paragraph> paragraphs;
    @Instance private Map<String, Item> items;
    @Instance private Map<String, Enemy> enemies;

    @BeforeClass
    public void setUpClass() {
        enemy.setId("111");
        enemy.setName("enemy");
    }

    @UnderTest
    public BookEntryStorage underTest() {
        paragraphs.put(EXISTENT_ELEMENT_ID, paragraph);
        items.put(EXISTENT_ELEMENT_ID, item);
        enemies.put(EXISTENT_ELEMENT_ID, enemy);
        return new BookEntryStorage(BOOK_ID, paragraphs, items, enemies);
    }

    @BeforeMethod
    public void setUpMethod() {
        Whitebox.setInternalState(underTest, "enemies", enemies);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenParagraphsIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new BookEntryStorage(BOOK_ID, null, items, enemies).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetParagraphWhenParagraphIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getParagraph(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetItemWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getItem(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetEnemyWhenEnemyIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getEnemy(null);
        // THEN throws exception
    }

    public void testGetItemWhenItemsNotSetShouldReturnNull() {
        // GIVEN
        final BookEntryStorage underTest = new BookEntryStorage(BOOK_ID, paragraphs, null, null);
        Whitebox.setInternalState(underTest, "logger", logger);
        logger.debug("Fetching {} {} from book {}.", "item", EXISTENT_ELEMENT_ID, BOOK_ID);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.getItem(EXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetEnemyWhenEnemiesNotSetShouldReturnNull() {
        // GIVEN
        final BookEntryStorage underTest = new BookEntryStorage(BOOK_ID, paragraphs, null, null);
        Whitebox.setInternalState(underTest, "logger", logger);
        logger.debug("Fetching {} {} from book {}.", "enemy", EXISTENT_ELEMENT_ID, BOOK_ID);
        mockControl.replay();
        // WHEN
        final Enemy returned = underTest.getEnemy(EXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetItemWhenItemNotPresentInStorageShouldReturnNull() {
        // GIVEN
        logger.debug("Fetching {} {} from book {}.", "item", NONEXISTENT_ELEMENT_ID, BOOK_ID);
        logger.error("Cannot find {} with id '{}' in book '{}'!", "item", NONEXISTENT_ELEMENT_ID, BOOK_ID);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.getItem(NONEXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetEnemyWhenEnemyNotPresentInStorageShouldReturnNull() {
        // GIVEN
        logger.debug("Fetching {} {} from book {}.", "enemy", NONEXISTENT_ELEMENT_ID, BOOK_ID);
        logger.error("Cannot find {} with id '{}' in book '{}'!", "enemy", NONEXISTENT_ELEMENT_ID, BOOK_ID);
        mockControl.replay();
        // WHEN
        final Enemy returned = underTest.getEnemy(NONEXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetItemWhenItemInStorageShouldReturnClonedItem() {
        // GIVEN
        logger.debug("Fetching {} {} from book {}.", "item", EXISTENT_ELEMENT_ID, BOOK_ID);
        expect(item.clone()).andReturn(clonedItem);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.getItem(EXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertSame(returned, clonedItem);
    }

    public void testGetEnemyWhenEnemyInStorageShouldReturnClonedEnemy() {
        // GIVEN
        logger.debug("Fetching {} {} from book {}.", "enemy", EXISTENT_ELEMENT_ID, BOOK_ID);
        mockControl.replay();
        // WHEN
        final Enemy returned = underTest.getEnemy(EXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertEquals(returned.getId(), enemy.getId());
        Assert.assertEquals(returned.getName(), enemy.getName());
    }

    public void testGetParagraphWhenParagraphIdExistsShouldReturnClonedParagraph() throws CloneNotSupportedException {
        // GIVEN
        logger.debug("Fetching {} {} from book {}.", "paragraph", EXISTENT_ELEMENT_ID, BOOK_ID);
        expect(paragraph.clone()).andReturn(clonedParagraph);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.getParagraph(EXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertSame(returned, clonedParagraph);
    }

    @Test(expectedExceptions = CloneFailedException.class)
    public void testGetParagraphWhenParagraphIdExistsButCloningFailsShouldThrowException() throws CloneNotSupportedException {
        // GIVEN
        logger.debug("Fetching {} {} from book {}.", "paragraph", EXISTENT_ELEMENT_ID, BOOK_ID);
        expect(paragraph.clone()).andThrow(new CloneNotSupportedException());
        logger.error("Failed to clone {} '{}' from book '{}'!", "paragraph", EXISTENT_ELEMENT_ID, BOOK_ID);
        mockControl.replay();
        // WHEN
        underTest.getParagraph(EXISTENT_ELEMENT_ID);
        // THEN throws exception
    }

    public void testGetParagraphWhenParagraphIdDoesNotExistsShouldReturnNull() {
        // GIVEN
        logger.debug("Fetching {} {} from book {}.", "paragraph", NONEXISTENT_ELEMENT_ID, BOOK_ID);
        logger.error("Cannot find {} with id '{}' in book '{}'!", "paragraph", NONEXISTENT_ELEMENT_ID, BOOK_ID);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.getParagraph(NONEXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetLastAccessShouldReturnProperValues() throws CloneNotSupportedException {
        // GIVEN
        final long before = underTest.getLastAccess();
        final long now = System.currentTimeMillis();
        logger.debug("Fetching {} {} from book {}.", "paragraph", EXISTENT_ELEMENT_ID, BOOK_ID);
        expect(paragraph.clone()).andReturn(clonedParagraph);
        mockControl.replay();
        underTest.getParagraph(EXISTENT_ELEMENT_ID);
        // WHEN
        final long after = underTest.getLastAccess();
        // THEN
        Assert.assertTrue(before <= now);
        Assert.assertTrue(now <= after);
    }

    public void testGetEnemiesWhenEnemiesIsNullShouldReturnEmptyMap() {
        // GIVEN
        Whitebox.setInternalState(underTest, "enemies", (Object) null);
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = underTest.getEnemies();
        // THEN
        Assert.assertTrue(returned.isEmpty());
    }

    public void testGetEnemiesWhenEnemiesIsNotNullShouldReturnMapOfClonedEnemies() throws CloneNotSupportedException {
        // GIVEN
        final Map<String, Enemy> enemies = new HashMap<>();
        final Enemy enemy = mockControl.createMock(Enemy.class);
        final Enemy clonedEnemy = mockControl.createMock(Enemy.class);
        enemies.put("a", enemy);
        expect(enemy.clone()).andReturn(clonedEnemy);
        Whitebox.setInternalState(underTest, "enemies", enemies);
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = underTest.getEnemies();
        // THEN
        Assert.assertSame(returned.get("a"), clonedEnemy);
    }

    @Test(expectedExceptions = CloneFailedException.class)
    public void testGetEnemiesWhenCloneFailsShouldLogErrorAndThrowException() throws CloneNotSupportedException {
        // GIVEN
        final Map<String, Enemy> enemies = new HashMap<>();
        final Enemy enemy = mockControl.createMock(Enemy.class);
        enemies.put("a", enemy);
        expect(enemy.clone()).andThrow(new CloneNotSupportedException());
        expect(enemy.getName()).andReturn("name");
        logger.error("Failed to clone {} '{}' from book '{}'!", "name", "a", String.valueOf(BOOK_ID));
        Whitebox.setInternalState(underTest, "enemies", enemies);
        mockControl.replay();
        // WHEN
        underTest.getEnemies();
        // THEN throws exception
    }

    public void testResolveParagraphIdWhenParagraphExistsShouldReturnResolvedId() {
        // GIVEN
        expect(paragraph.getDisplayId()).andReturn(EXISTENT_ELEMENT_DISPLAY);
        mockControl.replay();
        // WHEN
        final String returned = underTest.resolveParagraphId(EXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertEquals(returned, EXISTENT_ELEMENT_DISPLAY);
    }

    public void testResolveParagraphIdWhenParagraphDoesNotExistsShouldReturnOriginalId() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.resolveParagraphId(NONEXISTENT_ELEMENT_ID);
        // THEN
        Assert.assertEquals(returned, NONEXISTENT_ELEMENT_ID);
    }

}
