package de.schweinefilet.prototype.common.network.packet;

/**
 * Informs the client that he was kicked.
 */
public class PacketKick extends Packet {

    public PacketKick() {
        super(PacketUsage.KICK);
    }
}
