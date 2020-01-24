package de.schweinefilet.prototype.common.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.esotericsoftware.kryonet.Listener;
import de.schweinefilet.prototype.common.network.packet.Packet;
import de.schweinefilet.prototype.common.network.packet.PacketUsage;
import de.schweinefilet.prototype.common.util.LogUtil;

/**
 * Methods that get fired when a packet was received and
 * error handling methods.
 */
public abstract class PacketListener extends Listener {

    /**
     * Called when an object was received. Checks if the object is a {@link Packet} and has a {@link
     * PacketUsage}. When the object isn't a packet {@link PacketListener#unknownObject(Connection,
     * Object)} is called or, when the {@link PacketListener} has no usage {@link
     * PacketListener#noUsage(Connection)} is called. When the object is a packet and has an usage
     * {@link PacketListener#processPacket(Connection, Packet)} gets called so the packet can be
     * handled.
     *
     * @param connection the connection the object is from
     * @param object the received object
     */
    @Override
    public void received(Connection connection, Object object) {
        //check if the object is a packet
        if (object instanceof Packet) {
            Packet packet = (Packet) object;
            //check the packet usage
            if (packet.getUsage() != null) {
                if (connection != null) {
                    LogUtil.info("Received " + packet.getUsage());
                    try {
                        processPacket(connection, packet);
                    } catch (Exception exc) {
                        LogUtil.warn("Packet parsing error! Closing connection!");
                        exc.printStackTrace();
                        onParsingError(exc);
                        connection.close();
                    }
                } else {
                    LogUtil.warn("The listener was fired without a connection!");
                }
            } else {
                noUsage(connection);
            }
        } else {
            if (!object.toString()
                .contains("com.esotericsoftware.kryonet.FrameworkMessage$KeepAlive@")) {
                LogUtil.warn("Received UNKNOWN " + object);
                unknownObject(connection, object);
            }
        }
    }

    /**
     * Called when a packet couldn't be parsed.
     *
     * @param exc the occurred {@link Exception}
     */
    protected abstract void onParsingError(Exception exc);

    /**
     * Called when a fully qualified packet was received
     *
     * @param connection the connection which send the packet
     * @param packet the received packet
     */
    protected abstract void processPacket(Connection connection, Packet packet);

    /**
     * Called when an unknown object was received and it isn't Kryo's {@link KeepAlive} object.
     *
     * @param connection the connection that send the object
     * @param object the unknown object.
     */
    protected abstract void unknownObject(Connection connection, Object object);

    /**
     * Called when a packet has no usage.
     *
     * @param connection the connection that send the packet
     */
    protected abstract void noUsage(Connection connection);
}
