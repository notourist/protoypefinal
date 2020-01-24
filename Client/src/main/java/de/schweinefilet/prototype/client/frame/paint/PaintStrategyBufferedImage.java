package de.schweinefilet.prototype.client.frame.paint;

import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;

import de.schweinefilet.prototype.client.frame.ResizeRunnable;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Draws with a {@link BufferedImage} used as a buffer to prevent most of the Screen Tearing*.
 * First, the method draws onto the image, then it's shown. If a dedicated GPU is available, {@link
 * PaintStrategyVolatileImage} can be used for a better performance.
 *
 * <p>* Screen tearing is a visual artifact in video display where a display device shows
 * information from multiple frames in a single screen paintGame. Source: <a
 * href="https://en.wikipedia.org/wiki/Screen_tearing">https://en.wikipedia.org/wiki/Screen_tearing</a></p>
 */
public class PaintStrategyBufferedImage extends PaintStrategyImage<BufferedImage> {

    public PaintStrategyBufferedImage() {
        try {
            recreateImage();

            if (image.getWidth() <= FRAME_HANDLER.getWidth()
                && image.getHeight() <= FRAME_HANDLER.getHeight()) {
                new Thread(new ResizeRunnable()).start();
            }
        } catch (IllegalArgumentException exc) {
            new Thread(new ResizeRunnable()).start();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if (image != null) {
            paintGame(image.createGraphics());

            //paint the image onto the frame
            graphics
                .drawImage(image, 0, 0, FRAME_HANDLER.getWidth(), FRAME_HANDLER.getHeight(), null);
            image.flush();
        }
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getId() {
        return BUFFERED_IMAGE;
    }

    @Override
    public void recreateImage() throws IllegalArgumentException {
        image = new BufferedImage(FRAME_HANDLER.getWidth(), FRAME_HANDLER.getHeight(), BufferedImage.TYPE_INT_RGB);
    }
}