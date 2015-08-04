package hu.zagor.gamebooks.support.spring;

import java.util.HashMap;
import java.util.Map;

public class MapMerger {

    public static Map<Object, Object> merge(final Map<Object, Object> mapA, final Map<Object, Object> mapB) {
        final Map<Object, Object> merged = new HashMap<Object, Object>();
        merged.putAll(mapA);
        merged.putAll(mapB);
        return merged;
    }

}
