package hu.zagor.gamebooks.controller.image;

import org.testng.annotations.Test;

/**
 * Unit test for class {@link ImageLookupStrategyType}.
 * @author Tamas_Szekeres
 */
@Test
public class ImageLookupStrategyTypeTest {

    public void testValueOf() {
        ImageLookupStrategyType.valueOf("COLOR_BW");
        ImageLookupStrategyType.valueOf("BW_COLOR");
    }
}
