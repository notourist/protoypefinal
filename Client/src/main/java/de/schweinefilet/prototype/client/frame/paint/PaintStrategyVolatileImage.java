package de.schweinefilet.prototype.client.frame.paint;

import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;

import java.awt.Graphics;
import java.awt.image.VolatileImage;

/**
 * Uses a {@link VolatileImage} as a buffer. Should only be used when a dedicated GPU is available,
 * otherwise {@link PaintStrategyBufferedImage} should be used.
 * See {@link VolatileImage} for errors that can occur.
 */
public class PaintStrategyVolatileImage extends PaintStrategyImage<VolatileImage> {

    public PaintStrategyVolatileImage() {
        image = FRAME_HANDLER.createVolatileImage();
    }

    @Override
    public void paint(Graphics graphics) {
        if (image != null) {
            paintGame(image.createGraphics());

            //paint the image onto the frame
            graphics
                .drawImage(image, 0, 0, FRAME_HANDLER.getWidth(), FRAME_HANDLER.getHeight(), null);
        }
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getId() {
        return VOLATILE_IMAGE;
    }

    @Override
    public void recreateImage() {
        image = FRAME_HANDLER.createVolatileImage();
    }
}
