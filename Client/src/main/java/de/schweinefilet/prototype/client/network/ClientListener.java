package de.schweinefilet.prototype.client.network;

import static de.schweinefilet.prototype.client.Client.CONTROL_HANDLER;
import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;
import static de.schweinefilet.prototype.client.Client.MAP_HOLDER;
import static de.schweinefilet.prototype.client.Client.MATCH_CONTROLLER;

import com.esotericsoftware.kryonet.Connection;
import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.entity.Team;
import de.schweinefilet.prototype.common.network.PacketListener;
import de.schweinefilet.prototype.common.network.packet.Packet;
import de.schweinefilet.prototype.common.network.packet.PacketDisconnect;
import de.schweinefilet.prototype.common.network.packet.PacketFlagDrop;
import de.schweinefilet.prototype.common.network.packet.PacketFlagPickup;
import de.schweinefilet.prototype.common.network.packet.PacketHandshake;
import de.schweinefilet.prototype.common.network.packet.PacketMatchInfo;
import de.schweinefilet.prototype.common.network.packet.PacketPlayerUpdate;
import de.schweinefilet.prototype.common.network.packet.PacketShot;
import de.schweinefilet.prototype.common.network.packet.PacketWin;
import de.schweinefilet.prototype.common.util.LogUtil;

/**
 * This class implements the client side packet receive logic.
 */
class ClientListener extends PacketListener {

    @Override
    public void disconnected(Connection connection) {
        LogUtil.info("Disconnected");
        UILog.showBlockingInfo("Verbindung getrennt.");
        Client.stop();
    }

    @Override
    public void connected(Connection connection) {
        LogUtil.info("Connected to " + connection.getRemoteAddressTCP().getAddress() +
            ":" + connection.getRemoteAddressTCP().getPort());
    }

    @Override
    public void processPacket(Connection connection, Packet packet) {
        switch(packet.getUsage()) {
            case HANDSHAKE:
                PacketHandshake handshake = (PacketHandshake) packet;
                //check if the handshake was accepted
                if (handshake.isAccepted()) {
                    //Handshake accepted
                    LogUtil.info("Accepted HANDSHAKE");
                    //load the map
                    MAP_HOLDER.setMap(handshake.getMapName());
                } else {
                    //Handshake not accepted
                    LogUtil.error("Not accepted HANDSHAKE");
                    UILog.showError("Der Handshake wurde vom Server nicht akzeptiert!"
                        + "\nGrund: " + handshake.getDenyReason().getReadable()
                        + "\nFehler: " + handshake.getDenyReason().toString());
                    Client.stop();
                    System.exit(1);
                }
                break;
            case PLAYER_UPDATE:
                Player locationUpdatePlayer = ((PacketPlayerUpdate) packet).getPlayer();
                //check if the received player is the same as the user
                if (locationUpdatePlayer != ENTITY_HOLDER.getUser()) {
                    //update the player's location
                    ENTITY_HOLDER.updateLocation(locationUpdatePlayer);
                } else {
                    LogUtil.warn("Wrong user for PLAYER_UPDATE");
                    UILog.showWarn("Falscher Spieler für PLAYER_UPDATE");
                }
                break;
            case FLAG_PICKUP:
                MAP_HOLDER.getFlag().setPicker(
                    ENTITY_HOLDER.getPlayer(((PacketFlagPickup) packet).getPickerId()));
                break;
            case FLAG_DROP:
                PacketFlagDrop flagDrop = (PacketFlagDrop) packet;
                if (flagDrop.getLocation() != null) {
                    MAP_HOLDER.getFlag().setLocation(flagDrop.getLocation());
                } else if (MAP_HOLDER.getFlag().isPickedUp()) {
                    MAP_HOLDER.getFlag().drop();
                } else {
                    LogUtil.warn("Cannot drop flag! No Location and picker!");
                    UILog.showWarn("Kann Flagge nicht droppen! Keine Location oder Picker!");
                }
                break;
            case WIN:
                Client.MATCH_CONTROLLER.win(((PacketWin) packet).getTeam());
                Client.reset();
                break;
            case SHOT:
                ENTITY_HOLDER.addProjectile(((PacketShot) packet).getProjectile());
                break;
            case KICK:
                UILog.showInfo("Du wurdest vom Server gekicked!");
                break;
            case CHANGE_TEAM:
                Player user = ENTITY_HOLDER.getUser();
                switch(user.getTeam()) {

                    case BLUE:
                        user.setTeam(Team.RED);
                        break;
                    case RED:
                        user.setTeam(Team.BLUE);
                        break;
                }
                UILog.showInfo("Dein Team wurde geändert. Du bist jetzt in " + user.getTeam());
                ENTITY_HOLDER.getUser().spawn();
                break;
            case MATCH:
                PacketMatchInfo match = (PacketMatchInfo) packet;
                MATCH_CONTROLLER.update(match.getRedWins(), match.getBlueWins());
                break;
            case DISCONNECT:
                Player disconnectPlayer = ((PacketDisconnect) packet).getPlayer();
                if (ENTITY_HOLDER.contains(disconnectPlayer)) {
                    //remove the disconnected player
                    ENTITY_HOLDER.removePlayer(disconnectPlayer);
                    //set flag picker to null
                    if (MAP_HOLDER.getFlag().isPickedUp()
                        && MAP_HOLDER.getFlag().getPicker().equals(disconnectPlayer)) {
                        MAP_HOLDER.getFlag().drop();
                    }
                    LogUtil.info("Removed player " + disconnectPlayer.toString());
                } else {
                    LogUtil.warn("Can't remove unknown player " + disconnectPlayer.toString());
                }
                break;
        }
    }

    @Override
    public void unknownObject(Connection connection, Object object) {
        UILog.showWarn("Unbekanntes Object empfangen: " + object);
    }

    @Override
    protected void noUsage(Connection connection) {
        UILog.showWarn("Ein Packet ohne Anwendung wurde empfangen!");
    }

    @Override
    protected void onParsingError(Exception exc) {
        UILog.showWarn("Kann Packet nicht übersetzen!\n" + exc.getMessage());
    }
}
