package de.schweinefilet.prototype.server.map;

import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.map.MapHolder;
import java.util.HashSet;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A class which is used for updating and storing map and player related information.
 */
@NoArgsConstructor
public class ServerMapHolder extends MapHolder {

    /**
     * Every connected {@link Player} is stored here.
     */
    @Getter
    private HashSet<Player> players;

    /**
     * Checks if a name us already used.
     * @param name the new name
     * @return true if the name is used, else false
     */
    public boolean hasUsedName(String name) {
        boolean used = false;
        for (Player player : players) {
            if (player.getName().equals(name)) {
                used = true;
            }
        }
        return used;
    }

    /**
     * Gets a player based on his {@link Player#uniqueId}.
     * @param uniqueId the {@link Player}'s {@link UUID}.
     * @return the player, if the {@link UUID} was found, else null.
     */
    public Player getPlayer(String uniqueId) {
        for (Player player : players) {
            if (player.getUniqueId().equals(uniqueId)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Removes the given player.
     * @param player -
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    protected void onMapDoesNotExist(String name) {
    }

    @Override
    protected void onMapLoadError(String name) {
    }

    /**
     * Initializes {@link ServerMapHolder#players} with the number of maximal players.
     */
    @Override
    protected void onMapLoadSuccess() {
        this.players = new HashSet<>(map.getMaxPlayers());
    }
}
