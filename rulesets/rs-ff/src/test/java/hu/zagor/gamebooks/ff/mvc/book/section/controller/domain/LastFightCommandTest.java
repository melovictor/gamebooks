package hu.zagor.gamebooks.ff.mvc.book.section.controller.domain;

import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link LastFightCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class LastFightCommandTest {

    public void testConstructor() throws Exception {
        // GIVEN
        // WHEN
        final Constructor<LastFightCommand> constructor = LastFightCommand.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
            Assert.fail();
        } catch (final InvocationTargetException ex) {
            // THEN
            final Throwable cause = ex.getCause();
            if (!(cause instanceof UnsupportedOperationException)) {
                Assert.fail();
            }
        }
    }
}
