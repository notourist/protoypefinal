package de.schweinefilet.prototype.client.control;

import static de.schweinefilet.prototype.client.Client.CONTROL_HANDLER;
import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;
import static de.schweinefilet.prototype.client.Client.MAP_HOLDER;

import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.GameLoop;
import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.client.entity.User;
import de.schweinefilet.prototype.common.map.Map;
import de.schweinefilet.prototype.common.network.packet.PacketPlayerUpdate;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Modifies the user's {@link Player} entity based on the user input,
 * the other users activity and the {@link Map}.
 */
public class ControlLoop extends GameLoop {

    /**
     * The current velocity in the x- and y-direction.
     */
    private int velX, velY;

    /**
     * The last location before the {@link ControlLoop} ran.
     */
    private Point lastLoc;

    ControlLoop() {
        super("Control", 60);
        lastLoc = new Point();
    }

    /**
     * Checks the block and projectile collision, updates the {@link User}'s location and
     * sends a {@link PacketPlayerUpdate} to the server after a position change.
     * @param delta indicates how long the loop took
     */
    @Override
    public void process(double delta) {
        synchronized (ENTITY_HOLDER.getProjectiles()) {
        ENTITY_HOLDER.getProjectiles()
            .stream()
            .filter(projectile -> projectile.getTeam() != ENTITY_HOLDER.getUser().getTeam())
            .forEach(projectile -> {
                    if (ENTITY_HOLDER.getUser().getBounds().intersectsLine(projectile.getLine())) {
                        ENTITY_HOLDER.getUser().spawn();
                    }
            });
        }
        velX = 0;
        velY = 0;

        //check controls
        if (ENTITY_HOLDER.getUser().isUp()) {
            velY -= Vars.CONTROL.VELOCITY;
        }
        if (ENTITY_HOLDER.getUser().isDown()) {
            velY += Vars.CONTROL.VELOCITY;
        }

        if (ENTITY_HOLDER.getUser().isLeft()) {
            velX -= Vars.CONTROL.VELOCITY;
        }

        if (ENTITY_HOLDER.getUser().isRight()) {
            velX += Vars.CONTROL.VELOCITY;
        }

        //apply delta
        velX *= delta;
        velY *= delta;

        Player user = ENTITY_HOLDER.getUser();

        //check map bounds
        int nextX = user.getLocation().x + velX;
        int nextY = user.getLocation().y + velY;

        //alligned by -1 because the Graphics2D#fillOval fills with
        //a one pixel gap
        if (nextX <= 8) {
            velX = -user.getLocation().x + 8;
        } else if (nextX >= MAP_HOLDER.getMapWidth() - 9) {
            velX = MAP_HOLDER.getMapWidth() - 9 - user.getLocation().x;
        }

        if (nextY <= 8) {
            velY = -user.getLocation().y + 8;
        } else if (nextY >= MAP_HOLDER.getMapHeight() - 9) {
            velY = MAP_HOLDER.getMapHeight() - 9 - user.getLocation().y;
        }

        Rectangle userBounds = user.getBounds();

        Rectangle rectX = new Rectangle(userBounds);
        rectX.setLocation((int) rectX.getX() + velX, (int) rectX.getY());

        Rectangle rectY = new Rectangle(userBounds);
        rectY.setLocation((int) rectX.getX(), (int) rectX.getY() + velY);

        //block collision
        MAP_HOLDER.getBlocks().forEach(block -> {
            //x bounds
            if (block.getBounds().intersects(rectX)) {
                if (nextX >= block.getLocation().x
                    && user.getLocation().x <= block.getLocation().x) {
                    velX = block.getLocation().x
                        - user.getLocation().x - 9;
                } else if (nextX <= block.getLocation().x + block.getWidth()
                    && user.getLocation().x >= block.getLocation().x + block
                    .getWidth()) {
                    velX = block.getLocation().x
                        + block.getWidth()
                        - user.getLocation().x + 9;
                }
            }
            //y bounds
            if (block.getBounds().intersects(rectY)) {
                if (nextY >= block.getLocation().y
                    && user.getLocation().y <= block.getLocation().y) {
                    velY = block.getLocation().y
                        - user.getLocation().y - 9;
                } else if (nextY <= block.getLocation().y + block.getHeight()
                    && user.getLocation().y >= block.getLocation().y + block
                    .getHeight()) {
                    velY = block.getLocation().y
                        + block.getHeight()
                        - user.getLocation().y + 9;
                }
            }
        });

        user.move(velX, velY);

        //update the flag position
        MAP_HOLDER.getFlag().updateLocation();

        //sends a position update to the server
        Point newLoc = new Point(lastLoc.x + velX, lastLoc.y + velY);
        if (!lastLoc.equals(newLoc)) {
            Client.NETWORK_HANDLER.updateUserLocation();
            lastLoc = newLoc;
        }
    }

    @Override
    protected void onFirstRun() {
    }
}