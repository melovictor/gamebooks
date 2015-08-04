package hu.zagor.gamebooks.ff.mvc.book.image.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.controller.ImageHandler;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.image.ImageLookupStrategyType;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookImageController}.
 * @author Tamas_Szekeres
 */
@Test
public class FfBookImageControllerTest {

    private FfBookImageController underTest;
    private IMocksControl mockControl;
    private ImageHandler imageHandler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private BeanFactory beanFactory;
    private Locale locale;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private ServletOutputStream outputStream;
    private ImageLocation imageLocation;
    private PlayerUser player;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        imageHandler = mockControl.createMock(ImageHandler.class);
        request = mockControl.createMock(HttpServletRequest.class);
        response = mockControl.createMock(HttpServletResponse.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        locale = Locale.GERMAN;
        session = mockControl.createMock(HttpSession.class);
        outputStream = mockControl.createMock(ServletOutputStream.class);
        imageLocation = new ImageLocation("ff21", "small*", locale);
        player = new PlayerUser(11, "FireFoX", false);
        player.getSettings().setImageTypeOrder("bwFirst");
        wrapper = mockControl.createMock(HttpSessionWrapper.class);

        underTest = new FfBookImageController();
        underTest.setImageHandler(imageHandler);
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setHandleSmallImageWhenDirIsEmptyShouldThrowException() throws IOException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleSmallImage(request, response, "", locale);
        // THEN throws exception
    }

    public void testHandleSmallImageShouldRequestSmallImageAndWriteItIntoResponse() throws IOException {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(response.getOutputStream()).andReturn(outputStream);
        expect(imageHandler.createImageLocation("ff21", "small*", locale)).andReturn(imageLocation);
        expect(wrapper.getPlayer()).andReturn(player);
        imageHandler.handleImage(outputStream, imageLocation, ImageLookupStrategyType.BW_COLOR, true);

        mockControl.replay();
        // WHEN
        underTest.handleSmallImage(request, response, "ff21", locale);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
