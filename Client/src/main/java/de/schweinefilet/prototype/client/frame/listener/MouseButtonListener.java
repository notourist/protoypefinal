package de.schweinefilet.prototype.client.frame.listener;

import static de.schweinefilet.prototype.client.Client.CONTROL_HANDLER;
import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;

import de.schweinefilet.prototype.client.control.ControlHandler;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This listener checks the mouse button input.
 * The current mouse layout is:
 * <ul>
 * <li>Button 1 - shoot via {@link de.schweinefilet.prototype.client.entity.User#shoot()}</li>
 * </ul>
 */
public class MouseButtonListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            ENTITY_HOLDER.getUser().shoot();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}