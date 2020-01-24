package de.schweinefilet.prototype.common.network.packet;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * The base packet which is used by every other packet.
 */
@NoArgsConstructor
public class Packet implements Serializable {

    @Getter
    private PacketUsage usage;

    private static final long serialVersionUID = 0;

    protected Packet(@NonNull PacketUsage usage) {
        this.usage = usage;
    }
}
