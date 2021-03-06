package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper;
import hu.zagor.gamebooks.books.saving.xml.exception.UnknownFieldTypeException;
import hu.zagor.gamebooks.support.logging.LoggerInjector;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Default implementation of the {@link XmlGameStateLoader} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultXmlGameStateLoader extends AbstractGameStateHandler implements XmlGameStateLoader {

    private static final String TRUE = "true";
    private static final String VALUE = "value";

    @Autowired private DocumentBuilderFactory builderFactory;
    @Autowired private LoggerInjector loggerInjector;
    @Autowired private ClassFieldFilter classFieldFilter;

    @Override
    public Object load(final String content) {
        Object parsed = null;
        final Map<String, Object> objectCache = new HashMap<>();
        try {
            final StringReader stringReader = (StringReader) getBeanFactory().getBean("stringReader", content);
            final InputSource inputSource = (InputSource) getBeanFactory().getBean("inputSource", stringReader);
            final Document document = builderFactory.newDocumentBuilder().parse(inputSource);

            final Node mainObjectNode = document.getDocumentElement();
            final SavedGameMapWrapper wrapper = (SavedGameMapWrapper) getInstance(getAttributeValue(mainObjectNode, "class"));
            initInstance(wrapper, mainObjectNode, objectCache);
            parsed = wrapper.getElement();
        } catch (final Exception exception) {
            getLogger().error("Failed to load saved game, the deserializer threw an exception.", exception);
        }
        return parsed;
    }

    private String getAttributeValue(final Node mainObjectNode, final String attributeName) {
        final Node namedItem = mainObjectNode.getAttributes().getNamedItem(attributeName);
        return namedItem == null ? null : namedItem.getNodeValue();
    }

    private void initInstance(final Object parsed, final Node objectNode, final Map<String, Object> objectCache) throws Exception {
        final NodeList childNodes = objectNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                populateField(parsed, childNode, objectCache);
            }
        }
    }

    private void populateField(final Object parsed, final Node fieldNode, final Map<String, Object> objectCache) throws Exception {
        final String nodeName = fieldNode.getNodeName();
        final Field field = getDeclaredField(parsed.getClass(), nodeName);
        if (field == null && !classFieldFilter.isIgnorableField(parsed, nodeName)) {
            throw new NoSuchFieldException("The field '" + nodeName + "' doesn't exists.");
        }
        final Object fieldObject = getFieldObjectFromNode(fieldNode, objectCache);
        setField(parsed, field, fieldObject);
    }

    private Field getDeclaredField(final Class<? extends Object> klass, final String nodeName) {
        return ReflectionUtils.findField(klass, nodeName);
    }

    private Object getFieldObjectFromNode(final Node fieldNode, final Map<String, Object> objectCache) throws Exception {
        Object fieldObject;
        if (TRUE.equals(getAttributeValue(fieldNode, "isNull"))) {
            fieldObject = null;
        } else {
            final boolean isList = TRUE.equals(getAttributeValue(fieldNode, "isList"));
            final boolean isMap = TRUE.equals(getAttributeValue(fieldNode, "isMap"));
            final boolean isEnum = TRUE.equals(getAttributeValue(fieldNode, "isEnum"));
            final String className = getAttributeValue(fieldNode, "class");
            final String ref = getAttributeValue(fieldNode, "ref");
            if (objectCache.containsKey(ref)) {
                fieldObject = objectCache.get(ref);
            } else if (isEnum) {
                final String enumValue = getAttributeValue(fieldNode, VALUE);
                @SuppressWarnings({"rawtypes", "unchecked"})
                final Class<Enum> forName = (Class<Enum>) Class.forName(className);
                @SuppressWarnings("unchecked")
                final Object tempFieldObject = Enum.valueOf(forName, enumValue);
                fieldObject = tempFieldObject;
            } else if (isSimpleType(className)) {
                fieldObject = getSimpleType(className, fieldNode);
            } else {
                if (isList || isMap) {
                    if (isList) {
                        fieldObject = getList(className, fieldNode, objectCache);
                    } else {
                        fieldObject = getMap(className, fieldNode, objectCache, ref);
                    }
                } else {
                    fieldObject = getInstance(getAttributeValue(fieldNode, "class"));
                    initInstance(fieldObject, fieldNode, objectCache);
                }
            }
            if (ref != null) {
                objectCache.put(ref, fieldObject);
            }
        }
        return fieldObject;
    }

    @SuppressWarnings("unchecked")
    private Object getMap(final String className, final Node fieldNode, final Map<String, Object> objectCache, final String ref) throws Exception {
        final Map<Object, Object> map = (Map<Object, Object>) this.getInstance(className);
        if (ref != null) {
            objectCache.put(ref, map);
        }

        final NodeList childNodes = fieldNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                final Object key = getNode(childNode, "key", objectCache);
                final Object value = getNode(childNode, VALUE, objectCache);
                map.put(key, value);
            }
        }

        return map;
    }

    private Object getNode(final Node node, final String string, final Map<String, Object> objectCache) throws Exception {
        Object parsedObject = null;
        final NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && string.equals(childNode.getNodeName())) {
                parsedObject = getFieldObjectFromNode(childNode, objectCache);
            }
        }
        return parsedObject;
    }

    private Object getList(final String className, final Node fieldNode, final Map<String, Object> objectCache) throws Exception {
        @SuppressWarnings("unchecked")
        final Collection<Object> list = (Collection<Object>) this.getInstance(className);

        final NodeList childNodes = fieldNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                list.add(getFieldObjectFromNode(childNode, objectCache));
            }
        }
        getBeanFactory().autowireBean(list);

        return list;
    }

    private Object getSimpleType(final String className, final Node fieldNode) throws Exception {
        final Node valueNode = fieldNode.getChildNodes().item(0);
        final String nodeValue = valueNode == null ? "" : valueNode.getNodeValue();
        Object parsedValue = null;
        if (className.contains("String")) {
            parsedValue = nodeValue;
        } else if ("java.lang.Integer".equals(className)) {
            parsedValue = Integer.valueOf(nodeValue);
        } else if ("java.lang.Long".equals(className)) {
            parsedValue = Long.valueOf(nodeValue);
        } else if ("java.lang.Double".equals(className)) {
            parsedValue = Double.valueOf(nodeValue);
        } else if ("java.lang.Boolean".equals(className)) {
            parsedValue = Boolean.valueOf(nodeValue);
        } else {
            throw new UnknownFieldTypeException(className);
        }
        return parsedValue;
    }

    private void setField(final Object parsed, final Field field, final Object parsedValue) throws ReflectiveOperationException {
        if (field != null) {
            if (parsedValue == null || ClassUtils.isAssignable(field.getType(), parsedValue.getClass())) {
                field.setAccessible(true);
                field.set(parsed, parsedValue);
                field.setAccessible(false);
            } else {
                final String setterName = "set" + StringUtils.capitalize(field.getName());
                final Method setterMethod = ReflectionUtils.findMethod(parsed.getClass(), setterName, parsedValue.getClass());
                if (setterMethod == null) {
                    getLogger().error("Couldn't find setter method '{}' in class '{}' for type '{}'.", setterName, parsed.getClass(), parsedValue.getClass());
                    throw new IllegalStateException();
                }
                setterMethod.invoke(parsed, parsedValue);
            }
        }
    }

    private boolean isSimpleType(final String className) {
        return className.startsWith("java.lang.");
    }

    private Object getInstance(final String className) throws ReflectiveOperationException {
        getLogger().debug("Creating new instance of class " + className);
        final Class<?> clazz = Class.forName(className);
        final Constructor<?> defaultConstructor;
        try {
            defaultConstructor = clazz.getDeclaredConstructor();
        } catch (final ReflectiveOperationException ex) {
            getLogger().error("Failed to grab default constructor for class '{}'.", className);
            throw ex;
        }
        defaultConstructor.setAccessible(true);
        final Object createdInstance = defaultConstructor.newInstance();
        defaultConstructor.setAccessible(false);

        if (createdInstance instanceof BeanFactoryAware) {
            ((BeanFactoryAware) createdInstance).setBeanFactory(getBeanFactory());
        }
        loggerInjector.postProcessBeforeInitialization(createdInstance, null);

        return createdInstance;

    }

}
