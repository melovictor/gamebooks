package hu.zagor.gamebooks.books.contentransforming.enemy;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract class handling the common parts of parsing in an {@link Enemy} object.
 * @author Tamas_Szekeres
 * @param <T> the actual type of the enemy object to be parsed
 */
public abstract class AbstractBookEnemyTransformer<T extends Enemy> extends AbstractTransformer implements BookEnemyTransformer {

    @LogInject private Logger logger;

    @Override
    public Map<String, Enemy> transformEnemies(final Document document) throws XmlTransformationException {
        final Map<String, Enemy> enemies = new HashMap<>();

        final NodeList childNodes = document.getChildNodes();
        if (childNodes.getLength() > 0) {
            parseEnemies(childNodes.item(0), enemies);
        } else {
            logger.error("Couldn't find 'enemies' element in xml file!");
            throw new XmlTransformationException("Missing element 'enemies'.");
        }

        return enemies;
    }

    private void parseEnemies(final Node node, final Map<String, Enemy> enemies) {
        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node enemyNode = childNodes.item(i);
            if (isEnemy(enemyNode)) {
                final T enemy = parseEnemy(enemyNode);
                final String id = enemy.getId();
                if (enemies.containsKey(id)) {
                    throw new IllegalStateException("Book contains at least two instances of the enemy id '" + id + "'!");
                }
                enemies.put(id, enemy);
            }
        }
    }

    private boolean isEnemy(final Node childNode) {
        return "enemy".equals(childNode.getNodeName());
    }

    private T parseEnemy(final Node node) {
        final T enemy = createEnemyObject();

        enemy.setId(extractAttribute(node, "id"));
        enemy.setName(extractAttribute(node, "name"));
        finishParsing(node, enemy);

        return enemy;
    }

    /**
     * Finishes parsing of a single enemy based on the {@link Node} provided.
     * @param node the {@link Node} to parse the enemy from
     * @param enemy the enemy object to finish parsing into
     */
    protected abstract void finishParsing(Node node, T enemy);

    /**
     * Returns an empty, appropriate instance.
     * @return the instance to populate
     */
    protected abstract T createEnemyObject();

}
