package de.schweinefilet.prototype.client.frame.paint;

import static de.schweinefilet.prototype.client.Client.CONTROL_HANDLER;
import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;
import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;
import static de.schweinefilet.prototype.client.Client.HUD_HOLDER;
import static de.schweinefilet.prototype.client.Client.MAP_HOLDER;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Describes a method to paint the game. Different strategies have different CPU/GPU usages. This
 * are some benchmarks: CPU: Intel(R) Core(TM)2 Duo CPU P8400 @ 2.26GHz, RAM: 2GB, Screen
 * resolution: 1440x900 <p><b>All benchmarks were run with the server on the same PC!</b></p>
 * <p>Data:
 * <ul>
 *     <li>{@link PaintStrategyBufferedImage} - around 75% CPU usage, screen tearing, doesn't need dedicated GPU</li>
 *     <li>{@link PaintStrategyPageFlipping} - around 35% CPU usage, no screen tearing, needs dedicated GPU to function correctly</li>
 *     <li>{@link PaintStrategyVolatileImage} - around 25% CPU usage, some screen tearing, a dedicated GPU is advised</li>
 * </ul>
 */
public interface PaintStrategy {

    /**
     * See {@link PaintStrategyBufferedImage}.
     */
    int BUFFERED_IMAGE = 1;

    /**
     * See {@link PaintStrategyPageFlipping}.
     */
    int PAGE_FLIPPING = 2;

    /**
     * See {@link PaintStrategyVolatileImage}.
     */
    int VOLATILE_IMAGE = 3;

    /**
     * The public paint method. Should call {@link PaintStrategy#paintGame(Graphics2D)}
     *
     * @param graphics {@link Graphics} object based on the current {@link PaintStrategy}.
     */
    void paint(Graphics graphics);

    /**
     * -
     *
     * @return true, when an {@link java.awt.Image} is used for drawing
     */
    boolean isImagePowered();

    /**
     * Returns the strategy specifics name.
     *
     * @return the name of the {@link PaintStrategy}
     */
    String getName();

    /**
     * Returns the strategy specifics ID.
     *
     * @return the ID of the {@link PaintStrategy}
     */
    int getId();

    /**
     * Draws the game with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object based on the current {@link PaintStrategy}.
     */
    default void paintGame(Graphics2D graphics2D) {
        if (MAP_HOLDER.getOptionalMap().isPresent()) {
            MAP_HOLDER.getOptionalMap().ifPresent(map -> {
                ENTITY_HOLDER.getUser()
                    .setDimensions(FRAME_HANDLER.getWidth(), FRAME_HANDLER.getHeight());

                //paint the background
                graphics2D.setColor(Color.DARK_GRAY);
                graphics2D.fillRect(0, 0, FRAME_HANDLER.getWidth(), FRAME_HANDLER.getHeight());

                //calculate the offset and set it
                int offsetX =
                    -ENTITY_HOLDER.getUser().getLocation().x + (FRAME_HANDLER.getWidth()
                        / 2);
                int offsetY =
                    -ENTITY_HOLDER.getUser().getLocation().y + (FRAME_HANDLER.getHeight()
                        / 2);

                //paint the map
                map.draw(graphics2D, offsetX, offsetY, MAP_HOLDER.getMapWidth(),
                    MAP_HOLDER.getMapHeight());

                //paint the projectiles
                ENTITY_HOLDER.getProjectiles()
                    .forEach(projectile -> projectile.draw(graphics2D, offsetX, offsetY));

                //paint the blocks, when the map has some
                map.getBlocks().forEach(block -> block.draw(graphics2D, offsetX, offsetY));

                //paint the two spawns
                map.getSpawns().forEach(spawn -> spawn.draw(graphics2D, offsetX, offsetY));

                //paint all the players
                ENTITY_HOLDER.getPlayers()
                    .forEach(player -> player.draw(graphics2D, offsetX, offsetY));

                //paint the user
                ENTITY_HOLDER.getUser().draw(graphics2D, offsetX, offsetY);

                //paint the flag
                map.getFlag().draw(graphics2D, offsetX, offsetY);

                //paint the HUD
                HUD_HOLDER.drawAll(graphics2D);

                //paint the mouse
                CONTROL_HANDLER.getMouse().draw(graphics2D);
            });
        }
    }
}
