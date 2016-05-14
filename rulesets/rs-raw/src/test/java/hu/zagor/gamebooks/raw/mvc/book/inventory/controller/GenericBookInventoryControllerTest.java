package hu.zagor.gamebooks.raw.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.inventory.service.BookInventoryService;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookInventoryController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookInventoryControllerTest {

    private static final String NOTES = "These are the notes I want to save...";
    @UnderTest private GenericBookInventoryController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Instance private Character character;
    @Inject private BeanFactory beanFactory;
    @Mock private Model model;
    private BookInformations info;
    @Mock private HttpSession session;
    @Mock private HttpSessionWrapper wrapper;
    @Inject private SectionHandlingService sectionHandlingService;
    @Mock private RawCharacterPageData characterPageData;
    @Inject private ApplicationContext applicationContext;
    @Instance private Map<Long, String> idBeanMap;
    @Mock private BookInventoryService inventoryService;
    @Inject private BookInformationFetcher bookInformationFetcher;

    @BeforeClass
    public void setUpClass() {
        idBeanMap.put(2L, "rawBookInventoryService");
        info = new BookInformations(2000L);
        Whitebox.setInternalState(underTest, "inventoryControllerIdBeanNameMap", idBeanMap);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testHandleInventoryWhenCannotGetInfoShouldThrowException() {
        // GIVEN
        response.setCharacterEncoding("UTF-8");
        expect(bookInformationFetcher.getInfoById(3000)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.handleInventory(model, request, response, 3000L);
        // THEN throws exception
    }

    public void testHandleInventoryWhenServiceBeanNameIsSpecifiedShouldFetchAndExecuteServiceCall() {
        // GIVEN
        response.setCharacterEncoding("UTF-8");
        expect(bookInformationFetcher.getInfoById(2000)).andReturn(info);
        expect(beanFactory.getBean("rawBookInventoryService")).andReturn(inventoryService);
        expectWrapper();
        expect(inventoryService.handleInventory(model, wrapper, info)).andReturn("rawSection");
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleInventory(model, request, response, 2000L);
        // THEN
        Assert.assertEquals(returned, "rawSection");
    }

    public void testSaveNotesShouldSaveNotes() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        mockControl.replay();
        // WHEN
        underTest.saveNotes(request, NOTES);
        // THEN
        Assert.assertEquals(character.getNotes().getNote(), NOTES);
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
