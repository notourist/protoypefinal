package de.schweinefilet.prototype.common.network.packet;

import de.schweinefilet.prototype.common.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Send when a player disconnects.
 */
@NoArgsConstructor
public class PacketDisconnect extends Packet {

    @Getter
    private Player player;

    public PacketDisconnect(@NonNull Player player) {
        super(PacketUsage.DISCONNECT);
        this.player = player;
    }
}
