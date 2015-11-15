package hu.zagor.gamebooks.ff.mvc.book.image.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.controller.ImageHandler;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.image.ImageLookupStrategyType;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.IMocksControl;
import org.easymock.Mock;
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

    @UnderTest
    private FfBookImageController underTest;
    @MockControl
    private IMocksControl mockControl;
    @Inject
    private ImageHandler imageHandler;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Inject
    private BeanFactory beanFactory;
    private Locale locale;
    @Mock
    private HttpSession session;
    @Mock
    private HttpSessionWrapper wrapper;
    @Mock
    private ServletOutputStream outputStream;
    private ImageLocation imageLocation;
    private PlayerUser player;

    @BeforeClass
    public void setUpClass() {
        locale = Locale.GERMAN;
        imageLocation = new ImageLocation("ff21", "small*", locale);
        player = new PlayerUser(11, "FireFoX", false);
        player.getSettings().setImageTypeOrder("bwFirst");
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
