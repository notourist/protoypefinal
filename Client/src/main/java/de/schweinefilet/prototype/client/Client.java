package de.schweinefilet.prototype.client;

import de.schweinefilet.prototype.client.control.ControlHandler;
import de.schweinefilet.prototype.client.entity.EntityHolder;
import de.schweinefilet.prototype.client.frame.FrameHandler;
import de.schweinefilet.prototype.client.frame.HUD.HUDHolder;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategy;
import de.schweinefilet.prototype.client.map.ClientMapHolder;
import de.schweinefilet.prototype.client.match.ClientMatchController;
import de.schweinefilet.prototype.client.network.ClientNetworkHandler;
import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.util.LogUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The client's main class. Contains all the instances and variables.
 */
public class Client {

    public static FrameHandler FRAME_HANDLER;
    public static ControlHandler CONTROL_HANDLER;
    public static ClientNetworkHandler NETWORK_HANDLER;
    public static ClientMapHolder MAP_HOLDER;
    public static EntityHolder ENTITY_HOLDER;
    public static ClientMatchController MATCH_CONTROLLER;
    public static HUDHolder HUD_HOLDER;

    /**
     * Indicates if the client is already stopping.
     */
    private static boolean stopping = false;

    /**
     * This is the entry point for the game.
     * Possible arguments are:
     * <ul>
     * <li>name - Sets the user's name.</li>
     * <li>team - Sets the user's team.</li>
     * <li>ip - Sets the server ip.</li>
     * <li>version - Prints the version and exits the game.</li>
     * <li>log - Sets the log level. For more information see {@link LogUtil#setLevel(int)}.</li>
     * <li>strategy - Sets the {@link PaintStrategy}.</li>
     * </ul>
     *
     * @param args the game parameters, which get parsed by commons-cli
     */
    public static void main(String... args) {
        System.out.println("Starting...");
        System.out.println("______          _                        ______ _             _            _____ _ _            _\n"
            + "| ___ \\        | |                       |  ___(_)           | |          /  __ \\ (_)          | |\n"
            + "| |_/ / __ ___ | |_ ___  _   _ _ __   ___| |_   _ _ __   __ _| |  ______  | /  \\/ |_  ___ _ __ | |_ \n"
            + "|  __/ '__/ _ \\| __/ _ \\| | | | '_ \\ / _ \\  _| | | '_ \\ / _` | | |______| | |   | | |/ _ \\ '_ \\| __|\n"
            + "| |  | | | (_) | || (_) | |_| | |_) |  __/ |   | | | | | (_| | |          | \\__/\\ | |  __/ | | | |_ \n"
            + "\\_|  |_|  \\___/ \\__\\___/ \\__, | .__/ \\___\\_|   |_|_| |_|\\__,_|_|           \\____/_|_|\\___|_| |_|\\__|\n"
            + " _                  __    __/ | |\n"
            + "| |                / _|  |___/|_|\n"
            + "| |__  _   _   ___| |_\n"
            + "| '_ \\| | | | / __|  _|\n"
            + "| |_) | |_| | \\__ \\ |\n"
            + "|_.__/ \\__, | |___/_|\n"
            + "        __/ |\n"
            + "       |___/");
        //setup option parser
        Options options = new Options();
        options.addOption("name", true, "Sets the client name");
        options.addOption("ip", true, "Sets the server ip.");
        options.addOption("version", false, "Prints the version and exits the game.");
        options.addOption("team", true, "Sets the user's team.");
        options.addOption("log", true,
            "Sets the log level. For more information see LogUtil#setLevel(int level).");
        options.addOption("strategy", true, "Sets the PaintStrategy.");

        CommandLine cmd = null;

        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            LogUtil.error("Can't parse options");
            e.printStackTrace();
            UILog.showError("Kann Optionen nicht übersetzen!\n" + e.getLocalizedMessage());
            System.exit(11);
        }

        if (cmd.hasOption("log")) {
            LogUtil.info("Setting log level to " + cmd.getOptionValue("log"));
            LogUtil.setLevel(Integer.parseInt(cmd.getOptionValue("log")));
        } else {
            LogUtil.info("Setting log level to INFO");
            LogUtil.setLevel(LogUtil.INFO);
        }

        LogUtil.info("Client version: " + Vars.VERSION);

        if (cmd.hasOption("version")) {
            System.exit(0);
        }

        //check if options are set
        if (!cmd.hasOption("name")) {
            LogUtil.error("No name defined.");
            UILog.showBlockingError("Kein Name definiert!");
            System.exit(12);
        }

        if (!cmd.hasOption("ip")) {
            LogUtil.error("No IP address defined.");
            UILog.showBlockingError("Keine IP-Addresse definiert");
            System.exit(13);
        }

        if (!cmd.hasOption("team")) {
            LogUtil.error("No team defined.");
            UILog.showBlockingError("Kein Team definiert!");
            System.exit(14);
        }

        if (!cmd.hasOption("strategy")) {
            LogUtil.error("No PaintStrategy defined.");
            UILog.showBlockingError("Keine PaintStrategy definiert!");
            System.exit(15);
        }

        init(cmd.getOptionValue("ip"),
            cmd.getOptionValue("name"),
            cmd.getOptionValue("team"),
            Integer.parseInt(cmd.getOptionValue("strategy")));
        start();
    }

    /**
     * Initializes all handler instances.
     *
     * @param host the server ip address
     * @param name the user's name
     * @param team the users' team
     * @param strategy the client's {@link PaintStrategy}
     */
    private static void init(String host, String name, String team, int strategy) {
        ENTITY_HOLDER = new EntityHolder(name, team);
        MAP_HOLDER = new ClientMapHolder();
        MATCH_CONTROLLER = new ClientMatchController();
        CONTROL_HANDLER = new ControlHandler();
        FRAME_HANDLER = new FrameHandler(strategy);
        HUD_HOLDER = new HUDHolder();
        NETWORK_HANDLER = new ClientNetworkHandler(host);
    }

    /**
     * Starts the handlers.
     */
    private static void start() {
        CONTROL_HANDLER.start();
        FRAME_HANDLER.start();
        NETWORK_HANDLER.start();
    }

    /**
     * Stops all handlers.
     * Can be only called once.
     */
    public static void stop() {
        if (!stopping) {
            stopping = true;
            NETWORK_HANDLER.stop();
            CONTROL_HANDLER.stop();
            FRAME_HANDLER.stop();
            System.exit(0);
        }
    }

    /**
     * Forces the client to stop. Only called when the client wasn't able to initialize correctly
     * in the first place.
     */
    public static void dirtyStop() {
        System.out.println("Executing Order 66...");
        System.exit(66);
    }

    /**
     * Resets the game.
     */
    public static void reset() {
        MAP_HOLDER.resetMap();
        ENTITY_HOLDER.getUser().setTeleported(false);
    }

    /**
     * Checks if the client is ready to be displayed and handle user input.
     * @return true, if the game is ready, else false
     */
    static boolean isReady() {
        return NETWORK_HANDLER.isConnected() && MAP_HOLDER.isLoaded();
    }
}