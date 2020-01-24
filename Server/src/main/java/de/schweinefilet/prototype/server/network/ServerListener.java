package de.schweinefilet.prototype.server.network;


import static de.schweinefilet.prototype.server.Server.MAP_HOLDER;
import static de.schweinefilet.prototype.server.Server.MATCH_CONTROLLER;
import static de.schweinefilet.prototype.server.Server.NETWORK_HANDLER;

import com.esotericsoftware.kryonet.Connection;
import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.entity.Flag;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.network.PacketListener;
import de.schweinefilet.prototype.common.network.packet.DenyReason;
import de.schweinefilet.prototype.common.network.packet.Packet;
import de.schweinefilet.prototype.common.network.packet.PacketDisconnect;
import de.schweinefilet.prototype.common.network.packet.PacketFlagDrop;
import de.schweinefilet.prototype.common.network.packet.PacketFlagPickup;
import de.schweinefilet.prototype.common.network.packet.PacketHandshake;
import de.schweinefilet.prototype.common.network.packet.PacketMatchInfo;
import de.schweinefilet.prototype.common.network.packet.PacketPlayerUpdate;
import de.schweinefilet.prototype.common.network.packet.PacketWin;
import de.schweinefilet.prototype.common.util.LogUtil;
import de.schweinefilet.prototype.server.Server;

/**
 * This class implements the server side packet receive logic.
 */
class ServerListener extends PacketListener {

    @Override
    public void connected(Connection connection) {
        LogUtil.info(connection + " connected...");
    }

    /**
     * Called when a client disconnects. Used to inform the other
     * still connected clients about the disconnection of other client.
     *
     * @param connection the connection that disconnected
     */
    @Override
    public void disconnected(Connection connection) {
        if (NETWORK_HANDLER.contains(connection)) {
            Player affiliatedPlayer = NETWORK_HANDLER.getAffiliatedPlayer(connection);
            MAP_HOLDER.removePlayer(affiliatedPlayer);
            LogUtil.info("Removed " + affiliatedPlayer);
            NETWORK_HANDLER.removeConnection(connection);
            NETWORK_HANDLER.sendUDPExcept(connection, new PacketDisconnect(affiliatedPlayer));

            Flag flag = MAP_HOLDER.getMap().getFlag();
            flag.updateLocation();
            if (flag.isPickedUp() && flag.getPicker().equals(affiliatedPlayer)) {
                LogUtil.info("Dropped flag on disconnect");
                flag.drop();
            }

            Server.updateGUI();
        } else {
            LogUtil.warn("A connection disconnected without having a player! " +
                "Maybe the client got kicked?");
        }
    }

    @Override
    protected void onParsingError(Exception exc) {
    }

    @Override
    public void processPacket(Connection connection, Packet packet) {
        if (MAP_HOLDER.getMap() == null) {
            LogUtil.warn("Map not loaded! Ignoring packet...");
            return;
        }

        switch(packet.getUsage()) {
            case HANDSHAKE:
                PacketHandshake receivedHandshake = (PacketHandshake) packet;
                Player receivedPlayer = receivedHandshake.getPlayer();

                //the client is most likely to have the wrong version
                if (!Vars.VERSION.equals(receivedHandshake.getVersion())) {
                    LogUtil.info(
                        "Rejected HANDSHAKE: versions don't match! Wanted: " + Vars.VERSION
                            +
                            ", Received: " + receivedHandshake.getVersion());
                    PacketHandshake denyHandshake
                        = new PacketHandshake(DenyReason.WRONG_VERSION);
                    connection.sendUDP(denyHandshake);
                    return;
                }

                //the server is full
                if (MAP_HOLDER.getMap().getMaxPlayers() == MAP_HOLDER
                    .getPlayers().size()) {
                    LogUtil.info("Rejected HANDSHAKE: player maximum reached");
                    PacketHandshake denyHandshake
                        = new PacketHandshake(DenyReason.TOO_MANY_PLAYERS);
                    connection.sendUDP(denyHandshake);
                    return;
                }

                //the name is already used
                if (MAP_HOLDER.hasUsedName(receivedPlayer.getName())) {
                    LogUtil.info("Rejected HANDSHAKE: player name already used");
                    PacketHandshake denyHandshake
                        = new PacketHandshake(DenyReason.NAME_USED);
                    connection.sendUDP(denyHandshake);
                    return;
                }

                //the player is already known
                if (MAP_HOLDER.getPlayers().contains(receivedPlayer)) {
                    LogUtil.info("Rejected HANDSHAKE: player object already exists");
                    PacketHandshake denyHandshake
                        = new PacketHandshake(DenyReason.PLAYER_EXISTS);
                    connection.sendUDP(denyHandshake);
                    return;
                }

                LogUtil.info("Accepted HANDSHAKE for player " + receivedPlayer);
                MAP_HOLDER.getPlayers().add(receivedPlayer);
                PacketHandshake acceptHandshake = new PacketHandshake(
                    MAP_HOLDER.getMap().getName());
                connection.sendUDP(acceptHandshake);
                NETWORK_HANDLER.addConnection(connection, receivedPlayer);

                //fix not appearing players for the joined player until they moved one time
                //and not properly set flags
                MAP_HOLDER.getPlayers()
                    .stream()
                    .filter(player -> !player.equals(receivedPlayer))
                    .forEach(player -> {
                        connection.sendUDP(new PacketPlayerUpdate(player));
                        LogUtil.info("Fixed invisible players for " + player.getName());
                    });

                if (MAP_HOLDER.getMap().getFlag().isPickedUp()) {
                    connection.sendUDP(new PacketFlagPickup(MAP_HOLDER
                        .getMap().getFlag().getPicker().getUniqueId()));
                }

                //predict the receivedPlayer position and force an update for the other players
                if (receivedPlayer.getLocation().distance(0, 0) == 0) {
                    receivedPlayer.setLocation(
                        MAP_HOLDER.getMap().getAssociatedSpawn(receivedPlayer.getTeam())
                            .getLocation());
                    NETWORK_HANDLER
                        .sendUDPExcept(connection, new PacketPlayerUpdate(receivedPlayer));
                }

                //update team wins
                connection.sendUDP(new PacketMatchInfo(MATCH_CONTROLLER.getRedWins(),
                    MATCH_CONTROLLER.getBlueWins()));

                //update flag
                MAP_HOLDER.getMap().getFlag().updateLocation();
                if (MAP_HOLDER.getMap().getFlag().isPickedUp()) {
                    connection.sendUDP(new PacketFlagPickup(MAP_HOLDER
                        .getMap().getFlag().getPicker().getUniqueId()));
                } else if (!MAP_HOLDER.getMap().getFlag().isAtBase()) {
                    connection.sendUDP(new PacketFlagDrop(
                        MAP_HOLDER.getMap().getFlag().getLocation()));
                }

                //update gui
                Server.updateGUI();
                break;
            case PLAYER_UPDATE:
                Player updatePlayer = ((PacketPlayerUpdate) packet).getPlayer();
                if (!MAP_HOLDER.getPlayers().contains(updatePlayer)) {
                    LogUtil.warn(
                        "A client (" + connection.getRemoteAddressTCP() + ":" + connection
                            .getRemoteAddressTCP().getPort() +
                            ") tried updating a player (" + updatePlayer.getName() +
                            ") without being in the list.");
                    return;
                }

                if (!NETWORK_HANDLER.getAffiliatedPlayer(connection).equals(updatePlayer)) {
                    LogUtil.warn("A client (" + connection.getRemoteAddressTCP() + ":" + connection
                        .getRemoteAddressTCP().getPort() +
                        ") tried updating the wrong player.");
                    connection.close();
                    return;
                }

                LogUtil.info("Updated location for " + updatePlayer.getName() +
                    " | X: " + updatePlayer.getLocation().getX() +
                    " | Y: " + updatePlayer.getLocation().getY());
                MAP_HOLDER.getPlayers()
                    .stream()
                    .filter(player -> player.equals(updatePlayer))
                    .forEach(player -> player.setLocation(updatePlayer.getLocation()));
                NETWORK_HANDLER.sendUDPExcept(connection, packet);
                //update gui
                Server.updateGUI();
                break;
            case FLAG_PICKUP:
                Player pickupPlayer = MAP_HOLDER
                    .getPlayer(((PacketFlagPickup) packet).getPickerId());
                if (pickupPlayer != null) {
                    MAP_HOLDER.getMap().getFlag().setPicker(pickupPlayer);
                    NETWORK_HANDLER.sendUDPExcept(connection, packet);
                } else {
                    LogUtil.warn("Player " + ((PacketFlagPickup) packet).getPickerId()
                        + " for FLAG_PICKUP not found");
                }
                break;
            case FLAG_DROP:
                MAP_HOLDER.getMap().getFlag().updateLocation();
                NETWORK_HANDLER.sendUDPExcept(connection, packet);
                if (MAP_HOLDER.getMap().getFlag().isPickedUp()) {
                    MAP_HOLDER.getMap().getFlag().drop();
                }
                break;
            case WIN:
                NETWORK_HANDLER.sendUDPExcept(connection, packet);
                MATCH_CONTROLLER.win(((PacketWin) packet).getTeam());
                MAP_HOLDER.getMap().getFlag().reset();
                break;
            case SHOT:
                NETWORK_HANDLER.sendUDPExcept(connection, packet);
                break;
            case KICK:
                LogUtil.warn("Kick?!");
                break;
            case CHANGE_TEAM:
                LogUtil.warn("Change Team?!");
                break;
            case MATCH:
                LogUtil.warn("Match?!");
                break;
            case DISCONNECT:
                LogUtil.warn("Disconnect?!");
                break;
        }
    }

    @Override
    protected void unknownObject(Connection connection, Object object) {
        LogUtil.warn("Received unknown object " + object);
    }

    @Override
    protected void noUsage(Connection connection) {
        LogUtil.warn("A packet without a usage was received from (" +
            connection.getRemoteAddressTCP() + ":" + connection
            .getRemoteAddressTCP().getPort() + ". Closing connection!");
        connection.close();
    }
}
