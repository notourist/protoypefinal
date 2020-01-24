package de.schweinefilet.prototype.common.frame.draw;

import java.awt.Graphics2D;

/**
 * An drawable object that can be drawn without a offset.
 */
public interface Drawable {
    void draw(Graphics2D graphics2D);
}
