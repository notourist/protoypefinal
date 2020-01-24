package de.schweinefilet.prototype.common.util;

import com.esotericsoftware.kryo.Kryo;
import de.schweinefilet.prototype.common.entity.Entity;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.entity.Projectile;
import de.schweinefilet.prototype.common.entity.Team;
import de.schweinefilet.prototype.common.network.packet.DenyReason;
import de.schweinefilet.prototype.common.network.packet.Packet;
import de.schweinefilet.prototype.common.network.packet.PacketChangeTeam;
import de.schweinefilet.prototype.common.network.packet.PacketDisconnect;
import de.schweinefilet.prototype.common.network.packet.PacketFlagDrop;
import de.schweinefilet.prototype.common.network.packet.PacketFlagPickup;
import de.schweinefilet.prototype.common.network.packet.PacketHandshake;
import de.schweinefilet.prototype.common.network.packet.PacketKick;
import de.schweinefilet.prototype.common.network.packet.PacketPlayerUpdate;
import de.schweinefilet.prototype.common.network.packet.PacketMatchInfo;
import de.schweinefilet.prototype.common.network.packet.PacketShot;
import de.schweinefilet.prototype.common.network.packet.PacketUsage;
import de.schweinefilet.prototype.common.network.packet.PacketWin;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

/**
 * Contains methods to properly setup KryoNet.
 */
public class NetworkUtil {

    private NetworkUtil() {
    }

    /**
     * Register all the needed classes with {@link Kryo#register(Class)}.
     *
     * @param kryo the Kryo object, which can be obtain from a {@link com.esotericsoftware.kryonet.Client}
     * or {@link com.esotericsoftware.kryonet.Server}
     */
    public static void registerClasses(Kryo kryo) {
        //register all the basic classes
        kryo.register(Rectangle.class);
        kryo.register(Line2D.Float.class);
        //register all the Entity classes
        kryo.register(Entity.class);
        kryo.register(Projectile.class);
        kryo.register(Point.class);
        kryo.register(Player.class);
        kryo.register(Team.class);
        //register all the packet classes
        kryo.register(PacketUsage.class);
        kryo.register(DenyReason.class);
        kryo.register(Packet.class);
        kryo.register(PacketHandshake.class);
        kryo.register(PacketMatchInfo.class);
        kryo.register(PacketKick.class);
        kryo.register(PacketChangeTeam.class);
        kryo.register(PacketPlayerUpdate.class);
        kryo.register(PacketFlagPickup.class);
        kryo.register(PacketFlagDrop.class);
        kryo.register(PacketShot.class);
        kryo.register(PacketWin.class);
        kryo.register(PacketDisconnect.class);
    }
}
