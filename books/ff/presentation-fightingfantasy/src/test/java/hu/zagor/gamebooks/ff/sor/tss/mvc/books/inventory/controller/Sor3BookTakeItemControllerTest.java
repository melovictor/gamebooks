package hu.zagor.gamebooks.ff.sor.tss.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Sor3BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Sor3BookTakeItemControllerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Sor3BookTakeItemController underTest;

    @Inject private LocaleProvider provider;
    @Inject private MessageSource messageSource;
    @Inject private DiceResultRenderer renderer;
    @Inject private RandomNumberGenerator generator;
    @Mock private HttpServletRequest request;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Paragraph paragraph;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    @Mock private SorCharacter character;
    @Inject private MarketHandler marketHandler;
    @Inject private ItemInteractionRecorder itemInteractionRecorder;
    @Mock private MarketCommand marketCommand;
    private BuySellResponse buySellResponse;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(4);
        Whitebox.setInternalState(underTest, "info", info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
    }

    public void testDoHandleMarketBuyWhenOnRandomSectionDoUsual() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("75");
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getItemsToProcess()).andReturn(Arrays.asList(new ProcessableItemHolder(marketCommand)));
        expect(marketHandler.handleMarketPurchase("3005", character, marketCommand, characterHandler)).andReturn(buySellResponse);
        itemInteractionRecorder.recordItemMarketMovement(wrapper, "Sale", "3005");
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.doHandleMarketBuy(request, "3005");
        // THEN
        Assert.assertSame(returned, buySellResponse);
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
