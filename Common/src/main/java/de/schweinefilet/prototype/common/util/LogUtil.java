package de.schweinefilet.prototype.common.util;

/**
 * Contains methods for logging on different log levels.
 */
public class LogUtil {

    /**
     * Interesting messages and debug information.
     */
    public static int INFO = 0;

    /**
     * Problems, that COULD have a big impact e.g. crash the game.
     */
    public static int WARN = 1;

    /**
     * Any problem that actually crashes the game/needs a restart of the game.
     */
    public static int ERROR = 2;

    /**
     * The current log level.
     */
    private static int level = 1;

    private LogUtil() {
    }

    /**
     * Prints a error message.
     * @param msg the error message
     */
    public static void error(String msg) {
        if (level <= 2) {
            System.out.println("[ERROR] " + msg);
        }
    }

    /**
     * Prints a warning message.
     * @param msg the warning message
     */
    public static void warn(String msg) {
        if (level <= 1) {
            System.out.println("[WARNING] " + msg);
        }
    }

    /**
     * Prints a info message.
     * @param msg the info message
     */
    public static void info(String msg) {
        if (level <= 0) {
            System.out.println("[INFO] " + msg);
        }
    }

    /**
     * Sets the log level.
     * @param level the new log level
     */
    public static void setLevel(int level) {
        LogUtil.level = level;
    }
}
