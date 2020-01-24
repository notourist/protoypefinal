package de.schweinefilet.prototype.client.util;

import javax.swing.JOptionPane;

/**
 * An utility class which contains methods to show the user error and warning messages.
 */
public class UILog {

    private static String link = "\nBesuche https://git.kvfg.eu/nasarelu/PrototypeFinal/wikis/Meldungen um mehr zu erfahren.";

    private UILog() {
    }

    /**
     * Shows the user a visual error message
     * without blocking any game relevant thread.
     *
     * @param message the error message
     */
    public static void showError(String message) {
        new Thread(() -> JOptionPane.showConfirmDialog(null,
            message + link,
            "Fehler",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null)).start();
    }

    /**
     * Shows the user a visual warning message
     * without blocking any game relevant thread.
     *
     * @param message the warning message
     */
    public static void showWarn(String message) {
        new Thread(() -> JOptionPane.showConfirmDialog(null,
            message + link,
            "Warnung",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null)).start();
    }

    /**
     * Shows the user a visual information message
     * without blocking any game relevant thread.
     *
     * @param message the info message
     */
    public static void showInfo(String message) {
        new Thread(() -> JOptionPane.showConfirmDialog(null,
            message,
            "Information",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null)).start();
    }

    /**
     * Shows the user a visual error message that blocks
     * the thread its started from.
     *
     * @param message the error message
     */
    public static void showBlockingError(String message) {
        JOptionPane.showConfirmDialog(null,
            message + link,
            "Fehler",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null);
    }

    /**
     * Shows the user a visual information message
     * without blocking any game relevant thread.
     *
     * @param message the info message
     */
    public static void showBlockingInfo(String message) {
        JOptionPane.showConfirmDialog(null,
            message,
            "Information",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null);
    }
}
