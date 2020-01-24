package de.schweinefilet.prototype.common.network.packet;

import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Send to inform the server that a client is trying to connect.
 * The client uses the {@link PacketHandshake#PacketHandshake(Player)} constructor.
 * <p>When the Handshake was denied the server uses
 * the {@link PacketHandshake#PacketHandshake(DenyReason)}
 * Handshake or {@link PacketHandshake#PacketHandshake(String)}
 * when the Handshake was accepted.</p>
 */
@NoArgsConstructor
public class PacketHandshake extends Packet {

    @Getter
    private String mapName;
    @Getter
    private boolean accepted;
    @Getter
    private Player player;
    @Getter
    private String version;
    @Getter
    private DenyReason denyReason;

    /**
     * Used by the server to inform the client about acceptance
     * of the client's handshake.
     *
     * @param mapName the map name
     */
    public PacketHandshake(@NonNull String mapName) {
        super(PacketUsage.HANDSHAKE);
        this.mapName = mapName;
        this.accepted = true;
    }

    /**
     * Used when the server denies the client's Handshake attempt-
     *
     * @param denyReason The reason the Handshake was denied
     */
    public PacketHandshake(@NonNull DenyReason denyReason) {
        super(PacketUsage.HANDSHAKE);
        this.denyReason = denyReason;
        this.accepted = false;
    }

    /**
     * Used by the client to inform the server about his connection attempt.
     *
     * @param player the player object of the connecting client
     */
    public PacketHandshake(@NonNull Player player) {
        super(PacketUsage.HANDSHAKE);
        this.player = player;
        this.version = Vars.VERSION;
    }
}
