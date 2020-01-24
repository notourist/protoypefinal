package de.schweinefilet.prototype.client;

import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.util.LogUtil;
import lombok.Setter;

/**
 * <p>Used to modify data with a fixed time. <p>This loop was originally made by Kevin Class <a
 * href="http://www.cokeandcode.com/info/showsrc/showsrc.php?src=../spaceinvaders102/org/newdawn/spaceinvaders/Game.java">
 * here</a>. Found on <a href="http://www.java-gaming.org/index.php?topic=24220.0">java-gaming.com</a>.
 * Edited and fixed by Lukas aka schweinefilet.</p>
 */
public abstract class GameLoop extends Thread {

    /**
     * Indicates if the loop is running.
     */
    @Setter
    private boolean running;

    /**
     * The ticks per second that should be reached.
     */
    private final int targetTps;

    /**
     * The time the last tick was "run".
     */
    private long lastTickTime;

    /**
     * The ticks per second that are actually achieved.
     */
    private int tps;

    /**
     * The Client Loop's name.
     */
    private final String name;

    /**
     * The optimal loop time.
     */
    private long optimalTime ;

    /**
     * True, when the loop calls {@link GameLoop#process(double)} for the first time.
     */
    private boolean firstRun;

    /**
     * Creates a game loop with the given name.
     *
     * @param name the Client Loop's name
     * @param targetTps the TPS that should be targeted, but aren't guaranteed
     */
    protected GameLoop(String name, int targetTps) {
        this.targetTps = targetTps;
        this.name = name;
        //sets the Thread's name for easier debugging
        setName("GameLoop: " + name);
        firstRun = true;
        optimalTime = 1000000000 / targetTps;
    }

    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();

        while (running) {

            //calculate delta and update length
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            //for an explanation of the delta value see GameLoop#process(delta)
            double delta = updateLength / ((double) optimalTime);

            //process the frame counter
            lastTickTime += updateLength;
            tps++;

            //check if one second has passed
            if (lastTickTime >= 1000000000) {
                LogUtil.info(name + "-TPS: " + tps);
                lastTickTime = 0;
                tps = 0;
            }

            if (Client.isReady()) {
                if (firstRun) {
                    onFirstRun();
                    firstRun = false;
                }
                process(delta);
            }

            long sleep = (lastLoopTime - System.nanoTime() + optimalTime) / 1000000;

            try {
                //fixes crashing because sleep is smaller than 0
                Thread.sleep(sleep > 0 ? sleep : 0);
            } catch (InterruptedException | IllegalArgumentException e) {
                String crashReport =
                    "-----------------------<GAMELOOP CRASH>----------------------\n" +
                        "A game loop crashed. Please restart the game!\n" +
                        "Values: name: " + name + "\n" +
                        "        targetTps: " + targetTps + "\n" +
                        "        tps: " + tps + "\n" +
                        "        lastTickTime: " + lastTickTime + "\n" +
                        "        updateLength: " + updateLength + "\n" +
                        "        delta: " + delta + "\n" +
                        "        lastLoopTime:" + lastLoopTime + "\n" +
                        "        optimalTime: " + optimalTime + "\n" +
                        "        nanoTime (can be wrong): " + System.nanoTime() + "\n" +
                        "        calculated sleep time: "
                        + (lastLoopTime - System.nanoTime() + optimalTime) / 1000000 + "\n" +
                        "-----------------------<END OF REPORT>----------------------" + "\n";
                LogUtil.error(crashReport);
                e.printStackTrace();
                UILog.showError(crashReport);
                Client.stop();
            }
        }
    }

    /**
     * Called by the loop with a fixed time.
     * <p>The delta values shows how long the loop took.
     * A 1.0 delta means it took as long as expected.
     * When the value is bigger, the loop took longer than expected.
     * When the values is smaller, it took shorter than expected.</p>
     *
     * @param delta indicates how long the loop took
     */
    protected abstract void process(double delta);

    /**
     * Called on the first run.
     */
    protected abstract void onFirstRun();
}
