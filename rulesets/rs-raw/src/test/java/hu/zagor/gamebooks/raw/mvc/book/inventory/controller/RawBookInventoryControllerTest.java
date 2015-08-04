package hu.zagor.gamebooks.raw.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookInventoryController}.
 * @author Tamas_Szekeres
 */
@Test
public class RawBookInventoryControllerTest {

    private static final String NOTES = "These are the notes I want to save...";
    private RawBookInventoryController underTest;
    private IMocksControl mockControl;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Character character;
    private BeanFactory beanFactory;
    private Model model;
    private BookInformations info;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private PlayerUser player;
    private SectionHandlingService sectionHandlingService;
    private RawCharacterPageData characterPageData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        request = mockControl.createMock(HttpServletRequest.class);
        response = mockControl.createMock(HttpServletResponse.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        character = new Character();
        model = mockControl.createMock(Model.class);
        info = new BookInformations(1L);
        session = mockControl.createMock(HttpSession.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        player = new PlayerUser(99, "FireFoX", false);
        sectionHandlingService = mockControl.createMock(SectionHandlingService.class);
        characterPageData = mockControl.createMock(RawCharacterPageData.class);

        underTest = new RawBookInventoryController();
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "sectionHandlingService", sectionHandlingService);
        Whitebox.setInternalState(underTest, "info", info);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testHandleInventoryShouldReturnCharPageTileNameWithResourceDirName() {
        // GIVEN
        response.setCharacterEncoding("UTF-8");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        sectionHandlingService.initModel(model, player, info);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(beanFactory.getBean("rawCharacterPageData", character)).andReturn(characterPageData);
        expect(model.addAttribute("charEquipments", characterPageData)).andReturn(model);
        expect(model.addAttribute("bookInfo", info)).andReturn(model);

        mockControl.replay();
        // WHEN
        final String returned = underTest.handleInventory(model, request, response);
        // THEN
        Assert.assertEquals(returned, "rawCharPage");
    }

    public void testSaveNotesShouldSaveNotes() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getCharacter()).andReturn(character);
        mockControl.replay();
        // WHEN
        underTest.saveNotes(request, NOTES);
        // THEN
        Assert.assertEquals(character.getNotes().getNote(), NOTES);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
