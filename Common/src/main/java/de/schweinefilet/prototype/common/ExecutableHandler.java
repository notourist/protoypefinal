package de.schweinefilet.prototype.common;

/**
 * A handler that can be started and stopped.
 */
public interface ExecutableHandler {

    /**
     * Starts the handler execution.
     */
    void start();

    /**
     * Stops the handler execution.
     */
    void stop();
}
