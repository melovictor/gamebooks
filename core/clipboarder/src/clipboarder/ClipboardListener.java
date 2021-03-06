package clipboarder;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClipboardListener extends Thread implements ClipboardOwner {
    private static final Pattern EMPTY_P_REMOVING_PATTERN = Pattern.compile("\\[p\\]\\[\\/p\\][\\r\\n \\t]+");
    private static final Pattern IMAGE_NAME_PATTERN = Pattern.compile("b-([0-9]+)\\.jpg");
    private static final Pattern IMAGE_DIR_PATTERN = Pattern.compile("([a-z]+[0-9]+)(?:[a-z]+){0,1}");
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private final Replacer hunReplacer = new HungarianReplacer();
    // private final Replacer altHunReplacer = new AlternateHungarianReplacer();
    private final Replacer engReplacer = new EnglishReplacer();
    private final ContentSorter contentSorter = new ContentSorter();

    private final Pattern paragraphPattern = Pattern.compile("([^\\n\\r]+)");

    private BookImageData imageData;

    @Override
    public void run() {
        lostOwnership(null, null);
        while (true) {
            try {
                Thread.sleep(10000000L);
            } catch (final InterruptedException e) {
            }
        }
    }

    @Override
    public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
        try {
            Thread.sleep(30);
            checkClipboard();
        } catch (UnsupportedFlavorException | IllegalStateException | IOException | InterruptedException e) {
            e.printStackTrace();
            lostOwnership(clipboard, contents);
        }
    }

    public void checkClipboard() throws UnsupportedFlavorException, IOException {
        final Transferable contents = clipboard.getContents(null);

        Transferable newClipboardContent = null;
        if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            String clipboardContent = readContent(contents, DataFlavor.stringFlavor);

            if (!isConverted(clipboardContent) && !isProgramCode(clipboardContent)) {
                newClipboardContent = processClipboardContent(clipboardContent);
            } else {
                final String newContent = contentSorter.tryMap(clipboardContent, imageData);
                if (newContent != null) {
                    imageData = null;
                    clipboardContent = newContent;
                    clipboard.setContents(new StringSelection(newContent), this);
                    System.out.println("Full-file structure reordered.");
                } else {
                    newClipboardContent = contents;
                }
            }
        } else if (contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            @SuppressWarnings("unchecked")
            final List<File> transferData = (List<File>) contents.getTransferData(DataFlavor.javaFileListFlavor);
            imageData = new BookImageData();
            imageData.setCode(calculateLocaleInvariantName(transferData));
            for (final File file : transferData) {
                final String filename = file.getName();
                final Matcher matcher = IMAGE_NAME_PATTERN.matcher(filename);
                if (matcher.matches()) {
                    imageData.getSections().add(matcher.group(1));
                }
            }
            System.out.println("Image file list initialized");
            newClipboardContent = new StringSelection("");
        } else {
            newClipboardContent = contents;
        }
        clipboard.setContents(newClipboardContent, this);
    }

    private String readContent(final Transferable contents, final DataFlavor flavorToUse) throws UnsupportedFlavorException, IOException {
        String clipboardContent = "";
        try (Reader clipboardReader = flavorToUse.getReaderForText(contents); Scanner scanner = new Scanner(clipboardReader)) {
            while (scanner.hasNextLine()) {
                clipboardContent += scanner.nextLine() + "\n";
            }
        }
        return clipboardContent.replace("¬", "").trim();
    }

    private String calculateLocaleInvariantName(final List<File> transferData) {
        String name = transferData.get(0).getParentFile().getName();

        final Matcher matcher = IMAGE_DIR_PATTERN.matcher(name);
        if (matcher.matches()) {
            name = matcher.group(1);
        }

        return name;
    }

    private boolean isProgramCode(final String clipboardContent) {
        return clipboardContent.contains("{") || clipboardContent.contains("<") || clipboardContent.contains(">")
            || (clipboardContent.contains(";") && !clipboardContent.contains("; "));
    }

    private boolean isConverted(final String clipboardContent) {
        return clipboardContent.contains("[p]") || clipboardContent.contains("[li]");
    }

    private Transferable processClipboardContent(final String content) {
        final String newContent = convertParagraphs(content);
        return new StringSelection(newContent);
    }

    private String convertParagraphs(final String content) {
        String newContent = null;

        newContent = hunReplacer.tryMap(content);
        if (newContent == null) {
            newContent = engReplacer.tryMap(content);
        }
        if (newContent == null) {
            newContent = contentSorter.tryMap(content, imageData);
        }

        if (newContent == null) {
            newContent = "";
            boolean first = true;
            if (content.contains("\n")) {
                final Matcher matcher = paragraphPattern.matcher(content);
                while (matcher.find()) {
                    if (first) {
                        first = false;
                        newContent += matcher.group(0);
                    } else {
                        newContent += "[/p]\n\t\t[p]" + matcher.group(0);
                    }
                }
                newContent = newContent.trim();
            } else {
                newContent = content;
            }
        }

        final Matcher matcher = EMPTY_P_REMOVING_PATTERN.matcher(newContent);
        newContent = matcher.replaceAll("");

        System.out.println("All targeted conversion failed, fallback on standard conversion:");
        System.out.println(newContent);
        return newContent;
    }

}
