package de.schweinefilet.prototype.common.network.packet;

/**
 * Holds all the usages a packet can have.
 * Used for faster (and easier) testing and casting a packet.
 */
public enum PacketUsage {
    HANDSHAKE,
    PLAYER_UPDATE,
    FLAG_PICKUP,
    FLAG_DROP,
    WIN,
    SHOT,
    KICK,
    CHANGE_TEAM,
    MATCH,
    DISCONNECT
}
