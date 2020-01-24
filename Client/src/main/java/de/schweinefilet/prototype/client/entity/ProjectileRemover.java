package de.schweinefilet.prototype.client.entity;

import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;

import de.schweinefilet.prototype.common.entity.Projectile;

/**
 * Removes a {@link Projectile} after the Projectiles
 * {@link Projectile#liveTime} has run down.
 */
public class ProjectileRemover implements Runnable {

    /**
     * The projectiles {@link Projectile#uniqueId} used for removing it.
     */
    private final String projectileId;

    ProjectileRemover(String projectileId) {
        this.projectileId = projectileId;
    }

    @Override
    public void run() {
        ENTITY_HOLDER.removeProjectile(projectileId);
    }
}
