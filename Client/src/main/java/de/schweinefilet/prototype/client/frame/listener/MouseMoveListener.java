package de.schweinefilet.prototype.client.frame.listener;

import de.schweinefilet.prototype.client.Client;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * This listener checks the mouse movement and updates the game mouse.
 */
public class MouseMoveListener implements MouseMotionListener {

    @Override
    public void mouseDragged(MouseEvent e) {
        Client.CONTROL_HANDLER.getMouse().setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Client.CONTROL_HANDLER.getMouse().setLocation(e.getX(), e.getY());
    }
}
