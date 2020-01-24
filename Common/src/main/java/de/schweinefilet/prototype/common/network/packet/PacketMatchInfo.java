package de.schweinefilet.prototype.common.network.packet;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Contains information about the running match on a server the client is connecting to.
 */
@NoArgsConstructor
public class PacketMatchInfo extends Packet {

    /**
     * The number of wins both teams have.
     */
    @Getter
    private int redWins, blueWins;

    public PacketMatchInfo(int redWins, int blueWins) {
        super(PacketUsage.MATCH);
        this.redWins = redWins;
        this.blueWins = blueWins;
    }
}
