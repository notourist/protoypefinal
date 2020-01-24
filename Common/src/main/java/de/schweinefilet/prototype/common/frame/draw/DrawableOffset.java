package de.schweinefilet.prototype.common.frame.draw;

import java.awt.Graphics2D;

/**
 * An drawable object that can be only drawn with a offsets.
 */
public interface DrawableOffset {
    void draw(Graphics2D graphics2D, int offsetX, int offsetY);
}
