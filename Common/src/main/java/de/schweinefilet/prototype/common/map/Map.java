package de.schweinefilet.prototype.common.map;

import de.schweinefilet.prototype.common.entity.Block;
import de.schweinefilet.prototype.common.entity.Flag;
import de.schweinefilet.prototype.common.entity.Spawn;
import de.schweinefilet.prototype.common.entity.Team;
import de.schweinefilet.prototype.common.frame.draw.DrawableSize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class holds all map related objects like blocks, spawns etc.
 */
@NoArgsConstructor
public class Map implements DrawableSize {

    /**
     * The map name.
     */
    @Getter
    private String name;

    /**
     * A {@link HashSet} with all the spawns.
     */
    @Getter
    private HashSet<Spawn> spawns;

    /**
     * A {@link HashSet} with all the blocks.
     */
    @Getter
    private HashSet<Block> blocks;

    /**
     * The maximal number of players, who can play on the map.
     */
    @Getter
    private byte maxPlayers;

    /**
     * The width and height of the map.
     */
    @Getter
    private int width, height;

    /**
     * The {@link Flag} on the map.
     */
    @Getter
    private Flag flag;

    /**
     * Constructor used for map building
     *
     * @param name the map name
     * @param spawns the spawns for the players
     * @param blocks the blocks of the map
     * @param maxPlayers the maximal number of players
     * @param width the width of the map
     * @param height the height of the map
     * @param flag the flag that is used
     */
    public Map(String name,
        HashSet<Spawn> spawns,
        HashSet<Block> blocks,
        byte maxPlayers,
        int width,
        int height,
        Flag flag) {
        this.name = name;
        this.spawns = spawns;
        this.blocks = blocks;
        this.maxPlayers = maxPlayers;
        this.width = width;
        this.height = height;
        this.flag = flag;
    }

    /**
     * Draws the {@link Map} with the {@link Graphics2D} object.
     *
     * @param graphics2D the {@link Graphics2D} object that draws the flag
     * @param offsetX the offset on the x-axis the {@link Map} needs to be moved.
     * @param offsetY the offset on the y-axis the {@link Map} needs to be moved.
     * @param width the width of the map
     * @param height the height of the map
     */
    @Override
    public void draw(Graphics2D graphics2D, int offsetX, int offsetY, int width, int height) {
        graphics2D.setColor(Color.BLACK);
        graphics2D
            .fillRect(offsetX, offsetY, width, height);
    }

    /**
     * Returns the spawn with the same team as the argument.
     *
     * @param team the team which is used for search
     * @return The spawn that has the same team as the given one
     */
    public Spawn getAssociatedSpawn(Team team) {
        return (Spawn) spawns
            .stream()
            .filter(spawn -> spawn.getTeam() == team)
            .toArray()[0];
    }

    /**
     * Returns the spawn without the same team as the argument.
     *
     * @param team the team which is used for search
     * @return The spawn that has not the same team as the given one
     */
    public Spawn getNotAssociatedSpawn(Team team) {
        return (Spawn) spawns
            .stream()
            .filter(spawn -> spawn.getTeam() != team)
            .toArray()[0];
    }
}
