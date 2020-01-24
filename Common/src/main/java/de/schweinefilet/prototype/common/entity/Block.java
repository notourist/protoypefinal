package de.schweinefilet.prototype.common.entity;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A block is an unmovable object, that prevents other {@link BoundedEntity} from moving in itself.
 */
@NoArgsConstructor
public class Block extends BoundedEntity {

    /**
     * The width and height of the {@link Block}.
     */
    @Getter
    private int width, height;

    public Block(@NonNull Point location, int width, int height) {
        super(location, width, height);
        this.width = width;
        this.height = height;
    }

    /**
     * Draws this {@link Block} with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the block
     * @param offsetX the offset on the x-axis the {@link Block} needs to be moved
     * @param offsetY the offset on the y-axis the {@link Block} needs to be moved
     */
    public void draw(Graphics2D graphics2D, int offsetX, int offsetY) {
        graphics2D.setColor(Color.YELLOW);
        graphics2D.fillRect(offsetX + location.x,
            offsetY + location.y,
            width,
            height);
    }
}
