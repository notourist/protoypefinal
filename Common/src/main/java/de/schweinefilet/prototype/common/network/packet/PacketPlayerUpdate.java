package de.schweinefilet.prototype.common.network.packet;

import de.schweinefilet.prototype.common.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Send when a player moves to inform other clients about the location change of the sender.
 */
@NoArgsConstructor
public class PacketPlayerUpdate extends Packet {

    @Getter
    private Player player;

    public PacketPlayerUpdate(@NonNull Player player) {
        super(PacketUsage.PLAYER_UPDATE);
        this.player = player;
    }
}
