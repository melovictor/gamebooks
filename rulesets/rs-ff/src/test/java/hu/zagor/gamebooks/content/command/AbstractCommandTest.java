package hu.zagor.gamebooks.content.command;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Abstract class for unit testing the clone method of {@link Command} child classes.
 * @author Tamas_Szekeres
 */
public class AbstractCommandTest {

    /**
     * Expect the cloning of a {@link TrueCloneable} object.
     * @param cloneable the object to clone
     * @param cloned the cloned object
     * @throws CloneNotSupportedException when there is a problem
     */
    protected void expectTc(final TrueCloneable cloneable, final TrueCloneable cloned) throws CloneNotSupportedException {
        expect(cloneable.clone()).andReturn(cloned);
    }
}
