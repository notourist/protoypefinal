package de.schweinefilet.prototype.common.network.packet;

import de.schweinefilet.prototype.common.entity.Projectile;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Sent when a projectile was shot.
 */
@NoArgsConstructor
public class PacketShot extends Packet {

    @Getter
    private Projectile projectile;

    public PacketShot(Projectile projectile) {
        super(PacketUsage.SHOT);
        this.projectile = projectile;
    }
}
