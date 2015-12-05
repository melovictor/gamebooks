package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.books.saving.domain.IgnoreField;
import java.util.Arrays;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link ClassFieldFilter} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultClassFieldFilter implements ClassFieldFilter {

    @Override
    public boolean isIgnorableField(final Object parsed, final String nodeName) {
        final IgnoreField ignoreField = parsed.getClass().getAnnotation(IgnoreField.class);
        boolean ignorableField = false;
        if (ignoreField != null) {
            ignorableField = Arrays.asList(ignoreField.value().split(",")).contains(nodeName);
        }
        return ignorableField;
    }

}
