package hu.zagor.gamebooks.character.handler.attribute;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultAttributeHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultAttributeHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private DefaultAttributeHandler underTest;
    @Inject private ExpressionResolver expressionResolver;
    @Mock private Character character;

    public void testResolveValueShouldCallExpressionResolver() {
        // GIVEN
        expect(expressionResolver.resolveValue(character, "stamina")).andReturn(7);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "stamina");
        // THEN
        Assert.assertEquals(returned, 7);
    }

}
