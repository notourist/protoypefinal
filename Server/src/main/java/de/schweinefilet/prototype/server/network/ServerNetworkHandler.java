package de.schweinefilet.prototype.server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import de.schweinefilet.prototype.common.ExecutableHandler;
import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.network.packet.Packet;
import de.schweinefilet.prototype.common.network.packet.PacketDisconnect;
import de.schweinefilet.prototype.common.util.LogUtil;
import de.schweinefilet.prototype.common.util.NetworkUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import lombok.Getter;

/**
 * This class manages the networking and contains for example KryoNet's {@link Server}.
 */
public class ServerNetworkHandler implements ExecutableHandler {

    /**
     * Kryo's {@link Server} object.
     */
    @Getter
    private final Server server;

    /**
     * Holds all the connections with their respective {@link Player}.
     * Used for sending {@link PacketDisconnect}.
     */
    private final HashMap<Connection, Player> connections;

    public ServerNetworkHandler() {
        server = new Server();
        connections = new HashMap<>();
        server.addListener(new ServerListener());
        NetworkUtil.registerClasses(server.getKryo());
    }

    @Override
    public void start() {
        LogUtil.info("Start: Network");
        server.start();

        try {
            server.bind(Vars.NETWORK.PORT_TCP, Vars.NETWORK.PORT_UDP);
            LogUtil.info("Bound server");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error("Can't bind server");
            System.exit(0);
        }
    }

    @Override
    public void stop() {
        server.stop();
    }

    /**
     * Sends a {@link Packet} to every {@link Connection} except the given one.
     * @param exception the {@link Connection} that is used as an exception
     * @param t the sent packet
     * @param <T> every class that extends {@link Packet}
     */
    <T extends Packet> void sendUDPExcept(Connection exception, T t) {
        for (Connection connection : server.getConnections()) {
            if (connection != exception) {
                connection.sendUDP(t);
            }
        }
    }

    /**
     * Adds a new {@link Connection} with the respective {@link Player}.
     * @param connection the new connection
     * @param receivedPlayer the respective player
     */
    void addConnection(Connection connection, Player receivedPlayer) {
        connections.put(connection, receivedPlayer);
    }

    /**
     * Removes a connection.
     * @param connection -
     */
    void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    /**
     * True, if the {@link Connection} exists, else false.
     * @param connection -
     * @return -
     */
    boolean contains(Connection connection) {
        return connections.containsKey(connection);
    }

    /**
     * Returns the {@link Player} connected with the {@link Connection}.
     * @param connection -
     * @return -
     */
    Player getAffiliatedPlayer(Connection connection) {
        return connections.get(connection);
    }

    /**
     * Returns a {@link Connection} found via the respective {@link Player}.
     * @param player -
     * @return -
     */
    public Connection getAffiliatedConnection(Player player) {
        return Arrays.stream(server.getConnections())
            .filter(connection -> player.equals(getAffiliatedPlayer(connection))).findFirst()
            .orElse(null);
    }
}
