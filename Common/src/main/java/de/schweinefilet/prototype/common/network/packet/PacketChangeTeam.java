package de.schweinefilet.prototype.common.network.packet;

/**
 * Changes the team of the receiving client.
 */
public class PacketChangeTeam extends Packet {

    public PacketChangeTeam() {
        super(PacketUsage.CHANGE_TEAM);
    }
}
