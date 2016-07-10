package hu.zagor.gamebooks.screenshooter;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AutoScreenshotMaker {
    private static BufferedImage lastSavedImage;

    public static void main(final String[] args) throws IOException, AWTException, InterruptedException {
        int counter = 0;
        while (true) {
            final BufferedImage capture = getNewImage();
            if (shouldSaveNewImage(capture)) {
                ImageIO.write(capture, "bmp", new File("d:\\System\\eclipse\\book\\image" + (++counter) + ".bmp"));
                lastSavedImage = capture;
            }
            Thread.sleep(100);
        }
    }

    private static boolean shouldSaveNewImage(final BufferedImage capture) {
        if (lastSavedImage == null) {
            return true;
        }
        return !bufferedImagesEqual(lastSavedImage, capture);
    }

    private static boolean bufferedImagesEqual(final BufferedImage img1, final BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private static BufferedImage getNewImage() throws AWTException {
        // final Rectangle screenRect = new Rectangle(638, 79, 629, 930);
        final Rectangle screenRect = new Rectangle(307, 36, 1282, 1009);
        // final Rectangle screenRect = new Rectangle(107, 36, 1682, 1009);
        final BufferedImage capture = new Robot().createScreenCapture(screenRect);
        return capture;
    }

}
