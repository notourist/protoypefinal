package de.schweinefilet.prototype.server;

import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.Vars.SERVER;
import de.schweinefilet.prototype.common.util.LogUtil;
import de.schweinefilet.prototype.server.gui.GUI;
import de.schweinefilet.prototype.server.map.ServerMapHolder;
import de.schweinefilet.prototype.server.match.ServerMatchController;
import de.schweinefilet.prototype.server.network.ServerNetworkHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The server's main class.
 */
public class Server {

    public static ServerNetworkHandler NETWORK_HANDLER;
    public static ServerMapHolder MAP_HOLDER;
    public static GUI GUI;
    public static ServerMatchController MATCH_CONTROLLER;

    /**
     * The arguments used to start the server.
     */
    private static String[] startArgs;

    /**
     * This the entry point for the application.
     * Possible arguments are:
     * <ul>
     * <li>map - Sets the map by the map name.</li>
     * <li>ui - Opens the GUI.</li>
     * <li>autorestart - Restarts the server automatically.</li>
     * </ul>
     *
     * @param args the server start arguments, for example the map name
     */
    public static void main(String... args) {
        System.out.println("Starting...");
        System.out.println("______          _                        ______ _             _            _____\n"
            + "| ___ \\        | |                       |  ___(_)           | |          /  ___|\n"
            + "| |_/ / __ ___ | |_ ___  _   _ _ __   ___| |_   _ _ __   __ _| |  ______  \\ `--.  ___ _ ____   _____ _ __ \n"
            + "|  __/ '__/ _ \\| __/ _ \\| | | | '_ \\ / _ \\  _| | | '_ \\ / _` | | |______|  `--. \\/ _ \\ '__\\ \\ / / _ \\ '__|\n"
            + "| |  | | | (_) | || (_) | |_| | |_) |  __/ |   | | | | | (_| | |          /\\__/ /  __/ |   \\ V /  __/ |\n"
            + "\\_|  |_|  \\___/ \\__\\___/ \\__, | .__/ \\___\\_|   |_|_| |_|\\__,_|_|          \\____/ \\___|_|    \\_/ \\___|_|\n"
            + " _                  __    __/ | |\n"
            + "| |                / _|  |___/|_|\n"
            + "| |__  _   _   ___| |_\n"
            + "| '_ \\| | | | / __|  _|\n"
            + "| |_) | |_| | \\__ \\ |\n"
            + "|_.__/ \\__, | |___/_|\n"
            + "        __/ |\n"
            + "       |___/");
        LogUtil.setLevel(LogUtil.INFO);
        startArgs = args;
        LogUtil.info("SERVER version: " + Vars.VERSION);
        //setup option parser
        Options options = new Options();
        options.addOption("map", true, "Sets the map.");
        options.addOption("gui", "Opens the GUI.");
        options.addOption("autorestart", "Restarts the server automatically.");

        CommandLine cmd = null;

        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            LogUtil.error("Can't parse options.");
            e.printStackTrace();
            System.exit(11);
        }

        if (!cmd.hasOption("map")) {
            LogUtil.error("No map specified.");
            System.exit(12);
        }

        if (cmd.hasOption("autorestart")) {
            SERVER.AUTO_RESTART = true;
        }

        MATCH_CONTROLLER = new ServerMatchController();
        MAP_HOLDER = new ServerMapHolder();
        NETWORK_HANDLER = new ServerNetworkHandler();

        NETWORK_HANDLER.start();

        MAP_HOLDER.setMap(cmd.getOptionValue("map"));

        if (cmd.hasOption("gui")) {
            JFrame frame = new JFrame("SERVER - " + cmd.getOptionValue("map"));
            GUI = new GUI();

            frame.setContentPane(GUI.getPanel());
            frame.setSize(1100, 220);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    /**
     * Calls {@link GUI#update()} if the {@link Server#GUI} is not null.
     */
    public static void updateGUI() {
        if (GUI != null) {
            GUI.update();
        }
    }

    /**
     * Restarts the server but without output.
     */
    public static void restart() {
        NETWORK_HANDLER.stop();
        LogUtil.info("Starting new server instance...");
        String args[] = new String[startArgs.length+3];
        args[0] = "java";
        args[1] = "-jar";
        args[2] = "server.jar";
        System.arraycopy(startArgs, 0, args, 3, startArgs.length);
        try {
            new ProcessBuilder(args)
                .directory(new File(Paths.get("").toAbsolutePath().toString()))
                .start();
            LogUtil.info("New server instance started!");
        } catch (IOException e) {
            LogUtil.error("Couldn't restart server instance!");
            e.printStackTrace();
        }
    }
}
