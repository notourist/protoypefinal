package de.schweinefilet.prototype.client.frame;

import de.schweinefilet.prototype.client.GameLoop;

/**
 * Calls the repaint method of the {@link GameFrame}.
 */
public class PaintLoop extends GameLoop {

    /**
     * The {@link GameFrame} used for easier access to {@link GameFrame#repaint()}.
     */
    private final GameFrame gameFrame;

    PaintLoop(GameFrame gameFrame) {
        super("Paint", 60);
        this.gameFrame = gameFrame;
    }

    /**
     * Calls {@link GameFrame#repaint()}.
     * @param delta indicates how long the loop took
     */
    @Override
    public void process(double delta) {
        gameFrame.repaint();
    }

    /**
     * Adds the {@link GameFrame}'s listeners on the first run.
     */
    @Override
    protected void onFirstRun() {
        gameFrame.addListeners();
    }
}
