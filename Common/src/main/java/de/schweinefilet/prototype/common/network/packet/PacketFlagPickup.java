package de.schweinefilet.prototype.common.network.packet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Send when a player pickups a flag.
 */
@NoArgsConstructor
public class PacketFlagPickup extends Packet {

    @Getter
    private String pickerId;

    public PacketFlagPickup(@NonNull String pickerId) {
        super(PacketUsage.FLAG_PICKUP);
        this.pickerId = pickerId;
    }
}
