package de.schweinefilet.prototype.client.control;

import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;

import de.schweinefilet.prototype.common.frame.draw.Drawable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import lombok.Getter;

/**
 * Represents the in game mouse.
 */
public class Mouse implements Drawable {

    /**
     * The mouse location on the screen.
     */
    @Getter
    private Point location;

    Mouse() {
        location = new Point();
    }

    public void setLocation(int x, int y) {
        this.location = new Point(x, y);
    }

    /**
     * Draws this {@link Mouse} with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the mouse
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.GREEN);

        int x = location.x;
        int y = location.y;
        graphics2D.drawLine(x - 10, y, x + 10, y);
        graphics2D.drawLine(x, y - 10, x, y + 10);
        graphics2D.drawLine(x - 10, y + 1, x + 10, y + 1);
        graphics2D.drawLine(x + 1, y - 10, x + 1, y + 10);

        graphics2D.drawString(
            "distance: " + (int) new Point((FRAME_HANDLER.getWidth() / 2),
                (FRAME_HANDLER.getHeight() / 2)).distance(x, y),
            x + 6,
            y + 18);
    }
}
