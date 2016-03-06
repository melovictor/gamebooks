package hu.zagor.gamebooks.mvc.book.image.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.controller.ImageHandler;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
    @UnderTest private GenericBookImageController underTest;
    @MockControl private IMocksControl mockControl;
    private final Locale locale = new Locale(HU);
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Inject private ImageHandler imageHandler;
    @Mock private ImageLocation imageLocation;

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

    public void testHandleImageWhenParametersAreSetShouldCallImageHandler() throws IOException {
        // GIVEN
        expect(imageHandler.createImageLocation(DIR, FILE, locale)).andReturn(imageLocation);
        imageHandler.handleImage(request, response, imageLocation, false);
        mockControl.replay();
        // WHEN
        underTest.handleImage(request, response, DIR, FILE, locale);
        // THEN
    }

}
