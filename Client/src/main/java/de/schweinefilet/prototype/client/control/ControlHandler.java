package de.schweinefilet.prototype.client.control;

import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;
import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;

import de.schweinefilet.prototype.common.ExecutableHandler;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.util.LogUtil;
import java.awt.Point;
import lombok.Getter;
import lombok.Setter;
/**
 * This class contains the {@link ControlLoop} and methods to parse and handle user input.
 */
public class ControlHandler implements ExecutableHandler {

    /**
     * The {@link ControlLoop} who processes the user input.
     */
    private final ControlLoop controlLoop;

    /**
     * Represents the ingame mouse.
     */
    @Getter
    private final Mouse mouse;

    public ControlHandler() {
        LogUtil.info("Init: Control");

        mouse = new Mouse();
        controlLoop = new ControlLoop();
    }

    @Override
    public void start() {
        LogUtil.info("Start: Control");
        controlLoop.setRunning(true);
        controlLoop.start();
    }

    @Override
    public void stop() {
        LogUtil.info("Stop: Control");
        controlLoop.setRunning(false);
    }

    /**
     * Calculates the mouse point with the offsets.
     * @return the calculated point of the mouse
     */
    public Point calculateMousePoint() {
        Player user = ENTITY_HOLDER.getUser();
        int mouseX = mouse.getLocation().x;
        int mouseY = mouse.getLocation().y;
        int playerX = FRAME_HANDLER.getWidth() / 2;
        int playerY = FRAME_HANDLER.getHeight() / 2;
        //calculate the difference between them
        int diffX = playerX - mouseX;
        int diffY = playerY - mouseY;

        //check if the differences are positive, when not make them positive
        if (diffX < 0) {
            diffX = -diffX;
        }
        if (diffY < 0) {
            diffY = -diffY;
        }

        //check where the mouse is and recalculated the value
        if (mouseX < playerX) {
            diffX = -diffX;
        }

        if (mouseY < playerY) {
            diffY = -diffY;
        }

        //return the new position
        return new Point(user.getLocation().x + diffX,
            user.getLocation().y + diffY);
    }
}
