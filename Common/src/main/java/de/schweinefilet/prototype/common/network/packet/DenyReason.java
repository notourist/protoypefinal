package de.schweinefilet.prototype.common.network.packet;

import lombok.Getter;

/**
 * The reason the {@link PacketHandshake} was not accepted.
 */
public enum DenyReason {
    TOO_MANY_PLAYERS("Der Server ist voll"),
    WRONG_VERSION("Der Client hat die falsche Version."),
    NAME_USED("Dein Name wird bereits benutzt"),
    PLAYER_EXISTS("Der Spieler existierts bereits");

    /**
     * A readable explanation of the {@link DenyReason}.
     */
    @Getter
    String readable;

    DenyReason(String readable) {
        this.readable = readable;
    }
}
