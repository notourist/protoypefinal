package de.schweinefilet.prototype.client.entity;

import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.entity.Projectile;
import de.schweinefilet.prototype.common.entity.Team;
import de.schweinefilet.prototype.common.util.LogUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

/**
 * Holds all entities and manipulation methods.
 */
public class EntityHolder {

    /**
     * The user object.
     */
    @Getter
    private final User user;

    /**
     * All the players the client knows of.
     */
    private final Set<Player> players;

    /**
     * Contains all the projectiles the client knows of.
     */
    private final Set<Projectile> projectiles;

    /**
     * Runs every {@link ProjectileRemover}.
     */
    private final ScheduledExecutorService scheduledExecutor;

    public EntityHolder(String name, String team) {
        players = Collections.synchronizedSet(new HashSet<Player>());
        user = new User(name, Team.valueOf(team));
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        projectiles = Collections.synchronizedSet(new HashSet<Projectile>());
    }

    /**
     * Adds a projectile and starts a {@link ProjectileRemover}.
     *
     * @param projectile The new projectile
     */
    public synchronized void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        int liveTime = projectile.getLiveTime();
        scheduledExecutor
            .schedule(new ProjectileRemover(projectile.getUniqueId()),
                liveTime > 0 ? liveTime : Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    /**
     * Called by a {@link ProjectileRemover} to remove a {@link Projectile}.
     *
     * @param uniqueId The projectile Id
     */
    synchronized void removeProjectile(String uniqueId) {
        projectiles.removeIf(projectile -> projectile.getUniqueId().equals(uniqueId));
    }

    /**
     * Iterates through the {@link HashSet} of known players and updates the given player's
     * location. If the Set doesn't contain the player, he is added.
     *
     * @param update the player, who's location needs to be updated
     */
    public synchronized void updateLocation(Player update) {
        //check if the player is known
        if (players.contains(update)) {
            //iterate through the HashSet
            players.stream()
                .filter(update::equals)
                .forEach(
                    player -> player.setLocation(update.getLocation().x, update.getLocation().y));
        } else {
            //the player isn't known -> add him to the Set
            LogUtil.info("Added player " + update);
            players.add(new Player(update.getLocation(), update.getUniqueId(), update.getName(),
                update.getTeam()));
        }
    }

    /**
     * Wrapper function, removes the given player.
     *
     * @param player the player who needs to be removed
     */
    public synchronized void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Wrapper function, checks if a player is known.
     *
     * @param player the player who needs to be checked
     * @return true, if the player is known, else false
     */
    public boolean contains(Player player) {
        return players.contains(player);
    }

    /**
     * Gives the player who has the given UUID.
     *
     * @param uniqueId The UUID as a String, which is used to check if the user is known
     * @return the player, if he was found. If not, null.
     */
    public synchronized Player getPlayer(String uniqueId) {
        for (Player player : players) {
            if (player.getUniqueId().equals(uniqueId)) {
                return player;
            }
        }
        return null;
    }

    /**
     * -
     * @return {@link EntityHolder#projectiles}
     */
    public synchronized Set<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * -
     * @return {@link EntityHolder#players}
     */
    public synchronized Set<Player> getPlayers() {
        return players;
    }
}
