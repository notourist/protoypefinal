package de.schweinefilet.prototype.common.entity;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Everytime a {@link Player} gets in contact with a {@link Projectile},
 * he has to respawn.
 */
@NoArgsConstructor
public class Projectile extends Entity {

    /**
     * Represents the {@link Projectile}'s line. Used for drawing and checking collision.
     */
    @Getter
    private Line2D line;

    /**
     * The {@link Team} the {@link Projectile} belongs to.
     * Only players in the other team get damaged.
     */
    @Getter
    private Team team;

    /**
     * The time after the projectile is removed in ms.
     */
    @Getter
    private int liveTime;

    public Projectile(Point startLocation, @NonNull Point endLocation, Team team, int liveTime) {
        super(startLocation);
        this.line = new Line2D.Float(location, endLocation);
        this.team = team;
        this.liveTime = liveTime;
    }

    /**
     * Draws this {@link Projectile} with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the spawn
     * @param offsetX the offset on the x-axis the {@link Projectile} needs to be moved
     * @param offsetY the offset on the y-axis the {@link Projectile} needs to be moved
     */
    @Override
    public void draw(Graphics2D graphics2D, int offsetX, int offsetY) {
        graphics2D.setColor(team.getColor());
        graphics2D.drawLine((int) line.getX1() + offsetX,
            (int) line.getY1() + offsetY,
            (int) line.getX2() + offsetX,
            (int) line.getY2() + offsetY);
    }
}
