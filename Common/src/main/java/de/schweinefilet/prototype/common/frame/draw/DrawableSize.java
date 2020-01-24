package de.schweinefilet.prototype.common.frame.draw;

import java.awt.Graphics2D;

/**
 * An drawable object that can be only drawn with a offsets, width and height.
 */
public interface DrawableSize {
    void draw(Graphics2D graphics2D, int offsetX, int offsetY, int width, int height);
}
