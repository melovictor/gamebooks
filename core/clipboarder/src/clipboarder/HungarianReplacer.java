package clipboarder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HungarianReplacer implements Replacer {

    private static final String BIG_TURNTO = "Lapozz az? ([0-9]+) ?- ?r[ace][.!]";
    private static final String TURNTO = "(?:,| –) lapozz az? ([0-9]+) ?- ?r[ace][.!]";

    private static final String CHOICE_A = "(?:[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + "){0,1}";
    private static final String CHOICE_B = "(?: (.*)" + TURNTO + "){0,1}";

    private final Pattern multiChoiceSectionPattern = Pattern
        .compile("([0-9]+)\\.\\s+(.*[?!.–]) (.*)" + TURNTO + " (.*)" + TURNTO + "." + CHOICE_B + CHOICE_B + CHOICE_B + CHOICE_B + CHOICE_B, Pattern.DOTALL);
    private final Pattern singleChoiceLikeMultiSectionPattern = Pattern.compile("([0-9]+)\\.\\s+(.*[?!….–]) (.*)" + TURNTO, Pattern.DOTALL);
    private final Pattern singleSendoffSectionPattern = Pattern.compile("([0-9]+)\\.?\\s+(.*[?!.…–]) " + BIG_TURNTO, Pattern.DOTALL);
    private final Pattern multiChoiceListSectionPattern = Pattern.compile(
        "([0-9]+)\\.\\s+(.*[?!.:])[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + CHOICE_A + CHOICE_A + CHOICE_A + CHOICE_A + CHOICE_A + CHOICE_A + CHOICE_A,
        Pattern.DOTALL | Pattern.COMMENTS);
    private final Pattern multiChoiceListSectionPattern2 = Pattern
        .compile("([0-9]+)\\.\\s+(.*[?!.:])[\\r\\n]+(.*?)[ \\t]+" + BIG_TURNTO + CHOICE_A + CHOICE_A + CHOICE_A + CHOICE_A + CHOICE_A + CHOICE_A);

    private final Pattern multiChoiceContinuousSectionPattern = Pattern.compile(
        "([0-9]+)\\.\\s+(.*?[?!.])\\s+([^.?!]*), lapozz az? ([0-9]+)-r[ace][.!]\\s+([^.?!]*), lapozz az? ([0-9]+)-r[ace][.!](?:\\s+([^.?!]*), lapozz az? ([0-9]+)-r[ace][.!]){0,1}(?:\\s+([^.?!]*), lapozz az? ([0-9]+)-r[ace][.!]){0,1}(?:\\s+([^.?!]*), lapozz az? ([0-9]+)-r[ace][.!]){0,1}(?:\\s+([^.?!]*), lapozz az? ([0-9]+)-r[ace][.!]){0,1}",
        Pattern.DOTALL | Pattern.COMMENTS);

    private final Pattern mcX = Pattern.compile(
        "([0-9]*)\\.\\s+(.*?[.?!])\\s+([^.?!]*), lapozz az? ([0-9]*)-r..(?:\\s+([^.?!]*), lapozz az? ([0-9]*)-r..){0,1}(?:\\s+([^.?!]*), lapozz az? ([0-9]*)-r..){0,1}(?:\\s+([^.?!]*), lapozz az? ([0-9]*)-r..){0,1}(?:\\s+([^.?!]*), lapozz az? ([0-9]*)-r..){0,1}(?:\\s+([^.?!]*), lapozz az? ([0-9]*)-r..){0,1}",
        Pattern.DOTALL);

    private static final String PARENTHESIS_CHOICE = "(?:([^(]*)\\(lapozz az? ([0-9]+)-r[ace]\\)[.,?]\\s*){0,1}";
    private final Pattern multiChoiceWithParenthesisedSectionCommands = Pattern.compile("([0-9]+)[\\s]+(.*[?!.])\\s+([^(]*)\\(lapozz az? ([0-9]+)-r[ace]\\),\\s*"
        + PARENTHESIS_CHOICE + PARENTHESIS_CHOICE + PARENTHESIS_CHOICE + PARENTHESIS_CHOICE + PARENTHESIS_CHOICE + PARENTHESIS_CHOICE + PARENTHESIS_CHOICE,
        Pattern.DOTALL);

    private final static Pattern LOSING_STAMINA = Pattern.compile("Vesztesz ([0-9]) (?:ÉLETERŐ|életerő) pontot");

    private final Pattern dying = Pattern.compile("([0-9]+)\\.?\\s+(.*Kalandod (?:itt ){0,1}véget ért?\\.)");

    private final Pattern choiceOnly = Pattern.compile("([^.]*), lapozz az? ([0-9]+)-r[ace][.!]");
    private final Pattern choiceOnlyPage = Pattern.compile("([^.]*), lapozz az? ([0-9]+)\\. oldalra.");

    private final Pattern enemy = Pattern.compile("(.*)\\s+(?:ÜGYESSÉG|ügyesség)\\s+(\\d+)\\s+(?:ÉLETERŐ|életerő)\\s+(\\d+)");

    @Override
    public String tryMap(final String content) {
        return tryFitPatterns(content);
    }

    private String tryFitPatterns(final String content) {
        String newContent = null;
        newContent = fitSss(content);
        if (newContent == null) {
            newContent = fitMcs(content);
        }
        if (newContent == null) {
            newContent = fitSclms(content);
        }
        if (newContent == null) {
            newContent = fitCo(content);
        }
        if (newContent == null) {
            newContent = fitD(content);
        }
        if (newContent == null) {
            newContent = fitE(content);
        }

        newContent = lookForStaminaLoss(newContent);

        return newContent;
    }

    private String lookForStaminaLoss(final String content) {
        String newContent = content;
        if (newContent != null) {
            final Matcher matcher = LOSING_STAMINA.matcher(content);
            if (matcher.find()) {
                final String lostStaminaAmount = matcher.group(1);
                newContent = newContent.replace("</text>", "</text>\r\n    <add to=\"stamina\" amount=\"-" + lostStaminaAmount + "\" />");
            }
        }
        return newContent;
    }

    private String fitD(final String content) {
        String newContent = null;
        final Matcher matcher = dying.matcher(content);
        if (matcher.matches()) {
            final String id = matcher.group(1);
            final String mainText = matcher.group(2);

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]").replace("Kalandod véget", "Kalandod itt véget").replace("ért", "ér");
            newContent += "[/p]\n\t</text>";
            newContent += "\n\t<death />";
            newContent += "\n  </p>";
            System.out.println("Converted based on fitD, result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String fitE(final String content) {
        String newContent = null;
        final Matcher matcher = enemy.matcher(content);
        if (matcher.matches()) {
            final String name = matcher.group(1).trim();
            final String skill = matcher.group(2);
            final String stamina = matcher.group(3);

            newContent = "<enemy stamina=\"" + stamina + "\" skill=\"" + skill + "\" id=\"XX\" name=\"" + name + "\" />";
            System.out.println("Converted based on fitE, result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String fitCo(final String content) {
        String newContent = null;
        Matcher matcher = choiceOnly.matcher(content);
        if (!matcher.matches()) {
            matcher = choiceOnlyPage.matcher(content);
        }
        if (matcher.matches()) {
            newContent = "<next id=\"" + matcher.group(2).trim() + "\">" + trimChoice(matcher.group(1)) + "</next>";
            System.out.println("Converted based on fitCo, result:");
            System.out.println(newContent);
        }

        return newContent;
    }

    private String trimChoice(final String choiceText) {
        String trimmedChoice;
        if (choiceText.startsWith("-") || choiceText.startsWith("–")) {
            trimmedChoice = choiceText.substring(1);
        } else {
            trimmedChoice = choiceText;
        }

        return trimmedChoice.trim();
    }

    private String fitSclms(final String content) {
        String newContent = null;
        final Matcher matcher = singleChoiceLikeMultiSectionPattern.matcher(content);
        if (matcher.matches()) {
            final String id = matcher.group(1);
            final String mainText = matcher.group(2) + " " + matcher.group(3) + ".";
            final String choiceId = matcher.group(4);

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]");
            newContent += "[/p]\n\t</text>";

            newContent = checkForTests(newContent, matcher, content);

            newContent += "\n\t<next id=\"" + choiceId + "\"></next>";
            newContent += "\n  </p>";
            System.out.println("Converted based on fitSclms, result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String fitSss(final String content) {
        String newContent = null;
        final Matcher matcher = singleSendoffSectionPattern.matcher(content);
        if (matcher.matches()) {
            final String id = matcher.group(1);
            final String mainText = matcher.group(2);
            final String choiceId = matcher.group(3);

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]");
            newContent += "[/p]\n\t</text>";

            newContent = checkForTests(newContent, matcher, content);

            newContent += "\n\t<next id=\"" + choiceId + "\"></next>";
            newContent += "\n  </p>";
            System.out.println("Converted based on fitSss, result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String fitMcs(final String content) {
        String newContent = null;
        String matchPattern = "Mcs";
        Matcher matcher = multiChoiceSectionPattern.matcher(content);
        if (!matcher.matches()) {
            matcher = multiChoiceListSectionPattern.matcher(content);
            matchPattern = "Mcls";
        }
        if (!matcher.matches()) {
            matcher = multiChoiceListSectionPattern2.matcher(content);
            matchPattern = "Mcls2";
        }
        if (!matcher.matches()) {
            matcher = multiChoiceContinuousSectionPattern.matcher(content);
            matchPattern = "Mccsp";
        }
        if (!matcher.matches()) {
            matcher = mcX.matcher(content);
            matchPattern = "Mcx";
        }
        if (!matcher.matches()) {
            matcher = multiChoiceWithParenthesisedSectionCommands.matcher(content);
            matchPattern = "Mcwpsc";
        }
        if (matcher.matches()) {
            final String id = matcher.group(1);
            final String mainText = matcher.group(2);

            newContent = "<p id=\"" + id + "\">\n\t<text>\n\t\t[p]";
            newContent += mainText.replace("\r", "").replace("\n", "[/p]\n\t\t[p]");
            newContent += "[/p]\n\t</text>";

            newContent = checkForTests(newContent, matcher, mainText);

            final boolean convertToQuestion = content.trim().endsWith("?");
            for (int idx = 3; idx < matcher.groupCount(); idx += 2) {
                final String choiceText = matcher.group(idx);
                final String choiceId = matcher.group(idx + 1);
                if (choiceText != null) {
                    newContent += "\n\t<next id=\"" + choiceId + "\">" + capitalize(trimChoice(choiceText), convertToQuestion) + "</next>";
                }
            }

            newContent += "\n  </p>";

            System.out.println("Converted based on fit" + matchPattern + ", result:");
            System.out.println(newContent);
        }
        return newContent;
    }

    private String capitalize(final String choiceText, final boolean convertToQuestion) {
        return choiceText.substring(0, 1).toUpperCase() + choiceText.substring(1) + (convertToQuestion ? "?" : "");
    }

    private String checkForTests(final String nc, final Matcher matcher, final String mt) {
        String newContent = nc;
        final String mainText = mt;
        boolean isLuckTest = false;
        boolean isSkillTest = false;
        if (mainText.contains("Tedd próbára a Szerencsédet!")) {
            newContent = newContent.replace("Tedd próbára a Szerencsédet!", "").trim();
            isLuckTest = true;
        }
        if (mainText.contains("Tedd próbára a SZERENCSÉDET!")) {
            newContent = newContent.replace("Tedd próbára a SZERENCSÉDET!", "").trim();
            isLuckTest = true;
        }
        if (mainText.contains("Tedd próbára a SZERENCSÉDET.")) {
            newContent = newContent.replace("Tedd próbára a SZERENCSÉDET.", "").trim();
            isLuckTest = true;
        }
        if (mainText.contains("Tedd próbára SZERENCSÉD!")) {
            newContent = newContent.replace("Tedd próbára SZERENCSÉD!", "").trim();
            isLuckTest = true;
        }
        if (mainText.contains("Tedd próbára SZERENCSÉD.")) {
            newContent = newContent.replace("Tedd próbára SZERENCSÉD.", "").trim();
            isLuckTest = true;
        }
        if (mainText.contains("Dobj két kockával")
            && (matcher.group(5).contains("mint ÜGYESSÉG pontjaid száma") || matcher.group(3).contains("mint ÜGYESSÉG pontjaid száma")
                || matcher.group(5).contains("mint az Ügyességed") || matcher.group(3).contains("mint az Ügyességed"))) {
            newContent = newContent.replace("Dobj két kockával!", "").replace("Dobj két kockával.", "").trim();
            isSkillTest = true;
        }
        if (isLuckTest) {
            newContent += "\n\t<test against=\"luck\">\n";
            newContent += "      <success>\n" + "        <next id=\"" + matcher.group(4) + "\"></next>\n" + "      </success>\n" + "      <failure>\n"
                + "        <next id=\"" + matcher.group(6) + "\"></next>\n" + "      </failure>";
            newContent += "\n\t</test>";
        } else if (isSkillTest) {
            newContent += "\n\t<test against=\"skill\">\n";
            newContent += "      <success>\n" + "        <next id=\"" + matcher.group(4) + "\"></next>\n" + "      </success>\n" + "      <failure>\n"
                + "        <next id=\"" + matcher.group(6) + "\"></next>\n" + "      </failure>";
            newContent += "\n\t</test>";
        }
        return newContent.replace(" [/p]", "[/p]");
    }
}
