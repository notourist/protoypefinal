package de.schweinefilet.prototype.common.entity;


import java.awt.Graphics2D;
import java.awt.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * A spawn is a point, where a player enters the world, after he has died, the map was reset
 * or the player respawned.
 */
@ToString
@NoArgsConstructor
public class Spawn extends Entity {

    /**
     * The {@link Team} the {@link Spawn} belongs to.
     */
    @Getter
    private Team team;

    public Spawn(@NonNull Point location, Team team) {
        super(location);
        this.team = team;
    }

    /**
     * Draws this {@link Spawn} with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the spawn
     * @param offsetX the offset on the x-axis the {@link Spawn} needs to be moved
     * @param offsetY the offset on the y-axis the {@link Spawn} needs to be moved
     */
    @Override
    public void draw(Graphics2D graphics2D, int offsetX, int offsetY) {
        graphics2D.setColor(team.getSpawnColor());
        graphics2D.fillRect(offsetX + location.x - 10,
            offsetY + location.y - 10,
            20,
            20);
    }
}
