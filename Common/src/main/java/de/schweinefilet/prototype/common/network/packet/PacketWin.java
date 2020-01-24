package de.schweinefilet.prototype.common.network.packet;

import de.schweinefilet.prototype.common.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Send when a player brings the flag to his team's spawn.
 * Every client resets the map upon receiving.
 */
@NoArgsConstructor
public class PacketWin extends Packet {

    @Getter
    private Team team;

    public PacketWin(@NonNull Team team) {
        super(PacketUsage.WIN);
        this.team = team;
    }
}
