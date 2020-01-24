package de.schweinefilet.prototype.common.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * An player {@link Entity} controlled by another client.
 */
@ToString(callSuper = true)
@NoArgsConstructor
public class Player extends BoundedEntity {

    /**
     * The name of the other player.
     */
    @Getter
    protected String name;

    /**
     * The {@link Player}'s {@link Team}.
     */
    @Getter
    @Setter
    protected Team team;

    public Player(@NonNull String name, @NonNull Team team) {
        super(new Point(0, 0), 18, 18);
        this.name = name;
        this.team = team;
    }

    public Player(@NonNull Point location, @NonNull String uniqueId, @NonNull String name,
        @NonNull Team team) {
        super(location, uniqueId, 18, 18);
        this.name = name;
        this.team = team;
    }

    /**
     * Moves the player in x- and y-direction.
     *
     * @param x x-direction parameter
     * @param y y-direction parameter
     */
    public void move(int x, int y) {
        location.setLocation(location.getX() + x, location.getY() + y);
        updateBounds();
    }

    /**
     * Sets the location at the given x- and y-location.
     *
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void setLocation(int x, int y) {
        location.setLocation(x, y);
        updateBounds();
    }

    @Override
    public int hashCode() {
        return (uniqueId.hashCode() + name.hashCode()) * 37;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && obj.hashCode() == this.hashCode();
    }

    /**
     * Draws this {@link Player} with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the player
     * @param offsetX the offset on the x-axis the {@link Player} needs to be moved
     * @param offsetY the offset on the y-axis the {@link Player} needs to be moved
     */
    @Override
    public void draw(Graphics2D graphics2D, int offsetX, int offsetY) {
        graphics2D.setColor(team.getColor());
        graphics2D.fillOval(location.x + offsetX - 9,
            location.y + offsetY - 9,
            18,
            18);
        drawUsername(graphics2D, offsetX, offsetY);
    }

    /**
     * Draws a {@link Player}'s username centered above the player.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the username
     * @param offsetX the offset on the x-axis the {@link String} needs to be moved
     * @param offsetY the offset on the y-axis the {@link String} needs to be moved
     */
    private void drawUsername(Graphics2D graphics2D, int offsetX, int offsetY) {
        graphics2D.setColor(Color.GREEN);
        int length = (name.length()/2)*10;
        graphics2D.drawString(name,
            location.x + offsetX - length,
            location.y + offsetY - 10);
    }
}
