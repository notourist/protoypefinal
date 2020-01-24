package de.schweinefilet.prototype.client.frame.paint;

import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;

import java.awt.Graphics;
import java.awt.Graphics2D;
import lombok.NoArgsConstructor;

/**
 * Uses page flipping to paint the game.
 * Should only be used with at least 2 possible {@link java.awt.image.BufferStrategy} buffers.
 */
@NoArgsConstructor
public class PaintStrategyPageFlipping implements PaintStrategy {
    
    @Override
    public void paint(Graphics graphics) {
        paintGame((Graphics2D) graphics);
        graphics.dispose();
        FRAME_HANDLER.getBufferStrategy().show();
    }

    @Override
    public boolean isImagePowered() {
        return false;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int getId() {
        return PAGE_FLIPPING;
    }
}
