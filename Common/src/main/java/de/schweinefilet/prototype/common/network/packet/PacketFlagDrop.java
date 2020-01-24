package de.schweinefilet.prototype.common.network.packet;

import java.awt.Point;
import lombok.Getter;

/**
 * Informs the clients about a flag drop.
 */
public class PacketFlagDrop extends Packet {

    @Getter
    private Point location;

    public PacketFlagDrop() {
        super(PacketUsage.FLAG_DROP);
    }

    public PacketFlagDrop(Point location) {
        super(PacketUsage.FLAG_DROP);
        this.location = location;
    }
}
