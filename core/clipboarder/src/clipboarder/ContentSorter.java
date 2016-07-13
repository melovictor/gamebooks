package clipboarder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentSorter {

    private static final Pattern SECTIONS = Pattern.compile("(<p id=\"([^\"]+)\".*?<\\/p>)", Pattern.DOTALL);

    public String tryMap(final String content, final BookImageData imageData) {
        if (isXml(content) && (isFf(content) || isRaw(content) || isSorcery(content))) {
            try {
                return sortSections(content, imageData);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private boolean isSorcery(final String content) {
        return content.contains(
            "<content xmlns=\"http://gamebooks.zagor.hu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://gamebooks.zagor.hu http://zagor.hu/xsd/sor.xsd\">");
    }

    private boolean isFf(final String content) {
        return content.contains(
            "<content xmlns=\"http://gamebooks.zagor.hu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://gamebooks.zagor.hu http://zagor.hu/xsd/ff.xsd\">");
    }

    private boolean isRaw(final String content) {
        return content.contains(
            "<content xmlns=\"http://gamebooks.zagor.hu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://gamebooks.zagor.hu http://zagor.hu/xsd/raw.xsd\">");
    }

    private boolean isXml(final String content) {
        return content.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    }

    private String sortSections(final String content, final BookImageData imageData) {
        final boolean ff = content.contains("ff.xsd");
        final Map<String, String> sections = new HashMap<>();
        final List<String> sectionIds = new ArrayList<>();

        final Matcher matcher = SECTIONS.matcher(content);
        while (matcher.find()) {
            final String id = matcher.group(2);
            final String section = matcher.group(1).trim();
            sections.put(id, expandWithImageData(id, section, imageData));
            sectionIds.add(id);
        }

        sectionIds.remove("back_cover");
        sectionIds.remove("generate");
        sectionIds.remove("background");
        Collections.sort(sectionIds, new Comparator<String>() {

            @Override
            public int compare(final String idA, final String idB) {
                final Double valA = getValue(idA);
                final Double valB = getValue(idB);
                return valA.compareTo(valB);
            }

        });

        final StringBuilder builder = new StringBuilder();
        builder.append(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<content xmlns=\"http://gamebooks.zagor.hu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://gamebooks.zagor.hu http://zagor.hu/xsd/"
                + (ff ? "ff" : "sor") + ".xsd\">");

        appendSection(sections, builder, "back_cover", 1, 0);
        appendSection(sections, builder, "generate", 1, 0);
        appendSection(sections, builder, "background", 1, 0);

        int lastId = 0;
        int currentId;
        for (final String id : sectionIds) {
            currentId = (int) getValue(id);
            appendSection(sections, builder, id, Math.max(1, currentId - lastId), lastId);
            lastId = (int) getValue(id);
        }
        builder.append("\n</content>\n");
        return builder.toString();
    }

    private String expandWithImageData(final String id, final String section, final BookImageData imageData) {
        String expanded = section;
        if (imageData != null && imageData.getSections() != null && imageData.getSections().contains(id) && !expanded.contains("inlineImage")) {
            final String imageLink = "[p class=\"inlineImage\" data-img=\"" + id + "\"][/p]";
            expanded = expanded.replaceFirst("\\[p\\]", imageLink + "\r\n        [p]");
        }
        return expanded;
    }

    private void appendSection(final Map<String, String> sections, final StringBuilder builder, final String id, final int distance, final int lastValue) {
        for (int i = 0; i < distance - 1; i++) {
            builder.append("\n  <!-- " + (lastValue + 1 + i) + " -->");
        }
        builder.append("\n  " + sections.get(id));
    }

    private double getValue(final String id) {
        try {
            return Integer.valueOf(id);
        } catch (final NumberFormatException exception) {
            final String numString = id.replaceAll("[^0-9]+", "");
            double numValue = Integer.valueOf(numString);
            final String fractionString = id.replaceAll("[0-9]+", "");
            final int fraction = Integer.valueOf(fractionString, 36);
            numValue += fraction / 100;
            return numValue;
        }
    }

}
