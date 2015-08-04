package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;

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
public class FfBookInventoryControllerTest {

    private FfBookInventoryController underTest;
    private IMocksControl mockControl;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FfCharacter character;
    private BeanFactory beanFactory;
    private Model model;
    private FfBookInformations info;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private PlayerUser player;
    private SectionHandlingService sectionHandlingService;
    private FfCharacterPageData characterPageData;
    private FfCharacterHandler characterHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        request = mockControl.createMock(HttpServletRequest.class);
        response = mockControl.createMock(HttpServletResponse.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        character = new FfCharacter();
        model = mockControl.createMock(Model.class);
        characterHandler = mockControl.createMock(FfCharacterHandler.class);
        info = new FfBookInformations(1L);
        info.setResourceDir("ff21");
        info.setCharacterHandler(characterHandler);
        session = mockControl.createMock(HttpSession.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        player = new PlayerUser(99, "FireFoX", false);
        sectionHandlingService = mockControl.createMock(SectionHandlingService.class);
        characterPageData = mockControl.createMock(FfCharacterPageData.class);

        underTest = new FfBookInventoryController();
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
        expect(beanFactory.getBean("ffCharacterPageData", character, characterHandler)).andReturn(characterPageData);
        expect(model.addAttribute("charEquipments", characterPageData)).andReturn(model);
        expect(model.addAttribute("bookInfo", info)).andReturn(model);

        mockControl.replay();
        // WHEN
        final String returned = underTest.handleInventory(model, request, response);
        // THEN
        Assert.assertEquals(returned, "ffCharPage.ff21");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
