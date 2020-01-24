package de.schweinefilet.prototype.client.network;

import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;

import com.esotericsoftware.kryonet.Server;
import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.ExecutableHandler;
import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.network.packet.Packet;
import de.schweinefilet.prototype.common.network.packet.PacketHandshake;
import de.schweinefilet.prototype.common.network.packet.PacketPlayerUpdate;
import de.schweinefilet.prototype.common.util.LogUtil;
import de.schweinefilet.prototype.common.util.NetworkUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class manages the networking and contains for example KryoNet's {@link com.esotericsoftware.kryonet.Client}.
 */
public class ClientNetworkHandler implements ExecutableHandler {

    /**
     * Kryo's {@link com.esotericsoftware.kryonet.Client} used for communicating with the server via the network.
     */
    private final com.esotericsoftware.kryonet.Client client;

    /**
     * The {@link InetAddress} of the {@link Server}.
     */
    private final InetAddress address;
    
    /**
     * Starts the {@link ClientNetworkHandler}.
     *
     * @param host the server ip address
     */
    public ClientNetworkHandler(String host) {
        LogUtil.info("Init: Network");

        InetAddress local = null;

        try {
            local = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            LogUtil.error("Can't get the IP-Address " + host);
            e.printStackTrace();
            UILog.showError("Kann die IP-Addresse " + host + " nicht finden!");
            Client.stop();
        }

        address = local;

        client = new com.esotericsoftware.kryonet.Client();
        client.addListener(new ClientListener());
        NetworkUtil.registerClasses(client.getKryo());
    }
    
    @Override
    public void start() {
        LogUtil.info("Start: Network");
        //expected by KryoNet
        client.start();

        new Thread(() -> {
            try {
                client.connect(5000, address, Vars.NETWORK.PORT_TCP, Vars.NETWORK.PORT_UDP);
                LogUtil.info("Connected to " + address);
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.error("Can't connect to server");
                e.printStackTrace();
                UILog.showBlockingError("Kann Verbindung nicht aufbauen!\n" + e.getMessage());
                Client.stop();
            }

            //check if the client is connected
            if (client.isConnected()) {
                //send the Handshake packet
                client.sendUDP(new PacketHandshake(ENTITY_HOLDER.getUser().asPlayer()));
            } else {
                LogUtil.error("No connection");
                UILog.showBlockingError(
                    "Kann Verbindung nicht aufbauen!\n isConnected: " + client.isConnected());
                Client.stop();
            }
        }).start();
    }
    
    
    @Override
    public void stop() {
        LogUtil.info("Stop: Network");
        client.close();
    }
    
    /**
     * Sends a {@link PacketPlayerUpdate} object to the server when the client is connected.
     */
    public void updateUserLocation() {
        sendPacket(new PacketPlayerUpdate(ENTITY_HOLDER.getUser().asPlayer()));
    }

    /**
     * Returns {@link com.esotericsoftware.kryonet.Client#isConnected()}.
     * @return -
     */
    public boolean isConnected() {
        return client.isConnected();
    }

    /**
     * Returns the server's address.
     * @return -
     */
    public String getServerAddress() {
        return client.getRemoteAddressUDP().getAddress().getHostName();
    }

    /**
     * Sends a {@link Packet} to the server.
     * @param t the {@link Packet} that is sent.
     * @param <T> a class that extends {@link Packet}
     */
    public <T extends Packet> void sendPacket(T t) {
        LogUtil.info("Send " + t.getUsage());
        client.sendUDP(t);
    }
}
