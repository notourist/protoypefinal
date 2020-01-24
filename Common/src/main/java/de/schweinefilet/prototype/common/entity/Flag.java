package de.schweinefilet.prototype.common.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * An object that can be picked up by players.
 */
@NoArgsConstructor
public class Flag extends Entity {

    /**
     * This location is the "normal" location for the flag on the map.
     * Every time a win gets scored, the {@link Flag#location} gets reset to this location.
     */
    private Point baseLocation;

    /**
     * Represents the player who picked the {@link Flag}.
     * If the flag was not picked up, the object is null.
     */
    @Getter
    private Player picker;

    /**
     * True, if the {@link Flag} was picked up, else false.
     */
    @Getter
    private boolean pickedUp;

    public Flag(@NonNull Point location) {
        super(location);
        this.baseLocation = location;
        this.pickedUp = false;
    }

    /**
     * Sets the picker's location as the flag's location.
     */
    public void updateLocation() {
        if (pickedUp && picker != null) {
            location.setLocation(picker.getLocation().x,
                picker.getLocation().y);
        }
    }

    /**
     * Resets the location to the base location.
     */
    public void reset() {
        location.setLocation(baseLocation);
        pickedUp = false;
        picker = null;
    }

    /**
     * Drops the flag at the Pickers location.
     */
    public void drop() {
        pickedUp = false;
        //align the location by -5
        location = new Point(picker.getLocation().x, picker.getLocation().y);
    }

    /**
     * Sets the picker.
     *
     * @param user the picker
     */
    public void setPicker(Player user) {
        this.pickedUp = true;
        this.picker = user;
    }

    /**
     * Draws this {@link Flag} with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the flag
     * @param offsetX the offset on the x-axis the {@link Flag} needs to be moved
     * @param offsetY the offset on the y-axis the {@link Flag} needs to be moved
     */
    @Override
    public void draw(Graphics2D graphics2D, int offsetX, int offsetY) {
        graphics2D.setColor(Color.GREEN);
        graphics2D.fillRect(offsetX + location.x - 5,
            offsetY + location.y - 5,
            10,
            10);
    }

    public boolean isAtBase() {
        return location.distance(baseLocation) == 0;
    }
}
