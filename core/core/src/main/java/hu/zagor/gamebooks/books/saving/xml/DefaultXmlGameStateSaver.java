package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.books.saving.xml.domain.SavedGameMapWrapper;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link XmlGameStateSaver} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultXmlGameStateSaver extends AbstractGameStateHandler implements XmlGameStateSaver {

    private static final String MAP_ENTRY = "mapEntry";
    private static final String VALUE = "value";
    private static final String CLASS = "class";

    @Override
    public String save(final Object object) {
        Assert.notNull(object, "The parameter 'object' cannot be null!");

        String content = null;
        try {
            final XmlNodeWriter writer = getBeanFactory().getBean(DefaultXmlNodeWriter.class);
            final List<Object> objectCache = new ArrayList<>();

            saveFields(writer, new SavedGameMapWrapper(object), "mainObject", objectCache);

            writer.closeWriter();
            content = writer.getContent();
        } catch (final XMLStreamException | IllegalArgumentException | IllegalAccessException | UnsupportedEncodingException exception) {
            getLogger().error("Failed to save game, the serializer threw an exception.", exception);
        }
        return content;
    }

    private void saveFields(final XmlNodeWriter writer, final Object object, final String objectName, final List<Object> objectCache)
        throws XMLStreamException, IllegalAccessException {
        final Class<?> clazz = object.getClass();
        writer.openNode(objectName);
        writer.addAttribute(CLASS, clazz.getName());
        Integer ref = getRefByKey(objectCache, object);
        if (ref != null) {
            getLogger().debug("Saving class reference " + ref);
            writer.addAttribute("ref", ref.toString());
        } else {
            ref = objectCache.size();
            objectCache.add(object);
            getLogger().debug("Saving class " + clazz.getName());
            writer.addAttribute("ref", ref.toString());

            final Field[] fields = getAllFields(clazz);
            AccessibleObject.setAccessible(fields, true);
            for (final Field field : fields) {
                if (serializable(field)) {
                    final String fieldName = field.getName();
                    final Object fieldValue = field.get(object);

                    getLogger().debug("Saving field '" + fieldName + "' with value '" + fieldValue + "'.");
                    saveField(writer, fieldName, fieldValue, objectCache);
                }
            }
        }

        writer.closeNode(objectName);
    }

    private boolean serializable(final Field field) {
        final int fieldModifiers = field.getModifiers();
        final boolean notTransient = !Modifier.isTransient(fieldModifiers);
        final boolean notStatic = !Modifier.isStatic(fieldModifiers);
        final boolean notLogger = !field.isAnnotationPresent(LogInject.class);
        final boolean notSpringClass = !field.getType().getName().contains("springframework");
        return notTransient && notStatic && notLogger && notSpringClass;
    }

    private Field[] getAllFields(final Class<?> type) {
        final List<Field> fields = new ArrayList<Field>();
        getAllFields(fields, type);
        return fields.toArray(new Field[]{});
    }

    private void getAllFields(final List<Field> fields, final Class<?> type) {
        for (final Field field : type.getDeclaredFields()) {
            fields.add(field);
        }

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
    }

    private void saveField(final XmlNodeWriter writer, final String fieldName, final Object fieldValue, final List<Object> objectCache)
        throws IllegalAccessException, XMLStreamException {
        if (fieldValue == null) {
            writer.createSimpleNode(fieldName);
        } else {
            saveNonNullField(writer, fieldName, fieldValue, objectCache);
        }
    }

    private void saveNonNullField(final XmlNodeWriter writer, final String fieldName, final Object fieldValue, final List<Object> objectCache)
        throws XMLStreamException, IllegalAccessException {
        final Class<?> fieldType = fieldValue.getClass();
        if (isSimpleValue(fieldType)) {
            final String fieldStringValue = String.valueOf(fieldValue);
            writer.createSimpleNode(fieldName, fieldStringValue, fieldType.getName());
        } else if (isEnum(fieldValue)) {
            final String fieldStringValue = String.valueOf(fieldValue);
            writer.openNode(fieldName);
            writer.addAttribute(CLASS, fieldType.getName());
            writer.addAttribute("isEnum", "true");
            writer.addAttribute(VALUE, fieldStringValue);
            writer.closeNode(fieldName);
        } else if (isList(fieldValue) || isMap(fieldValue)) {
            writer.openNode(fieldName);
            writer.addAttribute(CLASS, fieldType.getName());

            if (isList(fieldValue)) {
                saveListField(writer, fieldValue, objectCache);
            } else {
                saveMapField(writer, fieldValue, objectCache);
            }

            writer.closeNode(fieldName);
        } else {
            saveFields(writer, fieldValue, fieldName, objectCache);
        }
    }

    private void saveMapField(final XmlNodeWriter writer, final Object fieldValue, final List<Object> objectCache) throws XMLStreamException, IllegalAccessException {
        Integer ref = getRefByKey(objectCache, fieldValue);
        if (ref != null) {
            getLogger().debug("Saving class reference " + ref);
            writer.addAttribute("ref", ref.toString());
        } else {
            ref = objectCache.size();
            objectCache.add(fieldValue);
            writer.addAttribute("isMap", "true");
            writer.addAttribute("ref", ref.toString());
            @SuppressWarnings("unchecked")
            final Map<Object, Object> map = (Map<Object, Object>) fieldValue;
            saveElements(writer, map, objectCache);
        }
    }

    private Integer getRefByKey(final List<Object> objectCache, final Object fieldValue) {
        Integer key = null;
        if (fieldValue instanceof Map<?, ?> || fieldValue instanceof Item) {
            for (int i = 0; i < objectCache.size(); i++) {
                final Object entry = objectCache.get(i);
                if (entry == fieldValue) {
                    key = i;
                    break;
                }
            }
        } else {
            key = objectCache.indexOf(fieldValue);
            if (key == -1) {
                key = null;
            }
        }
        return key;
    }

    private void saveListField(final XmlNodeWriter writer, final Object fieldValue, final List<Object> objectCache) throws XMLStreamException, IllegalAccessException {
        writer.addAttribute("isList", "true");
        @SuppressWarnings("unchecked")
        final Collection<Object> collection = (Collection<Object>) fieldValue;
        saveElements(writer, collection, objectCache);
    }

    private boolean isEnum(final Object fieldValue) {
        return fieldValue instanceof Enum;
    }

    private void saveElements(final XmlNodeWriter writer, final Collection<Object> list, final List<Object> objectCache)
        throws XMLStreamException, IllegalAccessException {
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Object fieldValue = iterator.next();
            saveField(writer, "listElement", fieldValue, objectCache);
        }
    }

    private void saveElements(final XmlNodeWriter writer, final Map<Object, Object> map, final List<Object> objectCache)
        throws IllegalAccessException, XMLStreamException {
        final Set<Entry<Object, Object>> entrySet = map.entrySet();
        final Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            writer.openNode(MAP_ENTRY);
            final Entry<Object, Object> entry = iterator.next();

            final Object key = entry.getKey();
            final Object value = entry.getValue();

            saveField(writer, "key", key, objectCache);
            saveField(writer, VALUE, value, objectCache);
            writer.closeNode(MAP_ENTRY);
        }
    }

    private boolean isList(final Object fieldValue) {
        return fieldValue instanceof Collection;
    }

    private boolean isMap(final Object fieldValue) {
        return fieldValue instanceof Map;
    }

    private boolean isSimpleValue(final Class<?> fieldType) {
        final String fieldTypeName = fieldType.getName();
        return fieldTypeName.startsWith("java.lang.");
    }

}
