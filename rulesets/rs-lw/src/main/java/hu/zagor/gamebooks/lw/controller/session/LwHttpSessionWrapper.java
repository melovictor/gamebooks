package hu.zagor.gamebooks.lw.controller.session;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Extension to the {@link HttpSessionWrapper} which can store and return the previous paragraph as well.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class LwHttpSessionWrapper extends HttpSessionWrapper {

    /**
     * Creates a new {@link LwHttpSessionWrapper} around the given {@link HttpServletRequest} bean.
     * @param request the bean to wrap, cannot be null
     */
    public LwHttpSessionWrapper(final HttpServletRequest request) {
        super(request);
    }

    public Paragraph getPreviousParagraph() {
        return (Paragraph) getSession().getAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY + "_prev_" + getBookId());
    }

    /**
     * Stores the given {@link Paragraph} bean in the session as the previous paragraph, then returns it for further use.
     * @param paragraph the {@link Paragraph} to store
     * @return the same {@link Paragraph} bean
     */
    public Paragraph setPreviousParagraph(final Paragraph paragraph) {
        getSession().setAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY + "_prev_" + getBookId(), paragraph);
        return paragraph;
    }

    @Override
    public LwCharacter getCharacter() {
        return (LwCharacter) super.getCharacter();
    }

}
