package hu.zagor.gamebooks.mvc.login.facade;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ZagorLoginFacade}.
 * @author Tamas_Szekeres
 */
@Test
public class ZagorLoginFacadeTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ZagorLoginFacade underTest;
    @Mock private Authentication authentication;
    @Inject private Logger logger;
    @Inject private ServerCommunicator communcator;
    @Instance(inject = true) private Map<String, String> authorizationCodeContainer;
    @Mock private URLConnection connection;

    @BeforeClass
    public void setUpClass() {
        authorizationCodeContainer.put("login", "{login}");
    }

    public void testAuthenticateWhenLoginSucceedsAndResponseIsProvidedShouldRecordRewardsAndReturnNewAuthentication() throws IOException {
        // GIVEN
        expect(authentication.getPrincipal()).andReturn("FireFoX");
        expect(communcator.compilePostData("username", "FireFoX", null)).andReturn("username=FireFoX");
        expect(authentication.getCredentials()).andReturn("password");
        expect(communcator.compilePostData("password", "password", "username=FireFoX")).andReturn("username=FireFoX&password=password");
        expect(communcator.compilePostData("authCode", "{login}", "username=FireFoX&password=password")).andReturn("username=FireFoX&password=password&authCode={login}");
        expect(communcator.connect("http://zagor.hu/remoteloginxml.php")).andReturn(connection);
        communcator.sendRequest(connection, "username=FireFoX&password=password&authCode={login}");
        expect(connection.getInputStream()).andReturn(new ByteArrayInputStream(
            ("<?xml version=\"1.0\" encoding=\"utf-8\" ?><login><status>ok</status><user id='29' roles='USER,ADMIN,TEST'><reward bookId='990133001' code='tablet' />"
                + "<reward bookId='990133006' code='bush' /><reward bookId='990162001' code='goldpieces' /><reward bookId='990162001' code='gyemant' />"
                + "<reward bookId='990162001' code='mino' /><reward bookId='990162001' code='smaragd' /><reward bookId='990162001' code='zafir' />"
                + "<reward bookId='990162002' code='painting' /><reward bookId='990162002' code='shield' /><reward bookId='990162004' code='feather' />"
                + "<reward bookId='990162008' code='arrow' /><reward bookId='990162008' code='candle' /><reward bookId='990162008' code='egg' />"
                + "<reward bookId='990162008' code='switch' /><reward bookId='990162008' code='wdragon' /><reward bookId='990162008' code='yeti' />"
                + "<reward bookId='990162009' code='ring1' /><reward bookId='990162009' code='ring2' /><reward bookId='990162009' code='ring3' />"
                + "<reward bookId='990162009' code='ring4' /><reward bookId='990162009' code='ring5' /><reward bookId='990162009' code='ring6' /><"
                + "reward bookId='990162009' code='ring7' /><reward bookId='990162009' code='ring8' /><reward bookId='990162009' code='ring9' />"
                + "<reward bookId='990162014' code='bat' /><reward bookId='990162014' code='bullshit' /><reward bookId='990162014' code='cadaver' />"
                + "<reward bookId='990162014' code='chair' /><reward bookId='990162014' code='dagger' /><reward bookId='990162014' code='demon' />"
                + "<reward bookId='990162014' code='ghost' /><reward bookId='990162014' code='ironkey' /><reward bookId='990162014' code='mirror' />"
                + "<reward bookId='990162014' code='skeletoncl' /><reward bookId='990162014' code='whiteman' /><reward bookId='990162017' code='tomb1' />"
                + "<reward bookId='990231023' code='bookofheal' /><reward bookId='990231023' code='bookofswor' /><reward bookId='990231023' code='coach' />"
                + "<reward bookId='990231023' code='mail' /><reward bookId='990231023' code='stickAndCr' /><reward bookId='990231023' code='swordAndSh' />"
                + "<reward bookId='990231026' code='apple' /><reward bookId='990231026' code='wwolf' /><reward bookId='990586001' code='flanker' />"
                + "<reward bookId='990586001' code='plague' /><reward bookId='990586002' code='kharekey' /><reward bookId='990586002' code='snakering' />"
                + "<reward bookId='990586003' code='air' /><reward bookId='990586003' code='earth' /><reward bookId='990586003' code='fire' />"
                + "<reward bookId='990586003' code='moon' /><reward bookId='990586003' code='serpentsta' /><reward bookId='990586003' code='sun' />"
                + "<reward bookId='990586003' code='time' /><reward bookId='990586003' code='water' /><reward bookId='990934000' code='logo1' />"
                + "<reward bookId='990934000' code='logo2' /><reward bookId='990934000' code='logo3' /><reward bookId='990934000' code='logo4' /></user></login>")
                    .getBytes()));
        expect(authentication.getPrincipal()).andReturn("FireFoX");
        mockControl.replay();
        // WHEN
        final Authentication returned = underTest.authenticate(authentication);
        // THEN
        Assert.assertTrue(returned instanceof PlayerUser);
        final PlayerUser returnedUser = (PlayerUser) returned;
        Assert.assertEquals(returned.getPrincipal(), "FireFoX");
        Assert.assertTrue(returned.getAuthorities().contains(PlayerUser.USER));
        Assert.assertTrue(returned.getAuthorities().contains(PlayerUser.ADMIN));
        Assert.assertTrue(returned.getAuthorities().contains(PlayerUser.TEST));
        Assert.assertTrue(returnedUser.isTester());
        Assert.assertTrue(returnedUser.isAdmin());
        Assert.assertEquals(returnedUser.getId(), 29);
        Assert.assertTrue(returnedUser.getRewards().containsKey(990231026L));
        Assert.assertTrue(returnedUser.getRewards().get(990231026L).contains("apple"));
    }

}
