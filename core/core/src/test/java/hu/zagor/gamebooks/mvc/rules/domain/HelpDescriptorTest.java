package hu.zagor.gamebooks.mvc.rules.domain;

import hu.zagor.gamebooks.domain.BookInformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link HelpDescriptor}.
 * @author Tamas_Szekeres
 */
@Test
public class HelpDescriptorTest {

    private HelpDescriptor underTest;
    private List<HelpSection> helpSections;
    private Map<String, String> params;
    private BookInformations info;

    @BeforeClass
    public void setUpClass() {
        underTest = new HelpDescriptor();
        info = new BookInformations(3L);
        helpSections = new ArrayList<>();
        params = new HashMap<>();
    }

    public void testGetSectionsShouldReturnSections() {
        // GIVEN
        underTest.setSections(helpSections);
        // WHEN
        final List<HelpSection> returned = underTest.getSections();
        // THEN
        Assert.assertSame(returned, helpSections);
    }

    public void testGetParamsShouldReturnParams() {
        // GIVEN
        underTest.setParams(params);
        // WHEN
        final Map<String, String> returned = underTest.getParams();
        // THEN
        Assert.assertSame(returned, params);
    }

    public void testGetInfoShouldReturnInfo() {
        // GIVEN
        underTest.setInfo(info);
        // WHEN
        final BookInformations returned = underTest.getInfo();
        // THEN
        Assert.assertSame(returned, info);
    }

}
