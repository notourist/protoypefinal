package de.schweinefilet.prototype.common;

import de.schweinefilet.prototype.common.entity.Flag;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.entity.Spawn;
import de.schweinefilet.prototype.common.entity.Team;

/**
 * Contains static variables that both client and server use.
 */
public class Vars {

    /**
     * The maximal number of wins a {@link Team} can score.
     */
    public static final int MAX_WINS = 3;

    /**
     * The current game version. Used for debugging.
     * Determined by using "git rev-list --count -all".
     * How to read the version:
     * <ul>
     *     <li>r.X - the release version X</li>
     *     <li>d.Y - the development version Y</li>
     * </ul>
     */
    public static final String VERSION = "r.1-d.307";

    /**
     * Contains all control related variables.
     */
    public static final class CONTROL {

        /**
         * The {@link Player}'s moving velocity.
         */
        public static final int VELOCITY = 12;

        /**
         * The maximal distance between a {@link Player} and a {@link Flag} in which he can pick
         * the {@link Flag} up.
         */
        public static final int FLAG_PICK = 70;

        /**
         * The maximal distance between a {@link Player} and a {@link Spawn} in which he can lay
         * the {@link Flag} down.
         */
        public static final int FLAG_LAY = 70;

        /**
         * The maximal distance a player can teleport.
         */
        public static final int TELEPORT_DISTANCE = 700;

    }

    /**
     * Contains all network related variables.
     */
    public static final class NETWORK {

        /**
         * The port that is used for the network communication over TCP.
         */
        public static final int PORT_TCP = 4723;

        /**
         * The port that is used for the network communication over UDP.
         */
        public static final int PORT_UDP = 4722;
    }

    /**
     * Contains special server variables.
     */
    public static final class SERVER {
        /**
         * If the server should restart after the game ends true, else false.
         */
        public static boolean AUTO_RESTART = false;
    }
}
