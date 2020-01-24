package de.schweinefilet.prototype.client.entity;

import java.awt.*;

import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.entity.*;
import de.schweinefilet.prototype.common.network.packet.*;
import de.schweinefilet.prototype.common.util.LogUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import static de.schweinefilet.prototype.client.Client.*;

/**
 * The user controlled {@link Player}.
 */
@NoArgsConstructor
public class User extends Player {

    private int width, height;

    /**
     * Represents the current direction the {@link User} is going.
     */
    @Getter
    @Setter
    private boolean up, right, down, left;

    /**
     * True, if the {@link User} hasn't teleported this round, else false.
     * Reset after a win.
     */
    @Getter
    @Setter
    private boolean teleported;

    User(@NonNull String name, @NonNull Team team) {
        super(name, team);
        teleported = false;
    }

    /**
     * Teleports the user to the mouse location.
     *
     * @param mouseLoc the current mouse location.
     */
    public void teleport(Point mouseLoc) {
        //check picked flag
        if (MAP_HOLDER.getFlag().isPickedUp() && MAP_HOLDER.getFlag().getPicker().equals(ENTITY_HOLDER.getUser())) {
            return;
        }

        //check the teleport cooldown
        if (!teleported) {
            Rectangle userBounds = new Rectangle(ENTITY_HOLDER.getUser().getBounds());
            userBounds.setLocation(CONTROL_HANDLER.calculateMousePoint());

            boolean canTeleport = true;
            for (Block block : MAP_HOLDER.getBlocks()) {
                if (userBounds.intersects(block.getBounds())) {
                    canTeleport = false;
                }
            }
            //set the new position
            if (canTeleport) {
                NETWORK_HANDLER.sendPacket(new PacketPlayerUpdate(ENTITY_HOLDER.getUser().asPlayer()));
                ENTITY_HOLDER.getUser().setLocation(CONTROL_HANDLER.calculateMousePoint());
                teleported = true;
            }
        }
    }

    /**
     * Creates a {@link Projectile} in the current direction the mouse is going.
     */
    public void shoot() {
        Point userLoc = ENTITY_HOLDER.getUser().getLocation();
        Point endLoc = CONTROL_HANDLER.calculateMousePoint();

        Point diff = new Point(endLoc.x - userLoc.x, endLoc.y - userLoc.y);
        double amount = Math.sqrt((diff.x * diff.x) + (diff.y * diff.y));

        endLoc
                .setLocation(userLoc.x + (300 / amount) * diff.x, userLoc.y + (300 / amount) * diff.y);

        Projectile projectile = new Projectile(
                new Point(userLoc), endLoc, ENTITY_HOLDER.getUser().getTeam(), 500);
        NETWORK_HANDLER.sendPacket(new PacketShot(projectile));
        ENTITY_HOLDER.addProjectile(projectile);
    }

    /**
     * This method sets the user as the flag picker.
     */
    public void interact() {
        if (!MAP_HOLDER.getFlag().isPickedUp()
                && MAP_HOLDER.getFlag().getLocation().distance(
                ENTITY_HOLDER.getUser().getLocation()) <= Vars.CONTROL.FLAG_PICK) {
            //check if the user is in range
            pickupFlag();
        } else if (MAP_HOLDER.getFlag().isPickedUp()
                && MAP_HOLDER.getFlag().getPicker().equals(ENTITY_HOLDER.getUser())
                && MAP_HOLDER.getMap().getNotAssociatedSpawn(ENTITY_HOLDER.getUser().getTeam()).getLocation()
                .distance(ENTITY_HOLDER.getUser().getLocation())
                <= Vars.CONTROL.FLAG_LAY) {
            dropFlag();
        }
    }

    /**
     * Picks the flag.
     */
    private void pickupFlag() {
        LogUtil.info("Picked flag @ " + MAP_HOLDER.getFlag().getLocation());
        //send packet to inform other clients
        NETWORK_HANDLER.sendPacket(
                new PacketFlagPickup(ENTITY_HOLDER.getUser().getUniqueId()));
        //set the user as flag picker
        MAP_HOLDER.getFlag().setPicker(ENTITY_HOLDER.getUser().asPlayer());
    }

    /**
     * Drops the flag down.
     */
    private void dropFlag() {
        LogUtil.info("Send WIN");
        NETWORK_HANDLER.sendPacket(new PacketWin(ENTITY_HOLDER.getUser().getTeam()));
        Client.MATCH_CONTROLLER.win(ENTITY_HOLDER.getUser().getTeam());
        Client.reset();
    }

    /**
     * Spawns the user at the spawn which has the same team as the user and also drops the
     * {@link Flag} if the {@link User} has it picked up.
     */
    public void spawn() {
        //reset flag
        if (MAP_HOLDER.getFlag().isPickedUp()
                && MAP_HOLDER.getFlag().getPicker().equals(ENTITY_HOLDER.getUser())) {
            MAP_HOLDER.getFlag().drop();
            NETWORK_HANDLER.sendPacket(new PacketFlagDrop());
        }
        //get the spawn
        Spawn spawn = MAP_HOLDER.getMap().getAssociatedSpawn(ENTITY_HOLDER.getUser().getTeam());
        //move the player and add one to center him
        ENTITY_HOLDER.getUser().setLocation(spawn.getLocation().x, spawn.getLocation().y);
        //force position update to fix players spawning at the wrong position
        Client.NETWORK_HANDLER.updateUserLocation();
    }

    /**
     * Returns the user as a player object.
     * @return -
     */
    public Player asPlayer() {
        return new Player(location, uniqueId, name, team);
    }

    /**
     * Stops all movement. Used to prevent the user moving after tabbing out/minimizing the game.
     */
    public void stopMovement() {
        setRight(false);
        setLeft(false);
        setUp(false);
        setDown(false);
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Draws this {@link User} into the middle of the screen aligned by -9
     *
     * @param graphics2D the {@link Graphics2D} object that draws the user
     * @param offsetX unused
     * @param offsetY unused
     */
    @Override
    public void draw(Graphics2D graphics2D, int offsetX, int offsetY) {
        graphics2D.setColor(team.getColor());
        graphics2D.fillOval((width / 2) - 9, (height / 2) - 9, 18, 18);
    }
}
