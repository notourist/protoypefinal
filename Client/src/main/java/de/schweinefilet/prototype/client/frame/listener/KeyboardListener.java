package de.schweinefilet.prototype.client.frame.listener;

import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.control.ControlHandler;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategyImage;
import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.util.LogUtil;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static de.schweinefilet.prototype.client.Client.*;

/**
 * This listener checks the keyboard input.
 * The current keyboard layout is:
 * <ul>
 * <li>W - go up</li>
 * <li>A - go left</li>
 * <li>S - go down</li>
 * <li>D - go right</li>
 * <li>E - DISABLED</li>
 * <li>F - interact via {@link de.schweinefilet.prototype.client.entity.User#interact()}</li>
 * <li>Q - quit via {@link Client#stop()}</li>
 * <li>P - fix a to small image by calling {@link PaintStrategyImage#recreateImage()}</li>
 * <li>I - show some stats</li>
 * </ul>
 */
public class KeyboardListener implements KeyListener {

    public void keyTyped(KeyEvent event) {
    }

    public void keyPressed(KeyEvent event) {
        switch(event.getKeyCode()) {
            case KeyEvent.VK_W:
                ENTITY_HOLDER.getUser().setUp(true);
                break;
            case KeyEvent.VK_A:
                ENTITY_HOLDER.getUser().setLeft(true);
                break;
            case KeyEvent.VK_S:
                ENTITY_HOLDER.getUser().setDown(true);
                break;
            case KeyEvent.VK_D:
                ENTITY_HOLDER.getUser().setRight(true);
                break;
            //manual respawn
            case KeyEvent.VK_R:
                LogUtil.info("Manually respawned");
                ENTITY_HOLDER.getUser().spawn();
                break;
            //teleport
            case KeyEvent.VK_E:
                Point mouseLoc = CONTROL_HANDLER.getMouse().getLocation();
                if (mouseLoc.distance(FRAME_HANDLER.getWidth() / 2,
                    FRAME_HANDLER.getHeight() / 2) <= Vars.CONTROL.TELEPORT_DISTANCE) {
                    ENTITY_HOLDER.getUser().teleport(mouseLoc);
                }
                break;
            //pickup
            case KeyEvent.VK_F:
                //pickup flag
                ENTITY_HOLDER.getUser().interact();
                break;
            //recreate the image
            case KeyEvent.VK_P:
                if (FRAME_HANDLER.getPaintStrategy().isImagePowered()) {
                    try {
                        ((PaintStrategyImage) FRAME_HANDLER.getPaintStrategy()).recreateImage();
                        LogUtil.info("Tried to recreate the image...");
                    } catch(IllegalArgumentException exc) {
                        LogUtil.warn("Can't recreate image");
                        exc.printStackTrace();
                    }
                }
                break;
            //close
            case KeyEvent.VK_I:
                String info = "Version: " + Vars.VERSION
                    + "\nResolution: " + FRAME_HANDLER.getWidth() + "/" + FRAME_HANDLER.getHeight()
                    + "\nPaintStrategy: " + FRAME_HANDLER.getPaintStrategy().getName()
                    + "\nServer-IP: " + NETWORK_HANDLER.getServerAddress()
                    + "\nRedWins: " + MATCH_CONTROLLER.getRedWins()
                    + "\nBlueWins: " + MATCH_CONTROLLER.getBlueWins();

                if (!FRAME_HANDLER.getPaintStrategy().isImagePowered()) {
                    info += "\n MultiBufferAvailable: " + FRAME_HANDLER.getBufferStrategy()
                        .getCapabilities().isMultiBufferAvailable();
                }
                UILog.showInfo(info);
                break;
            case KeyEvent.VK_Q:
                Client.stop();
                break;
        }
    }

    public void keyReleased(KeyEvent event) {

        switch(event.getKeyCode()) {
            case KeyEvent.VK_W:
                ENTITY_HOLDER.getUser().setUp(false);
                break;
            case KeyEvent.VK_A:
                ENTITY_HOLDER.getUser().setLeft(false);
                break;
            case KeyEvent.VK_S:
                ENTITY_HOLDER.getUser().setDown(false);
                break;
            case KeyEvent.VK_D:
                ENTITY_HOLDER.getUser().setRight(false);
                break;
        }
    }
}
