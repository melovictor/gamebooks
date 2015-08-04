package hu.zagor.gamebooks.mvc.book.image.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.controller.ImageHandler;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.image.ImageLookupStrategyType;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookImageController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookImageControllerTest {

    private static final String HU = "hu";
    private static final String DIR = "dir";
    private static final String FILE = "file";
    private GenericBookImageController underTest;
    private IMocksControl mockControl;
    private final Locale locale = new Locale(HU);
    private HttpServletRequest request;
    private HttpServletResponse response;
    private BeanFactory beanFactory;
    private ImageHandler imageHandler;
    private HttpSession session;
    private HttpSessionWrapper sessionWrapper;
    private ServletOutputStream outputStream;
    private ImageLocation imageLocation;
    private PlayerUser player;
    private PlayerSettings settings;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        request = mockControl.createMock(HttpServletRequest.class);
        response = mockControl.createMock(HttpServletResponse.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        imageHandler = mockControl.createMock(ImageHandler.class);
        session = mockControl.createMock(HttpSession.class);
        sessionWrapper = mockControl.createMock(HttpSessionWrapper.class);
        outputStream = mockControl.createMock(ServletOutputStream.class);
        imageLocation = mockControl.createMock(ImageLocation.class);
        player = mockControl.createMock(PlayerUser.class);
        settings = new PlayerSettings();
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new GenericBookImageController();
        underTest.setBeanFactory(beanFactory);
        underTest.setImageHandler(imageHandler);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleImageWhenFileIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, DIR, null, locale);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleImageWhenFileIsEmptyShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, DIR, "", locale);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleImageWhenDirIsNullShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, null, FILE, locale);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleImageWhenDirIsEmptyShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, "", FILE, locale);
        // THEN throws exception
    }

    public void testHandleImageWhenParametersAreSetAndIsBwCallImageHandlerWithBw() throws IOException {
        // GIVEN
        settings.setImageTypeOrder("bwFirst");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(response.getOutputStream()).andReturn(outputStream);
        expect(imageHandler.createImageLocation(DIR, FILE, locale)).andReturn(imageLocation);
        expect(sessionWrapper.getPlayer()).andReturn(player);
        expect(player.getSettings()).andReturn(settings);

        imageHandler.handleImage(outputStream, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, DIR, FILE, locale);
        // THEN
    }

    public void testHandleImageWhenParametersAreSetAndIsColCallImageHandlerWithCol() throws IOException {
        // GIVEN
        settings.setImageTypeOrder("colFirst");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(response.getOutputStream()).andReturn(outputStream);
        expect(imageHandler.createImageLocation(DIR, FILE, locale)).andReturn(imageLocation);
        expect(sessionWrapper.getPlayer()).andReturn(player);
        expect(player.getSettings()).andReturn(settings);

        imageHandler.handleImage(outputStream, imageLocation, ImageLookupStrategyType.COLOR_BW, false);
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, DIR, FILE, locale);
        // THEN
    }

    public void testHandleImageWhenParametersAreSetAndIsImageTypeIsUnknownCallImageHandlerWithBw() throws IOException {
        // GIVEN
        settings.setImageTypeOrder("");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(response.getOutputStream()).andReturn(outputStream);
        expect(imageHandler.createImageLocation(DIR, FILE, locale)).andReturn(imageLocation);
        expect(sessionWrapper.getPlayer()).andReturn(player);
        expect(player.getSettings()).andReturn(settings);

        imageHandler.handleImage(outputStream, imageLocation, ImageLookupStrategyType.BW_COLOR, false);
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, DIR, FILE, locale);
        // THEN
    }

    public void testGetImageHandlerShouldReturnImageHandler() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final ImageHandler returned = underTest.getImageHandler();
        // THEN
        Assert.assertSame(returned, imageHandler);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
