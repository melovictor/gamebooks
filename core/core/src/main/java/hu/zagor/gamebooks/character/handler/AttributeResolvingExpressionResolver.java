package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Basic implementation of the {@link ExpressionResolver} interface which uses the provided {@link Character} object's attributes to resolve the expression.
 * @author Tamas_Szekeres
 */
@Component
public class AttributeResolvingExpressionResolver implements ExpressionResolver {
    private static final Pattern RESOLVE_PROPERTIES = Pattern.compile("([a-zA-Z]+)");
    @LogInject private Logger logger;

    @Override
    public int resolveValue(final Character character, final String expression) {
        String resolvingExpression = expression.trim();

        resolvingExpression = resolveAttributes(resolvingExpression, character);
        return ((Number) MVEL.eval(resolvingExpression)).intValue();
    }

    private String resolveAttributes(final String expression, final Character character) {
        final Matcher matcher = RESOLVE_PROPERTIES.matcher(expression);
        String resolvedExpresssion = expression;

        while (matcher.find()) {
            final String property = matcher.group(1);
            try {
                Integer value = getFieldValue(character, property, true);
                for (final Item item : character.getEquipment()) {
                    value += getItemFieldValue(item, property);
                }

                if (!expression.startsWith("initial")) {
                    int initialValue;
                    try {
                        initialValue = Integer.valueOf(resolveAttributes("initial" + StringUtils.capitalize(property), character));
                    } catch (final IllegalArgumentException ex) {
                        initialValue = Integer.MAX_VALUE;
                    }
                    if (initialValue < value) {
                        value = initialValue;
                    }
                }

                resolvedExpresssion = resolvedExpresssion.replace(property, value.toString());
            } catch (final ReflectiveOperationException e) {
                logger.error("Cannot resolve property '{}'.", property);
                throw new IllegalArgumentException(e);
            }
        }

        return resolvedExpresssion;
    }

    private int getItemFieldValue(final Item item, final String property) throws IllegalArgumentException, ReflectiveOperationException {
        int value = 0;
        if (item.getEquipInfo().isEquipped()) {
            value = getFieldValue(item, property, false);
        }
        return value;
    }

    private int getFieldValue(final Object object, final String property, final boolean triggerException) throws IllegalArgumentException, ReflectiveOperationException {
        final Method getter = ReflectionUtils.findMethod(object.getClass(), "get" + StringUtils.capitalize(property));
        int value = 0;
        if (getter != null) {
            final Object resolved = getter.invoke(object);
            if (resolved instanceof Enum) {
                value = ((Enum<?>) resolved).ordinal();
            } else {
                value = (int) resolved;
            }
        } else if (triggerException) {
            throw new NoSuchFieldException(property);
        }
        return value;
    }

}
