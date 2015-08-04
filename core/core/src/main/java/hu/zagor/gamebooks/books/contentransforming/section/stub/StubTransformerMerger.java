package hu.zagor.gamebooks.books.contentransforming.section.stub;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Factory bean for merging two maps containing stub transformers into one.
 * @author Tamas_Szekeres
 */
public class StubTransformerMerger {

    /**
     * Merges the two maps into a third one. The elements in the second map overrides the ones in the first.
     * @param first the first map containing the first batch of transformers
     * @param second the second map containing the second batch of transformers
     * @return the merged map
     */
    public Map<String, StubTransformer> merge(final Map<String, StubTransformer> first, final Map<String, StubTransformer> second) {
        Assert.notNull(first, "The parameter 'first' cannot be null!");
        Assert.notNull(second, "The parameter 'second' cannot be null!");

        final Map<String, StubTransformer> merged = new HashMap<>();
        merged.putAll(first);
        merged.putAll(second);
        return merged;
    }
}
