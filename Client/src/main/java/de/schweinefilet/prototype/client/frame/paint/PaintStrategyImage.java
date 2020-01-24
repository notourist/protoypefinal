package de.schweinefilet.prototype.client.frame.paint;

import java.awt.Image;

/**
 * Provides a image buffered {@link PaintStrategy}.
 * @param <T> the image used for buffering
 */
public abstract class PaintStrategyImage<T extends Image> implements PaintStrategy {

    T image;

    /**
     * Recreates the internally used image.
     */
    public abstract void recreateImage() throws IllegalArgumentException;

    /**
     * Returns whether the image creation was successful or not.
     * @return -
     */
    public boolean hasValidImage() {
        return image.getWidth(null) > 1 && image.getHeight(null) > 1;
    }

    @Override
    public boolean isImagePowered() {
        return true;
    }
}
