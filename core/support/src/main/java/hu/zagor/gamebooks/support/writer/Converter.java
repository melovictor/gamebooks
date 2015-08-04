package hu.zagor.gamebooks.support.writer;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Class to help with conversion between one form to another.
 * @author Tamas_Szekeres
 */
@Component
public class Converter {

    /**
     * Converts the specified map into a json string.
     * @param map the map to convert
     * @return the converted text
     */
    public String toJsonString(final Map<String, Object> map) {
        return JSONObject.toJSONString(map);
    }
}
